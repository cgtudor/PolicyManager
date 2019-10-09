/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policymanager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author v8002382
 */
public class Policy
{
    private String refNo, customerName;
    private int numberOfItems, maxValue, excess;
    private int limitPerGadget;
    private double selectedPremium;
    private boolean annual, rejected;
    private FileWriter fileWriter; 
    private String date;
    public Policy()
    {
        refNo = "";
        customerName = "";
        date = getDate();
        numberOfItems = 0;
        maxValue = 0;
        excess = 0;
        selectedPremium = 0;
        limitPerGadget = 0;
        selectedPremium = 0;
        rejected = false;
        annual = false;
        try
        {
            fileWriter = new FileWriter("policy.txt", true);
        }
        catch(IOException e)
        {
            System.out.println("Archive file could not be found. Please place it inside the project directory and try again.");  
            System.exit(0);
        }
    }
    
    public void setDate(String newDate)
    {
        date = newDate;
    }
    
    public String retrieveDate()
    {
        return date;
    }
    
    public String getRefNo()
    {
        return refNo;
    }
    
    //returns false if the validation fails, so that the driver class can ask for input again
    public boolean setRefNo(String ref)
    {
        if(!validateRefNo(ref))
            return false;
        refNo = ref;
        return true;
    }
    
    public static boolean validateRefNo(String refNumber)
    {
        if(refNumber.length() != 6)
            return false;
        //checking each character to see if it matches with the format requested
        if(!(Character.isLetter(refNumber.charAt(0)) && Character.isLetter(refNumber.charAt(1)) && Character.isDigit(refNumber.charAt(2)) && Character.isDigit(refNumber.charAt(3)) && Character.isDigit(refNumber.charAt(4)) && Character.isLetter(refNumber.charAt(5))))
            return false;
        return true;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }
    
    public boolean setCustomerName(String name)
    {
        if(!validateName(name))
            return false;
        customerName = name;
        return true;
    }
    
    public boolean validateName(String name)
    {
        return name.length() < 1 || name.length() > 20 ? false : true;  //alternatife if statement to check the name is between 1 and 20 characters
    }
    
    public int getNumberOfItems()
    {
        return numberOfItems;
    }
    
    public void setNumberOfItems(int no)
    {
        numberOfItems = no;
    }
    
    public int getMaxValue()
    {
        return maxValue;
    }
    
    public void setMaxValue(int value)
    {
        maxValue = value;
    }
    
    public int getExcess()
    {
        return excess;
    }
    
    public boolean setExcess(int exc)
    {
        if(!validateExcess(exc))
            return false;
        excess = exc;
        return true;
    }
    
    public boolean validateExcess(int value)
    {
        return value > 70 || value < 30 || value % 10 != 0 ? false : true; //alternative int to check that excess is between 30 and 70 and a multiple of 10
    }
    
    public boolean getAnnual()
    {
        return annual;
    }
    
    public void setAnnual(boolean ann)
    {
        annual = ann;
    }
    
    public int getLimitPerGadget()
    {
        return limitPerGadget;
    }
    
    public void setLimitPerGadget(int limit)
    {
        limitPerGadget = limit;
    }
    
    public double getSelectedPremium()
    {
        return selectedPremium;
    }
    
    public void setSelectedPremium(double premium)
    {
        selectedPremium = premium;
    }
    
    public boolean getRejected()
    {
        return rejected;
    }

    public void setRejected(boolean rej)
    {
        rejected = rej;
    }
    
    public int getMonth()
    {
        String tempDate = date;
        String[] words = tempDate.split("-");
        switch(words[1])
        {
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
                return 11;
        }
        return 0;
    }
    
    //method to return numbers as strings if the number of items in under 5. Else it returns them as string forms of normal itegers -- for display purposes
    private String numberInString()
    {
        switch(numberOfItems)
        {
            case 1:
                return "One";
            case 2:
                return "Two";
            case 3:
                return "Three";
            case 4:
                return "Four";
            case 5:
                return "Five";
            default:
                return String.valueOf(numberOfItems);
        }
    }
    
    //using various printf statements in order to display the policy properly, no matter the information given
    public void displayPolicy()
    {
        System.out.println("");
        System.out.println("");
        System.out.println("+==========================================+");
        System.out.println("|                                          |");
        System.out.printf("|%8s: %-32s|", "Client", customerName);
        System.out.println("");
        System.out.println("|                                          |");
        System.out.printf("|%8s: %-20s%3s: %-7s|", "Date", date, "Ref", refNo);
        System.out.println("");
        if(annual)
            System.out.printf("|%8s: %-18s%5s: %-7s|", "Terms", "Annual", "Items", numberInString());
        else
            System.out.printf("|%8s: %-18s%5s: %-7s|", "Terms", "Monthly", "Items", numberInString());
        System.out.println("");
        System.out.printf("|%8s: £%.2f%26s|", "Excess", (double)excess, "");
        System.out.println("");
        System.out.println("|                                          |");
        if(annual)
            System.out.printf("|%8s%25s%9s|", "Annual", "Limit per", "");
        else
            System.out.printf("|%8s%25s%9s|", "Monthly", "Limit per", "");
        System.out.println("");
        if(rejected)
            System.out.printf("|%8s: %-17s%6s:%-8s|", "Premium", "Rejected", "Gadget", "Rejected");
        else
        {
            if(!annual)
                System.out.printf("|%8s: £%-17.2f%6s: %-6d|", "Premium", (float)selectedPremium / 100, "Gadget", limitPerGadget);
            else
                System.out.printf("|%8s: £%-17.2f%6s: %-6d|", "Premium", (float)selectedPremium * 12 / 100, "Gadget", limitPerGadget);
        }
        System.out.println("");
        System.out.println("|                                          |");
        System.out.println("+==========================================+");       
    }
    
    //writing to file using tabs on order to keep the format
    public void writePolicyToFile() throws IOException 
    {
        PrintWriter printWriter = new PrintWriter(fileWriter);
        if(rejected)
            printWriter.print(getDate()+"\t"+refNo+"\t"+numberOfItems+"\t"+maxValue/100+"\t"+excess+"\t"+"-1"+"\t"+"R"+"\t"+customerName+"\n");
        else if(annual)
            printWriter.print(getDate()+"\t"+refNo+"\t"+numberOfItems+"\t"+maxValue/100+"\t"+excess+"\t"+(int)selectedPremium*12+"\t"+"A"+"\t"+customerName+"\n");
        else 
            printWriter.print(getDate()+"\t"+refNo+"\t"+numberOfItems+"\t"+maxValue/100+"\t"+excess+"\t"+(int)selectedPremium+"\t"+"M"+"\t"+customerName+"\n");
        printWriter.close();
        fileWriter.close();
    }
    
    //Method that returns the date to a string
    public static String getDate()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(cal.getTime());
    }
}
