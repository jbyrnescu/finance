package finance.reports.model;

public class BudgetItem {
	
	double amount = 0.0;
	String category = "";
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

}
