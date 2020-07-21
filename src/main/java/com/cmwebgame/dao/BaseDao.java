package com.cmwebgame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cmwebgame.dao.exceptions.DatabaseException;
import com.cmwebgame.dao.util.DataHelper;
import com.cmwebgame.dao.util.DbUtils;
import com.cmwebgame.dao.util.ReflectionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * DAO基础类
 * @author wilson
 *
 * @param <E>
 */
public abstract class BaseDao<E> extends AutoKeys {
	
	protected Class<E> entityClass;
	
	protected Connection connection;
	
	public BaseDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}
	
	protected abstract Connection getConnection();
	/**
	 * 注意：此方法只提供给单元测试赋值connection，正式web环境不能用
	 * @param connection
	 */
	public abstract void setConnection(Connection connection);

	/**
	 * 根据sql和查询条件获取对象列表
	 * @param sql 查询语句，如：select * from member where age = ? and gender = ? ;
	 * @param conditions 查询条件内容
	 * @return List 返回带指定泛型对象的list
	 */
	@SuppressWarnings("unchecked")
	protected List<E> getResultSetByCondition(String sql, List<Object> conditions){
		return this.getResultSetByCondition(sql, conditions, entityClass);
	}
	
	/**
	 * 根据sql和查询条件获取对象列表
	 * @param connection 有可能带事务属性的connection，若需事务处理,需先执行Connection.setAutoCommit(false)
	 * @param sql 查询语句，如：select * from member where age = ? and gender = ? ;
	 * @param conditions 查询条件内容
	 * @return List 返回带指定泛型对象的list
	 */
	@SuppressWarnings("unchecked")
	protected List<E> getResultSetByCondition(Connection connection, String sql, List<Object> conditions){
		return this.getResultSetByCondition(connection, sql, conditions, entityClass);
	}

	/**
	 * 
	 * 根据sql和查询条件获取对象列表
	 * @param sql 查询语句，如：select * from member where age = ? and gender = ? ;
	 * @param conditions 查询条件内容
	 * @param clazz 指定查询对象，若clazz为null，返回无固定泛型对象的List
	 * @return List 返回的List泛型由clazz决定
	 * @author wilson
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected List getResultSetByCondition(String sql, List<Object> conditions , Class clazz){
		return this.getResultSetByCondition(getConnection(), sql, conditions, clazz);
		
	}
	
	/**
	 * 插入一个对象实例数据到数据库
	 * @param object 对象实例
	 * @return int 返回所插入数据的记录的id主键值
	 */
	@SuppressWarnings("unchecked")
	protected int insert(Object object){
		object = Preconditions.checkNotNull(object);
		Object[] result = DataHelper.convertSqlAndCondition(object, false);
		String sql = (String)result[0];
		List<Object> conditions = (List<Object>)result[1];
		int id = this.insert(sql, conditions);
		
		return id;
	}
	
	/**
	 * 插入一个对象实例数据到数据库
	 * @param connection 有可能带事务属性的connection，若需事务处理，
	 * 需先执行Connection.setAutoCommit(false)
	 * @param object 对象实例
	 * @return int 返回所插入数据的记录的id主键值
	 */
	@SuppressWarnings("unchecked")
	protected int insert(Connection connection, Object object){
		object = Preconditions.checkNotNull(object);
		Object[] result = DataHelper.convertSqlAndCondition(object, false);
		String sql = (String)result[0];
		List<Object> conditions = (List<Object>)result[1];
		int id = this.insert(connection, sql, conditions);
		
		return id;
	}
	
	/**
	 * 插入一条数据记录
	 * @param sql insert sql语句，如：insert into member(user_name,password,age) values(?,?,?);
	 * @param conditions insert值,如：{@code Lists.newArrayList("wilson","12345678",22);}
	 * @return int 返回insert后的id主键
	 */
	protected int insert(String sql, List<Object> conditions){
		Connection conn = getConnection();
		return this.insert(conn, sql, conditions);
	}
	
	/**
	 * 更新一个对象实例数据岛数据库
	 * @param object 对象实例
	 * @return boolean 更新执行结果，true成功，false失败
	 */
	@SuppressWarnings("unchecked")
	protected boolean modify(Object object){
		object = Preconditions.checkNotNull(object);
		Object[] results = DataHelper.convertSqlAndCondition(object, true);
		String sql = (String)results[0];
		List<Object> conditions = (List<Object>)results[1];
		boolean result = this.modify(sql, conditions);
		return result;
	}
	
	/**
	 * 更新一个对象实例数据岛数据库
	 * @param connection 有可能带事务属性的connection，若需事务处理，
	 * 需先执行Connection.setAutoCommit(false)
	 * @param object 对象实例
	 * @return boolean 更新执行结果，true成功，false失败
	 */
	@SuppressWarnings("unchecked")
	protected boolean modify(Connection connection, Object object){
		object = Preconditions.checkNotNull(object);
		Object[] results = DataHelper.convertSqlAndCondition(object, true);
		String sql = (String)results[0];
		List<Object> conditions = (List<Object>)results[1];
		boolean result = this.modify(connection, sql, conditions);
		return result;
	}
	
	/**
	 * 更新一条数据记录
	 * @param sql insert sql语句，如：UPDATE members SET user_name=?,password=?,age=? where id=?;
	 * @param conditions insert值,如：{@code Lists.newArrayList("wilson","12345678",22,2);}
	 * @return boolean true成功，false失败
	 */
	protected boolean modify(String sql, List<Object> conditions){
		return this.modify(getConnection(), sql, conditions);
	}
	
	/**
	 * 更新一条数据记录
	 * @param connection 有可能带事务属性的connection，若需事务处理，
	 * 需先执行Connection.setAutoCommit(false)
	 * @param sql insert sql语句，如：UPDATE members SET user_name=?,password=?,age=? where id=?;
	 * @param conditions insert值,如：{@code Lists.newArrayList("wilson","12345678",22,2);}
	 * @return boolean true成功，false失败
	 */
	protected boolean modify(Connection connection, String sql, List<Object> conditions){
		PreparedStatement p = null;
		try {
			//UPDATE members SET user_name=?,password=?,age=? where id=?
			p = connection.prepareStatement(sql);
			int count = conditions.size();
//			System.out.println("modify sql:"+sql);
			for(int i=1; i<=count; i++){
				Object object = conditions.get(i-1);
//				System.out.println(object);
				DataHelper.invokeSetter(p, i, object);
			}
			return p.executeUpdate()==1?true:false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				if (!connection.getAutoCommit()) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new DatabaseException(e);
		}finally {
			DbUtils.close(p);
		}
	}
	
	/**
	 * 插入一条数据记录
	 * @param connection 有可能带事务属性的connection，若需事务处理，
	 * 需先执行Connection.setAutoCommit(false)
	 * @param sql insert sql语句，如：insert into member(user_name,password,age) values(?,?,?);
	 * @param conditions insert值,如：{@code Lists.newArrayList("wilson","12345678",22);}
	 * @return int 返回insert后的id主键
	 */
	protected int insert(Connection connection,String sql, List<Object> conditions){
		Connection conn = connection;
		PreparedStatement p = null;
		try {
			//INSERT INTO members() values(); 
			this.setAutoGeneratedKeysQuery("true");
			p = this.getStatementForAutoKeysBySql(sql, conn);
			int count = conditions.size();
			for(int i=1; i<=count; i++){
				Object object = conditions.get(i-1);
				DataHelper.invokeSetter(p, i, object);
			}
			int id = this.executeAutoKeysQuery(p, conn);
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				if (!connection.getAutoCommit()) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new DatabaseException(e);
		}finally {
			DbUtils.close(p);
		}
	}
	
	/**
	 * 
	 * 根据sql和查询条件获取对象列表
	 * @param connection 有可能带事务属性的connection，若需事务处理，
	 * 需先执行Connection.setAutoCommit(false)
	 * @param sql 查询语句，如：select * from member where age = ? and gender = ? ;
	 * @param conditions 查询条件内容
	 * @param clazz 指定查询对象，若clazz为null，返回无固定泛型对象的List
	 * @return List 返回的List泛型由clazz决定
	 * @author wilson
	 * 
	 */
	protected List getResultSetByCondition(Connection connection, String sql, List<Object> conditions , Class clazz){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = connection.prepareStatement(sql);
			int count = conditions.size();
			for(int i=1; i<=count; i++){
				Object object = conditions.get(i-1);
				DataHelper.invokeSetter(p, i, object);
			}
			rs = p.executeQuery();
			List list = Lists.newArrayList();
			list = DataHelper.get(rs, clazz);
			return list;
			
		}
		catch (SQLException e) {
			try {
				if (!connection.getAutoCommit()) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
	}
	
}
