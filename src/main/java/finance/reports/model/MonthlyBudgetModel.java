package finance.reports.model;

import java.util.HashMap;
import java.util.*;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.SimpleDateFormat;

/* really the only functionality used from BudgetModel, is the data structure (extended from PieChart) and
	loadBudgetFromFile. */
public class MonthlyBudgetModel extends BudgetModel
{
	/* The following construct is used to keep track of which categories are
		used during a comparison with Actual Spending 
		The way it works is as the algorithm goes through Actual
		Spending categories and finds the corresponding category in the Budget
		map.  It marks it as used if it finds it.  At the end of the comparison
		it will print out which ones aren't used and hence which categories weren't spent
		on during the comparison period.  */
	protected HashMap<String, String> used = new HashMap<>();

	public MonthlyBudgetModel(String basePath)
	{
		super(basePath);
	}

    public HashMap<String, String> getUsed()
    {
        return(used);
    }
		
	public BudgetItem findCategory(String category) 
	{
		Double amount = allowedAmounts.get(category);
		if (amount != null)
		{
		    System.out.println("Found Category: " + category + " marking it as used");
		    BudgetItem item = new BudgetItem(category, allowedAmounts.get(category), "used");
		    used.put(category, "used");
		    // String autoFinanceEntry = used.get("AUTO FINANCE");
		    // if (autoFinanceEntry == null)
		    // 	System.out.println("test AUTO FINANCE entry missing");
		    // else
		    // 	System.out.println("test AUTO FINANCE entry exists, at this point.");
		    return(item);
		}
		else return null;
	}

    public boolean wasUsed(String category)
    {
        System.out.println("Looking for category that was \"used\" or actually spent in: " + category);

        System.out.println("AUTO FINANCE category found?: " + used.get("AUTO FINANCE"));

        String wasUsedStringValue = used.get(category);
        if (wasUsedStringValue == null) 
        {
            return false;
        } else
        {
            if (wasUsedStringValue.equals("used"))
            {
                System.out.println("used value was \"used\"");
                return(true);
            } else
            {
                System.out.println("used value was " + wasUsedStringValue);
                return(false);
            }
        }
    }

    public void printUsedTable()
    {
	Set<Map.Entry<String, String>> entrySet = used.entrySet();
	Map.Entry<String, String> entry = null;
	Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
	System.out.println("Printing used table.");
	
        if (iterator.hasNext())
        {
            entry = iterator.next();
            for (;iterator.hasNext(); entry = iterator.next())
                {
                String category = entry.getKey();
                String usedValue = entry.getValue();
                System.out.println("category: " + category + " value: " + usedValue);
                }
        }
        else
        {
            System.out.println("No categories used in transactions");
        } 
    }
}
