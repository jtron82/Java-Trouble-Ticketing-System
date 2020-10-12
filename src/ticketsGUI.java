/*
 * Jimmy Tran
 * Final Project 12/07/17
 * Trouble Ticketing System
 * ITMD 411 Intermediate Software Development
 * ticketsGUI.java
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class ticketsGUI implements ActionListener {
	
	//instantiating variables
	DAO DAO = new DAO();
	String admin = null;
	static JFrame mainf = new JFrame("Trouble Ticketing System");
	static JLabel lb = new JLabel("Admin Buttons", JLabel.LEFT);
	static JLabel lb2 = new JLabel("User Buttons", JLabel.LEFT);
	static JPanel panel = new JPanel();
	
	//scroll pane variable to use scrolling property
	JScrollPane sp = null;

	//jbuttons
	JButton OpenTicketbutton;
	JButton ViewTicketbutton;
	JButton UpdateTicketbutton;
	JButton DeleteTicketbutton;
	JButton Exitbutton;
	
	//setting up constructor
	public ticketsGUI(String user) {
		
		//checks for type of user
		admin = user;
		JOptionPane.showMessageDialog(null, "Welcome " +user);
		if (admin.equals("Admin")) {
			
			//starts up createTables method 
			DAO.createTables();
			
			//initiates Menubuttons and AdminGUI methods
			Menubuttons();
			AdminGUI();
		}
		else if (admin != "Admin") {
			
			//starts up createTables method 
			DAO.createTables();
			
			//initiates Menubuttons and UserGUI methods
			Menubuttons();
			UserGUI();
		}
	}
	
	public void Menubuttons() {
		
		//instantiating objects
		OpenTicketbutton = new JButton("Open Ticket");	
		ViewTicketbutton = new JButton("View Ticket");
		UpdateTicketbutton = new JButton("Update Ticket");
		DeleteTicketbutton = new JButton("Delete Ticket");		
		Exitbutton = new JButton("Exit");
		
		//giving button objects an action
		OpenTicketbutton.addActionListener(this);
		ViewTicketbutton.addActionListener(this);
		UpdateTicketbutton.addActionListener(this);
		DeleteTicketbutton.addActionListener(this);
		Exitbutton.addActionListener(this);
	}
	
	public void AdminGUI() {
		
		//adding panel to frame
		mainf.add(panel);
		
		//admin ticket buttons
		panel.add(lb);
		panel.add(OpenTicketbutton);
		panel.add(ViewTicketbutton);
		panel.add(UpdateTicketbutton);
		panel.add(DeleteTicketbutton);
		
		//exit button
		panel.add(Exitbutton);
		
		//close window
		mainf.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
		//frame settings
		mainf.setSize(800, 600);
		mainf.setBackground(Color.gray);
		mainf.setLayout(new GridLayout(3, 1));
		mainf.setLocationRelativeTo(null);
		mainf.setVisible(true);
		
		//panel layout
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	}
	
	public void UserGUI() {
		
		//adding panel to frame
		mainf.add(panel);
		
		//user ticket buttons
		panel.add(lb2);
		panel.add(OpenTicketbutton);
		panel.add(ViewTicketbutton);
		
		//exit button
		panel.add(Exitbutton);
		
		//panel layout
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	}
	@Override
	public void actionPerformed(ActionEvent a) {
		
		//initiating connections
		Connection con = null;
		Statement stmt = null;
		
		//exit button
		if (a.getSource() == Exitbutton) {
			System.exit(0);
		}
		//open ticket button
		else if (a.getSource() == OpenTicketbutton) {
			
			try {
				
				//grab ticket user name and ticket details
				String userticketName = JOptionPane.showInputDialog(null, "Enter Name");
				String userticketDetail = JOptionPane.showInputDialog(null, "Enter Ticket Details");
				
				//starting up connection
				con = DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
				stmt = con.createStatement();
				
				//insert ticket details to jtran_tickets
				int x = stmt.executeUpdate("INSERT INTO jtran_tickets(ticket_user, ticket_detail)" +
											"VALUES ('"+userticketName+"', '"+userticketDetail+"');", Statement.RETURN_GENERATED_KEYS);
				
				//grabs ticket id that has AUTO_INCREMENT for each record inserted
				ResultSet rs = null;
				rs = stmt.getGeneratedKeys();
				int numID = 0;
				
				//if statement to get first data in table
				if (rs.next()) {
					numID = rs.getInt(1);
				}
				
				//if statement to display ticket id creation
				if (x != 0) {
					System.out.println("Ticket ID: "+numID+" Created.");
					JOptionPane.showMessageDialog(null, "Ticket ID: "+numID+" Created.");
				}
				
				//else statement to display that ticket cannot be created
				else {
					System.out.println("Cannot Create Ticket");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		//view ticket button
		else if (a.getSource() == ViewTicketbutton) {
			
			try {
				
				//setup connections
				con = DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
				stmt = con.createStatement();
				
				//for user GUI
				if (admin != "Admin") {
					
					//string variable to identify ticket user to view ticket users tickets
					String userticketName = JOptionPane.showInputDialog(null, "Enter Ticket User to View User Tickets");
					
					//SQL query to view jtran_tickets table
					ResultSet rs = stmt.executeQuery("SELECT * FROM jtran_tickets WHERE ticket_user = '"+userticketName+"';");
					
					//ticketsJTable with rows and columns to hold data
					JTable jtable = new JTable(ticketsJTable.createTM(rs));
					jtable.setBounds(80, 90, 400, 400);
					sp = new JScrollPane(jtable);
					mainf.add(sp);
					mainf.setVisible(true);
				}
				
				//for admin GUI
				if (admin.equals("Admin")) {
					
					//SQL query to view jtran_tickets table
					ResultSet rs = stmt.executeQuery("SELECT * FROM jtran_tickets;");
					
					//ticketsJTable with rows and columns to hold data
					JTable jtable = new JTable(ticketsJTable.createTM(rs));
					jtable.setBounds(80, 90, 400, 400);
					sp = new JScrollPane(jtable);
					mainf.add(sp);
					mainf.setVisible(true);
				}
				
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		} 
		//update ticket button
		else if (a.getSource() == UpdateTicketbutton) {
			
			try {
				
				//string variables to identify ticket user and new ticket detail to update ticket
				String userticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to Update");
				String newticketDetail = JOptionPane.showInputDialog(null, "Enter New Ticket Details");
				
				//setup connections
				con = DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
				stmt = con.createStatement();
				
				//SQL query to update jtran_tickets table
				int y = stmt.executeUpdate("UPDATE jtran_tickets SET ticket_detail = '"+newticketDetail+"' WHERE ticket_id = '"+userticketID+"';");
				
				//if statement to display that the specified ticket id's detail was updated
				if (y != 0) {
					System.out.println("Ticket ID: "+userticketID+" Updated.");
					JOptionPane.showMessageDialog(null, "Ticket ID: "+userticketID+" Updated.");
				}
				
			} catch (SQLException eee) {
				eee.printStackTrace();
			}
		} 
		//delete ticket button
		else if (a.getSource() == DeleteTicketbutton) {
			
			try {
				
				//string variable to identify ticket id to delete
				String userticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to Delete");
				
				//setup connections
				con = DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
				stmt = con.createStatement();
				
				//SQL query to delete ticket from jtran_tickets
				int z = stmt.executeUpdate("DELETE FROM jtran_tickets WHERE ticket_id = '"+userticketID+"';");
				
				//if statement to display that the specified ticket id was deleted
				if (z != 0) {
					System.out.println("Ticket ID: "+userticketID+" Deleted.");
					JOptionPane.showMessageDialog(null, "Ticket ID: "+userticketID+" Deleted.");
				}
				
				//else statement to display that ticket id cannot be deleted
				else {
					System.out.println("Cannot Delete Ticket");
				}
				
				//closing connection and statement
				stmt.close();
				con.close();
				
			} catch (SQLException eeee) {
				eeee.printStackTrace();
			}
		}
	}
}