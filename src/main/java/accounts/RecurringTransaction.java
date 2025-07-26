package accounts;

import java.util.Calendar;
import java.util.Date;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

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

    public String getDescription()
    {
	return(description);
    }

    public void setDescription(String description)
    {
	this.description = description;
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

    public static final String RECURRING_TRANSACTION_94DAY_QUERY = "select * from BigTXView where transactionDate > ?;";

    public static final String RECURRING_TRANSACTION_94DAY_QUERY4 = "select * from BigTXView where transactionDate > ? and transactionDate < ? and description = ? and amount > ? and amount < ? order by transactiondate desc;";

    public static void getRecurringTransactionsFromToday() 
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -94);
        Date date = calendar.getTime();

        // Use the date to query the database for transactions
        // that are similar to the current transaction.
        // This is where you would implement the logic to retrieve
        // and process transactions from the database.
        Connection connection = Finance.getConnectionStatic();
        try (PreparedStatement preparedStatement = connection.prepareStatement(RECURRING_TRANSACTION_94DAY_QUERY)) 
        {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Look for recurring transactions for this transaction
                PreparedStatement findRecurringTransactionsStatement = connection.prepareStatement(RECURRING_TRANSACTION_94DAY_QUERY4);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new java.util.Date(resultSet.getDate("transactionDate").getTime()));
                cal.add(Calendar.DAY_OF_MONTH, -94);
                Date lowerDate = cal.getTime();
                Date upperDate = new java.util.Date(resultSet.getDate("transactionDate").getTime());
                findRecurringTransactionsStatement.setDate(1, new java.sql.Date(lowerDate.getTime()));
                findRecurringTransactionsStatement.setDate(2, new java.sql.Date(upperDate.getTime()));
                findRecurringTransactionsStatement.setString(3, resultSet.getString("description"));
                findRecurringTransactionsStatement.setDouble(4, resultSet.getDouble("amount") - 50.0);
                findRecurringTransactionsStatement.setDouble(5, resultSet.getDouble("amount") + 50.0);
                ResultSet recurringResultSet = findRecurringTransactionsStatement.executeQuery();

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
                // Quarterly transactions will return 1 result
                if (returnedTransactions.size() == 1)
                {
                    System.out.println("Found quarterly transaction: " + returnedTransactions.get(0).getDescription());
                    cal = Calendar.getInstance();
                    cal.setTime(returnedTransactions.get(0).getLastSeenDate());
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    returnedTransactions.get(0).setRecurrenceType("Quarterly");
                    returnedTransactions.get(0).setAverageDayOfMonthOfTransaction(day);
                    returnedTransactions.get(0).setAmountType("Unknown");
                    returnedTransactions.get(0).loadIntoDatabase(connection);
                }
                // Monthly transactions will return 3 results
                if (returnedTransactions.size() == 3)
                {
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

                    setDescription(foundDescription);
                    setAmount(foundAmount);
                    setAmountType(amountType);
                    setRecurrenceType("Monthly");
                    setAverageDayOfMonthOfTransaction((int)(days/3.0));
                    setLastSeenDate(returnedTransactions.get(0).getLastSeenDate());
                    setAmount(sum/3.0);
                    this.loadIntoDatabase(connection);
                }

                // Enter into the database
                
                // System.out.println("Found transaction(s): " + resultSet.getString("description") + " on " + resultSet.getDate("TransactionDate").toString() + " with amount: " + resultSet.getDouble("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
	public int loadIntoDatabase(Connection connection) throws SQLException {
        connection = Finance.getConnectionStatic();
		if (super.loadIntoDatabase(connection) == Transaction.TRANSACTION_EXISTS)
			return(NOTHING_LOADED);
		PreparedStatement statement = connection.prepareStatement("insert or replace into RecurringTransactions ("
				+ "description, recurrence_type, amount, amount_type, average_day_of_purchase, "
				+ "latest_recurrence) "
				+ "values ( ?, ?, ?, ?, ?, ? );");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		statement.setString(8, simpleDateFormat.format(transactionDate));
		
		
		statement.setString(1, getDescription());
		statement.setString(2, getRecurrenceType());
		statement.setDouble(3, getAmount());
        statement.setString(4, getAmountType());
		statement.setInt(5, getAverageDayOfMonthOfTransaction() );
		statement.setDate(6, new java.sql.Date(getLastSeenDate().getTime()));
		
		statement.executeUpdate();
		return(TRANSACTION_LOADED);
	}


}
