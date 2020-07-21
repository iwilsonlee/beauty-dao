package com.cmwebgame.connection;

import java.sql.Connection;

 
public abstract class ConnectionManager {
	
	private static ConnectionManager instance;
	
	public static boolean createInstance(ConnectionManager implementation)
	{
		instance = implementation;
		return true;
	}
	
	public static ConnectionManager getImplementation(){
		return instance;
	}
	
	/**
	 * Inits the implementation. 
	 * Connection pools may use this method to init the connections from the
	 * database, while non-pooled implementation can provide an empty method
	 * block if no other initialization is necessary.
	 * <br>
	 * Please note that this method will be called just once, at system startup. 
	 * 
	 * @throws Exception
	 */
	public abstract void init() throws Exception;
	
	/**
	 * Gets a connection.
	 * Connection pools' normal behaviour will be to once connection
	 * from the pool, while non-pooled implementations will want to
	 * go to the database and get the connection in time the method
	 * is called.
	 * 
	 * @return Connection
	 */
	public abstract Connection getConnection();
	
	/**
	 * Releases a connection.
	 * Connection pools will want to put the connection back to the pool list,
	 * while non-pooled implementations should call <code>close()</code> directly
	 * in the connection object.
	 * 
	 * @param conn The connection to release
	 */
	public abstract void releaseConnection(Connection conn);
	
	/**
	 * Close all open connections.
	 * 
	 * @throws Exception
	 */
	public abstract void realReleaseAllConnections() throws Exception;
}
