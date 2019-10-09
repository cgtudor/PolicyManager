/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policymanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8002382
 */
public class PolicyManager
{

    static Scanner key = new Scanner(System.in);
    static int[][] priceTable = new int[3][3]; //holding all the prices in a matrix for easier access
    
    public static void main(String[] args)
    {
        setPrices();
        menuRepeater();
    }
    
    //Method that returns the date to a string
    public static String getDate()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(cal.getTime());
    }
    
    //Method that keeps repeating the selection process until user presses 0
    //It calls itself forever, unless the user inputs 0
    public static void menuRepeater()
    {
        displayMenu();
        setPrices();
        int selection = key.nextInt();
        key.nextLine();
        switch(selection)
        {
            case 0:
                System.out.println("\nThank you for using the Policy Manager");
                System.exit(0);
                break;
            case 1:
                newPolicy();
                break;
            case 2:
                summaryOfPolicies();
                break;
            case 3:
                summaryOfSelectedMonth();
                break;
            case 4:
                findPolicy();
                break;
            default:
                System.out.println("Invalid Option, try again.");
                break;
        }
        menuRepeater();     //By using the menuRepeater as a recursive method we do not need a while 
    }
    
    public static void newPolicy()
    {
        Policy newPolicy = new Policy();   //creating the newPolicy object, part of the Policy class
        getRefNo(newPolicy); //always sending the object as a reference for the method to be able to call methods from that class
        getName(newPolicy);
        getNumberOfItems(newPolicy);
        getMaxValue(newPolicy);
        getExcess(newPolicy);
        getPaymentPreference(newPolicy);    
        if(newPolicy.getMaxValue() > 100000)  //marking the policy as rejected if the maxValue is greater than 1000 GBP
            newPolicy.setRejected(true);
        //We are using a switch in order to choose the line we need, then the method for choosing the column, 
        //based on the number of items and the most expensive gadget
        switch(newPolicy.getNumberOfItems())
        {
            case 1: 
                newPolicy.setSelectedPremium(priceTable[0][selectTableColumn(newPolicy)]);
                break;
            case 2:
            case 3:
                newPolicy.setSelectedPremium(priceTable[1][selectTableColumn(newPolicy)]);
                break;
            case 4:
            case 5:
                newPolicy.setSelectedPremium(priceTable[2][selectTableColumn(newPolicy)]);
                break;
            default:
                newPolicy.setRejected(true); //if the number of items is greater than 5, we set the policy as rejected
        }
        applyExcessDiscount(newPolicy);
        if(newPolicy.getAnnual())  //if the policy is an annual one, we apply the discount
            applyAnnualDiscount(newPolicy);
        newPolicy.displayPolicy();
        try
        {
            newPolicy.writePolicyToFile();
        } catch (IOException ex)
        {
            //souldn't be possible since we are checking it in the policy class, right after creating
        }
    }
    
    public static void getRefNo(Policy policy)
    {
        System.out.println("Please input the policy reference number:");
        String refNo = key.nextLine();
        refNo = refNo.trim();
        //sending the reference number over to the policy class to have it validated
        //if it is invalid, it will return false, as it is a boolean method
        while(!policy.setRefNo(refNo))
        {
            System.out.println("Incorrect reference number format, please try again.");
            refNo = key.nextLine();
        }
    }
      
    public static void getName(Policy policy)
    {
        System.out.println("Please input the client name:");
        String customerName = key.nextLine();
        customerName = customerName.trim();
        //doing the same as with the reference number
        while(!policy.setCustomerName(customerName))
        {
            System.out.println("Name is invalid, please try again.");
            customerName = key.nextLine();
        }
    }   
    
    public static void getNumberOfItems(Policy policy)
    {
        System.out.println("Please input the number of items:");
        int numberOfItems = key.nextInt();
        key.nextLine();
        while(numberOfItems < 1)
        {
            System.out.println("Please input a number greater than 1.");
            numberOfItems = key.nextInt();
            key.nextLine();
        }
        policy.setNumberOfItems(numberOfItems);
    }
    
    public static void getMaxValue(Policy policy)
    {
        System.out.println("Please input the value of the most expensive item:");
        int maxValue = key.nextInt();
        key.nextLine();
        while(maxValue < 1)
        {
            System.out.println("Please input a number greater than 1.");
            maxValue = key.nextInt();
            key.nextLine();
        }
        maxValue = maxValue * 100; //working in pence, as requested
        policy.setMaxValue(maxValue);
    }
    
    public static void getExcess(Policy policy)
    {
        System.out.println("Please input the excess for the claim (between 30 and 70):");
        int excess = key.nextInt();
        key.nextLine();
        //validating in the policy class
        while(!policy.setExcess(excess))
        {
            System.out.println("Please input a value that is a multiple of 10 and is between 30 and 70.");
            excess = key.nextInt();
            key.nextLine();
        }
    }
    
    public static void getPaymentPreference(Policy policy)
    {
        boolean annual;
        System.out.println("Please input the number for the appropriate preference:");
        System.out.println("1. Monthly payment");
        System.out.println("2. Annual Payment");
        int select = key.nextInt();
        key.nextLine();
        while(select < 1 || select > 2)
        {
            System.out.println("Please input a valid option:");
            select = key.nextInt();
            key.nextLine();
        }
        //if the user inputs 2, policy is set to annual
        if(select == 1)
            policy.setAnnual(false);
        else
            policy.setAnnual(true);
        
    }
    
    //Method that stores the prices in the Basic Monthly Premium Table in pence
    //Storing the prices in a two-dimensional array for easier access
    static void setPrices()
    {
        priceTable[0][0] = 599;
        priceTable[0][1] = 715;
        priceTable[0][2] = 830;
        priceTable[1][0] = 1099;
        priceTable[1][1] = 1335;
        priceTable[1][2] = 1555;
        priceTable[2][0] = 1599;
        priceTable[2][1] = 1960;
        priceTable[2][2] = 2282;
    }
    
    //The method returns the index of the column we need, based on the maximum value of a gadget
    static int selectTableColumn(Policy policy)
    {        
        if(policy.getMaxValue() <= 50000)
        {
            policy.setLimitPerGadget(500); //sending the limit per gadget to the policy class, for displaying purposes
            return 0;
        }
        else if(policy.getMaxValue() <= 80000)
        {
            policy.setLimitPerGadget(800);
            return 1;
        }
        else
        {
            policy.setLimitPerGadget(1000);
            return 2;
        }            
    }
    
    //Using a temporary variable to keep the main price as an integer. Everything else is the basic applying of a discount based on the excess
    static void applyExcessDiscount(Policy policy)
    {
        double selectedPremium = policy.getSelectedPremium();
        if(policy.getExcess() != 30)
        {
            double discount = 5 * ((policy.getExcess() / 10) - 3);
            double tempPremium = selectedPremium;
            tempPremium = tempPremium - (discount / 100 * tempPremium);
            selectedPremium = (int)tempPremium;
        }
        policy.setSelectedPremium(selectedPremium);
    }
    
    //basic arithmetic for applying the annual discount
    static void applyAnnualDiscount(Policy policy)
    {
        double tempPremium = policy.getSelectedPremium();
        tempPremium = tempPremium - 0.1 * tempPremium;
        policy.setSelectedPremium((int)tempPremium);
    }
    
    public static void summaryOfPolicies()
    {
        List<Policy> policies;
        policies = new ArrayList<>();
        System.out.println("Please enter the file you wish to use (1 or 2)");
        System.out.println("1. archive.txt");
        System.out.println("2. policy.txt");
        String selectedFile = "";
        boolean valid = false;
        while(!valid)
        {
            switch(key.nextInt())
            {
                case 1:
                    selectedFile = "archive.txt";
                    valid = true;
                    break;
                case 2:
                    selectedFile = "policy.txt";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        try 
        {
            FileReader fileReader = new FileReader(selectedFile);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) 
            {
                int wordCount = 1;
                Policy policy = new Policy();
                Scanner lineReader = new Scanner(line);
                while(lineReader.hasNext())
                {
                    String word = lineReader.next();
                    switch(wordCount)
                    {
                        case 1: 
                            policy.setDate(word);
                            break;
                        case 2: 
                            policy.setRefNo(word);
                            break;
                        case 3: 
                            policy.setNumberOfItems(Integer.valueOf(word));
                            break;
                        case 4:
                            policy.setMaxValue(Integer.valueOf(word));
                            if(policy.getMaxValue() <= 500)
                            {
                                policy.setLimitPerGadget(500); //sending the limit per gadget to the policy class, for displaying purposes
                            }
                            else if(policy.getMaxValue() <= 800)
                            {
                                policy.setLimitPerGadget(800);
                            }
                            else
                            {
                                policy.setLimitPerGadget(1000);
                            } 
                            break;
                        case 5:
                            policy.setExcess(Integer.valueOf(word));
                            break;
                        case 6:
                            policy.setSelectedPremium(Integer.valueOf(word));
                            break;
                        case 7:
                            switch(word)
                            {
                                case "M":
                                    policy.setAnnual(false);
                                    break;
                                case "A":
                                    policy.setAnnual(true);
                                    break;
                                case "R":
                                    policy.setRejected(true);
                                default:
                                    break;
                            }
                            break;
                        case 8:
                            policy.setCustomerName(word + " " + lineReader.next());
                            break;
                        default:
                            break;
                    }
                    wordCount++; 
                }
                policies.add(policy); 
            }
            fileReader.close();
        }catch (FileNotFoundException ex)
        {
            Logger.getLogger(PolicyManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(PolicyManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
        int numberOfPolicies = 0, allItemsCount = 0;
        double sumOfMonthlyPremiums = 0;
        int[] policiesPerMonth = new int[12];
        for(Policy policy : policies)
        {
            numberOfPolicies++;
            policiesPerMonth[policy.getMonth()]++;
            if(!policy.getRejected())
            {
                allItemsCount += policy.getNumberOfItems();
                if(policy.getAnnual())
                {   
                    sumOfMonthlyPremiums += policy.getSelectedPremium()/100/12;
                }
                else
                {
                    sumOfMonthlyPremiums += policy.getSelectedPremium()/100;
                }
            }   
        }
        System.out.println("Total Number of policies: "+numberOfPolicies);
        System.out.printf("Average number of items (Accepted policies): %.2f", (double)allItemsCount/numberOfPolicies);
        System.out.printf("\nAverage Monthly Premium: %.2f", (double)sumOfMonthlyPremiums/numberOfPolicies);
        System.out.println("\nNumber of Policies per Month (inc. non-accepted):");
        System.out.println("");
        System.out.println("Jan\tFeb\tMar\tApr\tMay\tJun\tJul\tAug\tSep\tOct\tNov\tDec\n");
        for(int i = 0; i <= 11; i++)
            System.out.print(policiesPerMonth[i]+"\t");
    }  
    
    public static void summaryOfSelectedMonth()
    {
        List<Policy> policies;
        policies = new ArrayList<>();
        System.out.println("Please enter the file you wish to use (1 or 2)");
        System.out.println("1. archive.txt");
        System.out.println("2. policy.txt");
        String selectedFile = "";
        boolean valid = false;
        while(!valid)
        {
            switch(key.nextInt())
            {
                case 1:
                    selectedFile = "archive.txt";
                    valid = true;
                    break;
                case 2:
                    selectedFile = "policy.txt";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        try 
        {
            FileReader fileReader = new FileReader(selectedFile);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) 
            {
                int wordCount = 1;
                Policy policy = new Policy();
                Scanner lineReader = new Scanner(line);
                while(lineReader.hasNext())
                {
                    String word = lineReader.next();
                    switch(wordCount)
                    {
                        case 1: 
                            policy.setDate(word);
                            break;
                        case 2: 
                            policy.setRefNo(word);
                            break;
                        case 3: 
                            policy.setNumberOfItems(Integer.valueOf(word));
                            break;
                        case 4:
                            policy.setMaxValue(Integer.valueOf(word));
                            if(policy.getMaxValue() <= 500)
                            {
                                policy.setLimitPerGadget(500); //sending the limit per gadget to the policy class, for displaying purposes
                            }
                            else if(policy.getMaxValue() <= 800)
                            {
                                policy.setLimitPerGadget(800);
                            }
                            else
                            {
                                policy.setLimitPerGadget(1000);
                            } 
                            break;
                        case 5:
                            policy.setExcess(Integer.valueOf(word));
                            break;
                        case 6:
                            policy.setSelectedPremium(Integer.valueOf(word));
                            break;
                        case 7:
                            switch(word)
                            {
                                case "M":
                                    policy.setAnnual(false);
                                    break;
                                case "A":
                                    policy.setAnnual(true);
                                    break;
                                case "R":
                                    policy.setRejected(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 8:
                            policy.setCustomerName(word + " " + lineReader.next());
                            break;
                        default:
                            break;
                    }
                    wordCount++; 
                }
                policies.add(policy); 
            }
            fileReader.close();
        }catch (FileNotFoundException ex)
        {
            Logger.getLogger(PolicyManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(PolicyManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
        System.out.println("Please input the number of the month(1-12, January-December): ");
        int month = key.nextInt();
        int numberOfPolicies = 0, allItemsCount = 0, acceptedPolicies = 0;
        double sumOfMonthlyPremiums = 0;
        for(Policy policy : policies)
        {
            if(policy.getMonth() == month - 1)
            {
                acceptedPolicies++;
                numberOfPolicies++;
                if(!policy.getRejected())
                {
                    allItemsCount += policy.getNumberOfItems();
                    if(policy.getAnnual())
                    {   
                        sumOfMonthlyPremiums += policy.getSelectedPremium()/100/12;
                    }
                    else
                    {
                        sumOfMonthlyPremiums += policy.getSelectedPremium()/100;
                    }
                }  
            }
        }
        if(numberOfPolicies != 0)
        {
            System.out.println("Total Number of policies: "+numberOfPolicies);
            System.out.printf("Average number of items (Accepted policies): %.2f", (double)allItemsCount/numberOfPolicies);
            System.out.printf("\nAverage Monthly Premium: %.2f", (double)sumOfMonthlyPremiums/numberOfPolicies);
        }
        else
            System.out.println("No policies for the selected month.");
        System.out.println("");
    }
    
    public static void findPolicy()
    {
        List<Policy> policies;
        policies = new ArrayList<>();
        System.out.println("Please enter the file you wish to use (1 or 2)");
        System.out.println("1. archive.txt");
        System.out.println("2. policy.txt");
        String selectedFile = "";
        boolean valid = false;
        while(!valid)
        {
            switch(key.nextInt())
            {
                case 1:
                    selectedFile = "archive.txt";
                    valid = true;
                    break;
                case 2:
                    selectedFile = "policy.txt";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        try 
        {
            FileReader fileReader = new FileReader(selectedFile);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) 
            {
                int wordCount = 1;
                Policy policy = new Policy();
                Scanner lineReader = new Scanner(line);
                while(lineReader.hasNext())
                {
                    String word = lineReader.next();
                    switch(wordCount)
                    {
                        case 1: 
                            policy.setDate(word);
                            break;
                        case 2: 
                            policy.setRefNo(word);
                            break;
                        case 3: 
                            policy.setNumberOfItems(Integer.valueOf(word));
                            break;
                        case 4:
                            policy.setMaxValue(Integer.valueOf(word));
                            if(policy.getMaxValue() <= 500)
                            {
                                policy.setLimitPerGadget(500); //sending the limit per gadget to the policy class, for displaying purposes
                            }
                            else if(policy.getMaxValue() <= 800)
                            {
                                policy.setLimitPerGadget(800);
                            }
                            else
                            {
                                policy.setLimitPerGadget(1000);
                            } 
                            break;
                        case 5:
                            policy.setExcess(Integer.valueOf(word));
                            break;
                        case 6:
                            policy.setSelectedPremium(Integer.valueOf(word));
                            break;
                        case 7:
                            switch(word)
                            {
                                case "M":
                                    policy.setAnnual(false);
                                    break;
                                case "A":
                                    policy.setAnnual(true);
                                    break;
                                case "R":
                                    policy.setRejected(true);
                                default:
                                    break;
                            }
                            break;
                        case 8:
                            policy.setCustomerName(word + " " + lineReader.next());
                            break;
                        default:
                            break;
                    }
                    wordCount++; 
                }
                policies.add(policy); 
            }
            fileReader.close();
        }catch (FileNotFoundException ex)
        {
            Logger.getLogger(PolicyManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(PolicyManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
        key.nextLine();
        System.out.println("Please enter the search query:");
        String query = key.nextLine().toLowerCase();
        for(Policy policy : policies)
        {
            if(policy.getCustomerName().toLowerCase().contains(query) || policy.getRefNo().toLowerCase().contains(query))
                policy.displayPolicy();
        }
    }
    
    //Method for displaying the main menu
    public static void displayMenu()
    {
        System.out.println("");
        System.out.println("Please pick one of the following options:");
        System.out.println("1.  Enter new Policy");
        System.out.println("2.  Display Summary of Policies");
        System.out.println("3.  Display Summary of Policies for Selected Month");
        System.out.println("4.  Find and display Policy");
        System.out.println("0.  Exit");
    }
}
