package finance;

import java.lang.Integer;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import finance.reports.model.PieChartModel;
import finance.reports.model.MonthlyBudgetModel;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class MonthlyBudget
{

    public MonthlyBudget()
    {
    }

    public static void main(String args[]) throws
	IOException, SQLException, ParseException
    {
	//String downloadPath= System.getenv("FINANCE_DOWNLOADS_PATH");
	String basePath = System.getenv("FINANCE_BASE_PATH");
	
	if (args.length != 2)
	    {
		System.out.println("Useage: finance.MonthlyBudget <basePath> <month>");
		System.out.println("Where <month> is 1 based (June is month 6)");
		System.exit(-1);
	    }

	BufferedWriter errorFile = new BufferedWriter(new FileWriter("errors.csv"));

	Finance finance = new Finance();

	if (basePath == null) { 
	    finance.setBasePath(args[0]);
	    basePath = args[0];
	}

	finance.setBasePath(basePath);

	Logger logger = finance.getLogger();
	System.out.println("Connecting to Database");
	finance.connect();

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
	// Get the month in question from the number sent via command line argument

	PieChartModel actualSpending = new PieChartModel(finance.getConnection(), "." );

	Calendar firstOfMonth = Calendar.getInstance();
	int monthNumber = Integer.parseInt(args[1]);
	System.out.println("Getting Budget Analysis for month: " + monthNumber);
	firstOfMonth.set(Calendar.MONTH, monthNumber+1);
	firstOfMonth.set(Calendar.DAY_OF_MONTH, 1);
	Calendar endOfMonth = Calendar.getInstance();
	endOfMonth.set(Calendar.MONTH, monthNumber);
	endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));

        System.out.println("The first of the month appears to be: " + simpleDateFormat.format(firstOfMonth.getTime()) + " and end of month is: " +
			   simpleDateFormat.format(endOfMonth.getTime()));
	/* load the PieChart (Actual spending categorized) */		
	actualSpending.loadPieChartEntriesFromDatabase(simpleDateFormat.format(firstOfMonth.getTime()),
						       simpleDateFormat.format(endOfMonth.getTime()));


	// /* for the next functionality documentation see .../finance/documentation/ideasForImprovement.txt */

	// /* The algorithm for this:
	//    read both pieChart.csv and DollarsPerMonth.csv into memory.  (If they're not already in memory).
	//    The memory structure of DollarsPerMonth.csv will have a slot for remembering that it was used for comparison in addition
	//    to the category and amount.
	//    for each item in: the pieChart map
	//    look for a similarly named item (ALL CAPS CATEGORIES) in DollarsPerMonth memory structure (map with usedForComparison addition)
	//    if one if found:
	//    subtract the amount of pieChart from DollarsPerMonth (DollarsPerMonth.category.value - pieChart.category.value)
	//    to get a POSITIVE value of what is left to SPEND in that category.
	//    (If the value is NEGATIVE the budget was overspent and not obeyed)
	//    Mark the DollarsPerMonth category as USED.
	//    For the report:
	//    print out all UNUSED categories in DollarsPerMonth as nothing spent in this category
	//    print out all UNRECOGNIZED categories in pieChart (actual spending) as errors.  (Everything should be recognized.)	
	// */

	// /* the basePath sent is the current directory.  Hope that's ok.  There'll be some clean-up needed eventually. */

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
    }


    }
