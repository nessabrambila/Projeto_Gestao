package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DBConnection {
	
	
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/projeto_gestao?serverTimezone=UTC&useSSL=false";
    private static final String USER = "root"; 
    private static final String PASSWORD = "SUA SENHA ROOT AQUI!";
    
    
    public static Connection getConnection() 
    	throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
