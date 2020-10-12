/*
 * Jimmy Tran
 * Final Project 12/07/17
 * Trouble Ticketing System
 * ITMD 411 Intermediate Software Development
 * ticketsJTable.java
 */

import java.sql.*;
import java.util.*;
import javax.swing.table.*;

public class ticketsJTable {
	
	//instantiating default table model object
	@SuppressWarnings("unused")
	public final DefaultTableModel tm = new DefaultTableModel(); 
	
	//creating table model
	public static DefaultTableModel createTM(ResultSet rs) throws SQLException {
		
		ResultSetMetaData md = rs.getMetaData();
		
		//setting up column names
		Vector<String> cn = new Vector<String>();
		int cc = md.getColumnCount();
		for (int i=1; i<=cc; i++) {
			cn.add(md.getColumnName(i));
		}
		
		//inputting table data
		Vector<Vector<Object>> td = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> v = new Vector<Object>();
			for (int i=1; i<=cc; i++) {
				v.add(rs.getObject(i));
			}
			td.add(v);
		}
		
		//returns table data and column names into the JTable
		return new DefaultTableModel(td, cn);
	}
}
