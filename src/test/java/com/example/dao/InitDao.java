package com.example.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.cmwebgame.connection.ConnectionManager;
import com.cmwebgame.dao.BaseDao;

public class InitDao<E> extends BaseDao<E> {

	@Override
	protected Connection getConnection() {
		// TODO Auto-generated method stub
		try {
			if(super.connection == null || super.connection.isClosed()){
				super.connection = ConnectionManager.getImplementation().getConnection();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.connection;
	}

	@Override
	public void setConnection(Connection connection) {
		// TODO Auto-generated method stub
		super.connection = connection;
	}
	

}
