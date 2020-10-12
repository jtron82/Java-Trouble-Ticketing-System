/*
 * Jimmy Tran
 * Final Project 12/07/17
 * Trouble Ticketing System
 * ITMD 411 Intermediate Software Development
 * Login.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login {
	
	//creating instance field and instantiating objects
	static JFrame mainf = new JFrame("Trouble Ticketing System Login");
	static JPanel mainp = new JPanel();
	static JLabel headlb = new JLabel("Trouble Ticketing System Credentials", JLabel.CENTER);
	static JLabel userlb = new JLabel("Username: ");
	static JLabel passlb = new JLabel("Password: ");
	static JTextField usertf = new JTextField(10);
	static JPasswordField userpf = new JPasswordField(10);
	static JButton loginb = new JButton("Login");
	
	
	//call to methods for login
	public Login() {
		ttsGUI();
		GUIdetails();
	}
	
	public void ttsGUI() {
		
		//frame settings
		mainf.setSize(400, 210);
		mainf.setLayout(new GridLayout(3, 1));
		mainf.setLocationRelativeTo(null);
		
		//adding objects to frame
		mainf.add(headlb);
		mainf.add(userlb);
		mainf.add(passlb);
		mainf.add(mainp);
		
		//close window
		mainf.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
	
	public void GUIdetails() {
		
		//actionlistener for login button
		loginb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent x) {
				
				//gets text from user for username and converts user password to string
				String username = usertf.getText();
				String password = new String(userpf.getPassword());
				
				//true or false for admin login/rights
				boolean admin = false;
				if (username.equals("admin") && password.equals("admin")) {

					admin = true;
					
					//close login frame
					mainf.dispose();
					
					//opens ticketsGUI and gives admin administrative rights if admin login = true
					new ticketsGUI("Admin");
				}
				else if (!admin) {
					
					//DAO connection
					Connection con = DAO.getConnection();
					
					//SQL query as a String type
					String query = "SELECT username, password FROM jtran_users where username=? and password=?";
					
					//preparedstatement and resultset variables
					PreparedStatement ps;
					ResultSet rs = null;
					
					try {
						
						//prepared statements for SQL injections
						ps = (PreparedStatement) con.prepareStatement(query);
						ps.setString(1, username);
						ps.setString(2, password);
						rs = ps.executeQuery();
						
						//if statement to verify if username and password are in record table
						if (rs.next()) {
							
							JOptionPane.showMessageDialog(null, "Username & Password are GOOD");
							
							//close login frame
							mainf.dispose();
							
							//opens ticketGUI and gives normal users normal rights if login is good
							new ticketsGUI(username);
						} else {
							JOptionPane.showMessageDialog(null, "Username & Password are BAD");
						}
						
						//error catching for try block
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						
						//try block to close resultset
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						} 
						
						//try block to close connection
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		//panel layout and background color
		mainp.setLayout(new FlowLayout());
		mainp.setBackground(Color.lightGray);
		
		//adding objects to panel
		mainp.add(userlb);
		mainp.add(usertf);
		mainp.add(passlb);
		mainp.add(userpf);
		mainp.add(loginb);
		
		//setting frame to being visible when started
		mainf.setVisible(true);
	}
	
	//call to login method
	public static void main(String[] args) {
		new Login();
	}
}
