package accounts;

import java.util.Calendar;
import java.util.Date;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

// Import Finance class (adjust the package if needed)
import finance.Finance;

public class RecurringTransaction extends Transaction
{
    String description;
    String recurrenceType;
    double amount;
    // C - for constant, D - for dynamic
    String amountType;
    int averageDayOfMonthOfTransaction;
    Date lastSeenDate;

    @Override
    public void convertToAbstractTransaction() {
        // Return a new instance or this, depending on your design
        // Here, returning 'this' as a placeholder
    }

    @Override
    public void populateTransactionFromString(String data) {
        // Implement parsing logic here
        // Example placeholder implementation:
        // Parse the string and set fields accordingly
        // You should replace this with your actual parsing logic
        if (data != null && !data.isEmpty()) {
            this.description = data;
        }
    }
    
    public String getRecurrenceType() {
        return recurrenceType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setRecurrenceType(String recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public double getAmount()
    {
	return amount;
    }

    public void setAmount(double amount)
    {
	this.amount = amount;
    }

    public int getAverageDayOfMonthOfTransaction()
    {
	return averageDayOfMonthOfTransaction;
    }

    public void setAverageDayOfMonthOfTransaction(int day)
    {
	this.averageDayOfMonthOfTransaction = day;
    }

    public Date getLastSeenDate()
    {
	return lastSeenDate;
    }

    public void setLastSeenDate(Date lastSeenDate)
    {
	this.lastSeenDate = lastSeenDate;
    }

    public boolean transactionsAreSimilar(Transaction t1, Transaction t2)
    {

        Date lowerLimitDate = t1.getTransactionDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lowerLimitDate);
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        lowerLimitDate = calendar.getTime();
        
        Date upperLimitDate = t1.getTransactionDate();
        calendar.setTime(upperLimitDate);
        calendar.add(Calendar.DAY_OF_MONTH, 4);
        upperLimitDate = calendar.getTime();

        double lowerLimitAmount = t1.getAmount() - 50.0;
        double upperLimitAmount = t1.getAmount() + 50.0;

        boolean withinDate = t2.getTransactionDate().after(lowerLimitDate) 
            && t2.getTransactionDate().before(upperLimitDate);
        boolean withinAmount = t2.getAmount() > lowerLimitAmount 
            && t2.getAmount() < upperLimitAmount;
        boolean sameDescription = t1.getDescription().equals(t2.getDescription());

        return(withinDate && withinAmount && sameDescription);

    }

    public static final String RECURRING_TRANSACTION_94DAY_QUERY = "select * from BigTXView where transactionDate > ? order by transactiondate asc;";

    public static final String RECURRING_TRANSACTION_94DAY_QUERY4 = "select * from BigTXView where transactionDate > ? and transactionDate < ? and description = ? and amount > ? and amount < ? order by transactiondate asc;";

    public static void getRecurringTransactionsFromToday(Connection connection) throws ParseException
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -94);
        Date date = calendar.getTime();

        ArrayList<RecurringTransaction> recurringTransactionsToAdd = new ArrayList<>();

        // Use the date to query the database for transactions
        // that are similar to the current transaction.
        // This is where you would implement the logic to retrieve
        // and process transactions from the database.
        try (PreparedStatement preparedStatement = connection.prepareStatement(RECURRING_TRANSACTION_94DAY_QUERY)) 
        {
            preparedStatement.setString(1, simpleDateFormat.format(date));
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) 
            {
                // Look for recurring transactions for this transaction
                PreparedStatement findRecurringTransactionsStatement = connection.prepareStatement(RECURRING_TRANSACTION_94DAY_QUERY4);
                Calendar cal = Calendar.getInstance();
                String dateString = resultSet.getString("transactionDate");
                cal.setTime(simpleDateFormat.parse(dateString));
                cal.add(Calendar.DAY_OF_MONTH, -94);

                String lowerDate = simpleDateFormat.format(cal.getTime());
                String upperDate = resultSet.getString("transactionDate");
                findRecurringTransactionsStatement.setString(1, lowerDate);
                findRecurringTransactionsStatement.setString(2, upperDate);
                findRecurringTransactionsStatement.setString(3, resultSet.getString("description"));
                double lowerAmount = resultSet.getDouble("amount") - 50.0;
                findRecurringTransactionsStatement.setDouble(4, lowerAmount);
                double upperAmount = resultSet.getDouble("amount") + 50.0;
                findRecurringTransactionsStatement.setDouble(5, upperAmount);

                System.out.println("Executing query:");
                System.out.println("select * from BigTXView where transactionDate > " + lowerDate + " and transactionDate < " + upperDate + " and description = " + resultSet.getString("description") 
                    + " and amount > " + lowerAmount + " and amount < " + upperAmount + " order by transactiondate asc;");

                ResultSet recurringResultSet = findRecurringTransactionsStatement.executeQuery();
                System.out.println("Issued sql: " + findRecurringTransactionsStatement.toString());

                ArrayList<RecurringTransaction> returnedTransactions = new ArrayList<>();
                String foundDescription = null;
                double foundAmount = 0.0;

                while(recurringResultSet.next())
                {
                    RecurringTransaction recurringTransaction = new RecurringTransaction();
                    // Process the recurring transaction
                    foundDescription = recurringResultSet.getString("description");
                    foundAmount = recurringResultSet.getDouble("amount");
                    recurringTransaction.setDescription(recurringResultSet.getString("description"));
                    recurringTransaction.setAmount(recurringResultSet.getDouble("amount"));
                    recurringTransaction.setLastSeenDate(new java.util.Date(recurringResultSet.getDate("transactionDate").getTime()));

                    // Add to a list or process as needed
                    returnedTransactions.add(recurringTransaction);

                }
                // Quarterly transactions will return 1 result including the original transaction
                if (returnedTransactions.size() == 2)
                {
                    // Check to make sure the transactions are 90 +/- 4 days apart
                    Date firstTransactionDate = returnedTransactions.get(0).getLastSeenDate();
                    Date secondTransactionDate = returnedTransactions.get(1).getLastSeenDate();
                    long diffInMillies = Math.abs(secondTransactionDate.getTime() - firstTransactionDate.getTime());
                    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
                    if (diffInDays < 86 || diffInDays > 94) {
                        System.out.println("Skipping transaction: " + returnedTransactions.get(0).getDescription() + " as it is not a quarterly transaction.");
                        continue; // Skip this transaction
                    }
                    // If we get here, we have a quarterly transaction
                    // Set the recurrence type and average day of month of transaction
                    // and amount type

                    
                    System.out.println("Found quarterly transaction: " + returnedTransactions.get(0).getDescription());
                    cal = Calendar.getInstance();
                    cal.setTime(returnedTransactions.get(0).getLastSeenDate());
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    returnedTransactions.get(0).setRecurrenceType("Quarterly");
                    returnedTransactions.get(0).setAverageDayOfMonthOfTransaction(day);
                    returnedTransactions.get(0).setAmountType("Unknown");
                    returnedTransactions.get(0).loadIntoDatabase(connection);
                    recurringTransactionsToAdd.add(returnedTransactions.get(0));
                }
                // Monthly transactions will return 3 results
                if (returnedTransactions.size() == 3)
                {

                    // Check to make sure the transactions are 28 +/- 4 days apart
                    for (int i = 0; i < returnedTransactions.size()-1; i++)
                    {
                        Date firstTransactionDate = returnedTransactions.get(i).getLastSeenDate();
                        Date secondTransactionDate = returnedTransactions.get(i+1).getLastSeenDate();
                        long diffInMillies = Math.abs(secondTransactionDate.getTime() - firstTransactionDate.getTime());
                        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
                        if (diffInDays < 24 || diffInDays > 32) {
                            System.out.println("Skipping transaction: " + returnedTransactions.get(0).getDescription() + " as it is not a monthly transaction.");
                            continue; // Skip this transaction
                        }

                    }

                    System.out.println("Found monthly transaction: " + returnedTransactions.get(0).getDescription());
                    // find the average day of month of transaction
                    double days = 0.0;
                    double sum = 0.0, max = 0.0;
                    for (int i = 0; i < 3; i++)
                    {
                        RecurringTransaction rt = returnedTransactions.get(i);
                        // Get the day of the month from the transaction date
                        cal = Calendar.getInstance();
                        cal.setTime(rt.getLastSeenDate());
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        days = days + day;

                        // calculate average amount
                        sum = sum + rt.getAmount();
                        max = Math.max(sum, rt.getAmount()); 

                    }
                    String amountType = null;
                    if ((int)(sum/3.0) == (int)max)
                    {
                        amountType = "Constant";
                    } else amountType = "Dynamic";

                    RecurringTransaction rt = new RecurringTransaction();

                    rt.setDescription(foundDescription);
                    rt.setAmount(foundAmount);
                    rt.setAmountType(amountType);
                    rt.setRecurrenceType("Monthly");
                    rt.setAverageDayOfMonthOfTransaction((int)(days/3.0));
                    rt.setLastSeenDate(returnedTransactions.get(0).getLastSeenDate());
                    rt.setAmount(sum/3.0);
                    rt.setTransactionDate(returnedTransactions.get(0).getLastSeenDate());
                    // Load into the database
                    System.out.println("Found monthly transaction: " + rt.getDescription() + " with amount: " + rt.getAmount() + " and average day of month: " + rt.getAverageDayOfMonthOfTransaction());
                    // Load into the ArrayList for later load into database
                    recurringTransactionsToAdd.add(rt);
                }
                findRecurringTransactionsStatement.close();
            }

            preparedStatement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }

        for (RecurringTransaction rt : recurringTransactionsToAdd) 
        {
            System.out.println("Loading recurring transaction: " + rt.getDescription() + " with amount: " + rt.getAmount() + " and average day of month: " + rt.getAverageDayOfMonthOfTransaction());
            rt.loadIntoDatabase(connection);
        }
        
        try 
        {
            // Write to a file, because we're retarded.
            PrintWriter printWriter = new PrintWriter("recurring_transactions.txt");
            for (RecurringTransaction rt : recurringTransactionsToAdd) 
            {
            
                printWriter.println(rt.getDescription() + "," + 
                    rt.getRecurrenceType() + "," + 
                    rt.getAmount() + "," + 
                    rt.getAmountType() + "," + 
                    rt.getAverageDayOfMonthOfTransaction() + "," + 
                    rt.getLastSeenDate());
            }
            System.out.println("Recurring transactions written to file.");
            printWriter.close();
                    
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
  
    }


    @Override
	public int loadIntoDatabase(Connection connection) {
		try {
        // Check if the transaction already exists in the database}
        if (super.loadIntoDatabase(connection) == Transaction.TRANSACTION_EXISTS)
			return(NOTHING_LOADED);
        } catch (SQLException e) {
            System.out.println("Error checking for existing transaction: " + e.getMessage());
            return(NOTHING_LOADED);
        }
        try {
            PreparedStatement statement = connection.prepareStatement("insert or replace into RecurringTransactions ("
                    + "description, recurrence_type, amount, amount_type, average_day_of_purchase, "
                    + "latest_recurrence) "
                    + "values ( ?, ?, ?, ?, ?, ? );");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            
            statement.setString(1, getDescription());
            statement.setString(2, getRecurrenceType());
            statement.setDouble(3, getAmount());
            statement.setString(4, getAmountType());
            statement.setInt(5, getAverageDayOfMonthOfTransaction() );
            statement.setString(6, simpleDateFormat.format(transactionDate));
            
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error inserting into RecurringTransactions table: " + e.getMessage());
        } finally {

        }
		return(TRANSACTION_LOADED);
	}


}
