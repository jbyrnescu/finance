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
		
	public BudgetItem findCategory(String category) 
	{
		Double amount = allowedAmounts.get(category);
		if (amount != null)
		{
			BudgetItem item = new BudgetItem(category, allowedAmounts.get(category), "used");
			return(item);
		}
		else return null;
	}

}
