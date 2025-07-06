package finance.reports.model;

import java.util.Calendar;
import java.util.Date;

import accounts.Transaction;

public class RecurringTransaction
{
    String description;
    String recurrenceType;
    double amount;
    Date averageDayOfMonthOfTransaction;
    Date firstFoundDate;
    Date lastSeenDate;
    
    public String getRecurrenceType() {
        return recurrenceType;
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

    public Date getAverageDayOfMonthOfTransaction()
    {
	return averageDayOfMonthOfTransaction;
    }

    public void setAverageDayOfMonthOfTransaction(Date date)
    {
	this.averageDayOfMonthOfTransaction = date;
    }
 
    public Date getFirstFoundDate()
    {
	return firstFoundDate;
    }

    public void setFirstFoundDate(Date firstFoundDate)
    {
	this.firstFoundDate = firstFoundDate;
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
   
}
