package accounts;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import java.sql.*;

public abstract class Account {

	ArrayList<Transaction> transactions;
	Connection connection;
	String sourceName;
	String filenamePrefix;

	Account() {
		transactions = new ArrayList<Transaction>();
	}

	public String getSourceName() {
		return sourceName;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(Transaction t) {
		transactions.add(t);
	}

	public int getNumberTransactions() {
		return(transactions.size());
	}

	public Transaction getTransaction(int i) {
		return transactions.get(i);
	}

	public void printTransactions() {
		// This is an extended version of print Transactions
		for (int i = 0; i < this.getNumberTransactions(); i++) {
			Transaction t = this.getTransaction(i);
			t.print();
		}
	}
	
	public void loadDirectory(String downloadPath) throws IOException, ParseException {
		File directory = new File(downloadPath);

		FilenameFilter filter = (d, s) -> {
			return s.matches(filenamePrefix);
		};

		File[] listOfFiles = directory.listFiles(filter);
		if (listOfFiles == null) {
			System.out.println("No files to load in directory " + downloadPath + 
					" with filename prefix: " + filenamePrefix);
			return;
		}
		
		Arrays.parallelSort(listOfFiles, Comparator.comparingLong(File::lastModified));
		
		for (int i = 0; i < listOfFiles.length; i++) {
			loadTransactionsFromFile(downloadPath+listOfFiles[i].getName());
		}
		
	}



	public void loadLatestFile(String downloadPath) throws IOException, ParseException {

		File directory = new File(downloadPath);


		FilenameFilter filter = (d, s) -> {
			return s.matches(filenamePrefix);
		};

		File[] listOfFiles = directory.listFiles(filter);
		
		Arrays.parallelSort(listOfFiles, Comparator.comparingLong(File::lastModified));
		
		// now just get the last one
		loadTransactionsFromFile( downloadPath+listOfFiles[listOfFiles.length-1].getName());
		
	}

	public abstract void loadDatabaseWithTransactions(Connection connection) throws SQLException;
	public abstract void loadTransactionsFromFile(String filename) throws IOException, ParseException;

	public void loadTransactionsFromDatabase(Connection c, String beginDate, String endDate) throws SQLException
	{	
			String and1 ="", and2 = "";
			String endQuote = "'";
			if (beginDate == null) 
			{
				beginDate = "";
			} else and1 = " where transactionDate >= '" +beginDate+ endQuote;
			if (endDate == null)
			{ 
				endDate = ""; 
			} else and2 = " and transactionDate <= '" + endDate + endQuote;
			
			String queryString = "select * from BigTXView " + and1 + and2 + ";";
	
			System.out.println("query is: " + queryString);
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(queryString);
			try 
			{
				while (rs.next()) 
				{
					Transaction t = new BigViewTransaction();
					t.loadTransactionFromDatabase(rs);
					this.addTransaction(t);
				}
			} catch (SQLException e) 
			{
					System.out.println("problem reading transactions from Database into memory");
			}
	}

}
