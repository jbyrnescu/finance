package finance.reports.model;

import java.util.Date;

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
   
}
