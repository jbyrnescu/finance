package finance.reports.model;

public class BudgetItem {
	
	double amount = 0.0;
	String category = "";
    /* used is to store an extra something about the budget Item.
        I'm specifically using it to store whether the item was found
        in a search.  If it wasn't found, in my case, that signifies
        that no money was spent in that particular category for the
        specified period. */
	String used;
	
	public BudgetItem() {
	
	}

	public BudgetItem(String category, double amount, String used)
	{
		setCategory(category);
		setAmount(amount);
		setUsed(used);
	}
	
	public void populateItemFromString(String line) {
        System.out.println("loading budget item from line: " + line);
		String[] columns = line.split(",");
		category = columns[0];
		amount = Double.parseDouble(columns[1]);
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public void setUsed(String used)
	{
		this.used = used;
	}

    public String getCategory()
    {
        return(category);
    }

    public double getAmount()
    {
        return(amount);
    }

    public String getUsed()
    {
        return(used);
    }
}
