package finance.reports.model;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import finance.Logger;

public class PieChartModel {
	
	ArrayList<PieChartEntry> chartEntries = new ArrayList<PieChartEntry>();
	Connection connection;
	String basePath;
	
	public PieChartModel(Connection connection, String basePath) {
		this.connection = connection;
		this.basePath = basePath;
	}

	public PieChartModel(String basePath)
	{
		this.basePath = basePath;
	}	
	
	public void writePieChartEntries() throws IOException {
		PrintWriter file = new PrintWriter(new File(basePath+"/pieChart.csv"));
		for(int i = 0; i < chartEntries.size(); i++) {
			file.println(chartEntries.get(i).getCategory() + "," + chartEntries.get(i).getAmount());
		}
		file.close();
	}
	
	public int loadPieChartEntriesFromDatabase(String beginDate, String endDate) {
		String and1 ="", and2 = "";
		String endQuote = "\"";
		if (beginDate == null) {
			beginDate = "";
		} else and1 = " and transactionDate >= \"" +beginDate+ endQuote;
		if (endDate == null)
		{ 
			endDate = ""; 
		} else and2 = " and transactionDate <= \"" + endDate + " 23:59:59" + endQuote ;
		
		String query = "select BudgetCat,sum(amount) as amount from BigTXView "
				+ " where "
				+ " XclFrmCshFlw is null "
				+ " and "
				+ " budgetCat not like \"%ayment%\" "
				+ " and budgetCat not like \"%Income%\" "
                + " and budgetCat not like \"transfer\" "
				+ and1
				+ and2
				+ " group by BudgetCat "
				+ " order by sum(amount) asc;";
		
		Logger.out.println("query for pie chart: " + query);
		int numberOfEntries = 0;

		try {
			Statement s = connection.createStatement();
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				PieChartEntry pce = new PieChartEntry();
				pce.loadFromResultSet(rs);
				chartEntries.add(pce);
				numberOfEntries++;
			}
		} catch (SQLException e) {
			Logger.out.println("Error loading pie chart entries: " + e.getMessage());
			return -1;
		} 
		return numberOfEntries;
				
	}
	
	@SuppressWarnings("unchecked")
	public void drawPieChart() {
		if (chartEntries.size() < 1) 
			{
				Logger.out.println("No entries to display");
				return;
			}
		@SuppressWarnings("rawtypes")
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < chartEntries.size(); i++) {
			double amount = Math.abs(chartEntries.get(i).getAmount());
			dataset.insertValue(i, chartEntries.get(i).getCategory() + " \n" + amount,amount);
		}
		JFreeChart chart = ChartFactory.createPieChart("Spending Category Amounts", 
				dataset, true, false, false);
		JPanel jPanel = new ChartPanel(chart);
		jPanel.setSize(560,367);
//		RefineryUtilities.centerFrameOnScreen(jPanel);
		jPanel.setVisible(true);
		JFrame frame = new JFrame("Spending Category Amounts");
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400,400));
		frame.add(jPanel);
		frame.setVisible(true);

	}

    public ArrayList getItems()
    {
        return chartEntries; 
    }

}

