package it.polimi.tiw.utlli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	final static String DB_URL = "jdbc:mysql://localhost:3306/tiwp?serverTimezone=UTC";//to test connection
	final static String USER = "root";
	final static String PASS = "root";
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (Exception e) {
			e.printStackTrace();
			}
		return null; //fixme   sdadsada
	} 
	public static void closeConnection(Connection toClose) {
		if(toClose==null)
			return;
		try {
			toClose.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
