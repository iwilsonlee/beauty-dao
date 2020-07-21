package com.example.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
	
	private static PooledDruidConnection pool = new PooledDruidConnection();
	
	 private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {  
	        @Override  
	        protected Connection initialValue() {  
	        	try {
	        		pool.init();
	        		System.out.println("connection init ...");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	return pool.getConnection();
	        }  
	    };  
	  
	    public static Connection getConnection() {  
	    	Connection connection = connectionHolder.get();
	    	if (connection == null) {
	    		connection = pool.getConnection();
	    		setConnection(connection);
			}
	    	return connection;
	    }  
	  
	    public static void setConnection(Connection conn) {  
	        connectionHolder.set(conn);  
	    }  
	    
	    public static void releaseConnection(){
	    	Connection connection = connectionHolder.get();
	    	if(connection != null){
	    		try {
	    			System.out.println("closing connection......");
					connection.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	connectionHolder.set(null);
	    	System.out.println("connection already closed！");
	    }
}
