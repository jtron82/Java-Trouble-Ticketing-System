/*
 * Jimmy Tran
 * Final Project 12/07/17
 * Trouble Ticketing System
 * ITMD 411 Intermediate Software Development
 * DAO.java
 */

import java.io.*;
import java.sql.*;
import java.util.*;

public class DAO {
	
	//connection and statement instance fields
	static Connection con = null;
	Statement stmt = null;
	
	//setting up constructor
	public static Connection getConnection() {
		
		//try block to setup connection
		try {
			con = DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//create table method
	public void createTables() {
		
		//variable for my tickets table
		final String TicketTable = "CREATE TABLE jtran_tickets" +
							  "(ticket_id INT PRIMARY KEY AUTO_INCREMENT," +
							  "ticket_user VARCHAR(30)," +
							  "ticket_detail VARCHAR(500));";
		
		//variable for my users table
		final String UserTable = "CREATE TABLE jtran_users" +
							"(user_id INT PRIMARY_KEY AUTO_INCREMENT," +
							"username VARCHAR(30)," +
							"password VARCHAR(30));";
		
		try {
			
			//setting up connection to database
			stmt = getConnection().createStatement();
			
			//creating tickets and users table
			stmt.executeUpdate(TicketTable);
			stmt.executeUpdate(UserTable);
			
			//closing connections
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		//calls to add users to my user table
		addUsers();
	}
	
	public void addUsers() {

		//instantiating variables for connection, statement, bufferedreader, and arraylist
		Connection con = null;
		Statement stmt = null;
		BufferedReader br;
		List<List<String>> arr = new ArrayList<>();
		
		//bufferedreader to read the data from usernames.csv file
		try {
			
			//read file
			br = new BufferedReader(new FileReader(new File("./usernames.csv")));
			
			//while loop to split data in csv file
			String x;
			while ((x = br.readLine()) != null) {
				arr.add(Arrays.asList(x.split(",")));
			}
		} catch (Exception e) {
			System.out.println("Cannot Load File");
		}
		
		try {
			
			//setting up connection to database
			con = DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
			stmt = con.createStatement();
			
			//for loop to get array data and SQL query to insert data into my user table
			for (List<String> rowData : arr) {
				
				//insert sql query
				String SQL = "INSERT INTO jtran_users(username, password)" +
						"VALUES ('"+rowData.get(0)+"', '"+rowData.get(1)+"');";
				
				//execute sql query
				stmt.executeUpdate(SQL);
			}
		
		//close statement and connection
		stmt.close();
		con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
