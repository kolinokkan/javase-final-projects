package com.jdc.phoneshop.common.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class ConnectionManager {
	
	private static String URL;
	private static String USER;
	private static String PASS;
	
	static {
		try {
			Properties props = new Properties();
			props.load(Files.newInputStream(Paths.get("db.properties"), LinkOption.NOFOLLOW_LINKS));
			
			URL = props.getProperty("db.url");
			USER = props.getProperty("db.user");
			PASS = props.getProperty("db.pass");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
	
	public static void truncate(String ... tables) {
		
		String fkConstrient = "SET FOREIGN_KEY_CHECKS = %d";
		String sql = "truncate table %s";
		
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement()) {
			// set fk check off
			stmt.execute(String.format(fkConstrient, 0));
			
			for(String str : tables) {
				stmt.execute(String.format(sql, str));
			}
			
			// set fk check on
			stmt.execute(String.format(fkConstrient, 1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void truncateTables() {
		try {
			List<String> tables = Files.readAllLines(Paths.get("tables.txt"));
			truncate(tables.toArray(new String[tables.size()]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		truncateTables();
	}

}
