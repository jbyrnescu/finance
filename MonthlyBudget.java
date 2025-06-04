package finance;

public class MonthlyBudget
{
    Finance finance = new Finance
}



		/* for the next functionality documentation see .../finance/documentation/ideasForImprovement.txt */

		/* The algorithm for this:
		   read both pieChart.csv and DollarsPerMonth.csv into memory.  (If they're not already in memory).
		   The memory structure of DollarsPerMonth.csv will have a slot for remembering that it was used for comparison in addition
		   to the category and amount.
		   for each item in: the pieChart map
		   look for a similarly named item (ALL CAPS CATEGORIES) in DollarsPerMonth memory structure (map with usedForComparison addition)
		   if one if found:
		   subtract the amount of pieChart from DollarsPerMonth (DollarsPerMonth.category.value - pieChart.category.value)
		   to get a POSITIVE value of what is left to SPEND in that category.
		   (If the value is NEGATIVE the budget was overspent and not obeyed)
		   Mark the DollarsPerMonth category as USED.
		   For the report:
		   print out all UNUSED categories in DollarsPerMonth as nothing spent in this category
		   print out all UNRECOGNIZED categories in pieChart (actual spending) as errors.  (Everything should be recognized.)	
		*/

		/* the basePath sent is the current directory.  Hope that's ok.  There'll be some clean-up needed eventually. */
		PieChartModel actualSpending = new PieChartModel(finance.connection, "." );
		Calendar firstOfMonth = Calendar.getInstance();
		firstOfMonth.set(Calendar.DAY_OF_MONTH, 1); 
        
        System.out.println("The first of the month appears to be: " + simpleDateFormat.format(firstOfMonth.getTime()) + " and today is: " +
            simpleDateFormat.format(Calendar.getInstance().getTime()));
		/* load the PieChart (Actual spending categorized) */		
		actualSpending.loadPieChartEntriesFromDatabase(simpleDateFormat.format(firstOfMonth.getTime()),
            simpleDateFormat.format(Calendar.getInstance().getTime()));
		
		/* now read in the Monthly Budget numbers */
		String monthlyBudgetFilename = "DollarsPerMonth.csv";
        System.out.println("project base path: " + basePath);
	    MonthlyBudgetModel mbm = new MonthlyBudgetModel(basePath);
        System.out.println("Loading monthly budget from file: " + monthlyBudgetFilename);
        mbm.loadBudgetFromFile(monthlyBudgetFilename);
        
        /* we now have both a Monthly budget model and the actual spending. */
        finance.printMonthlyStatus(mbm, actualSpending);    
        
		errorFile.close();

		
		finance.closeAll();
