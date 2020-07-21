package com.cmwebgame.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.cmwebgame.dao.Properties;
import com.cmwebgame.dao.util.ReflectionUtils;
import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 * 数据库和entity映射操作封装
 * @author wilson
 *
 */
public class DataHelper {

	/** 
	   * 匹配指定class中数据,并返回包含get和set方法的object 
	   *  
	   * @author Wilson 
	   * @param clazz 对象类型
	   * @param beanProperty 属性
	   * @return Object[] 返回包含get和set方法的object
	   */  
	   private static Object[] beanMatch(@SuppressWarnings("rawtypes") Class clazz, String beanProperty) {  
	       Object[] result = new Object[2];  
//	       char beanPropertyChars[] = beanProperty.toCharArray();  
//	       beanPropertyChars[0] = Character.toUpperCase(beanPropertyChars[0]);  
//	       String s = new String(beanPropertyChars);  

		   String s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, beanProperty);
	       String names[] = { ("set" + s).intern(), ("get" + s).intern(),  
	               ("is" + s).intern(), ("write" + s).intern(),  
	               ("read" + s).intern() };  
	       Method getter = null;  
	       Method setter = null;  
	       Method methods[] = clazz.getMethods();  
	       for (int i = 0; i < methods.length; i++) {  
	           Method method = methods[i];  
	           // 只取公共字段  
	           if (!Modifier.isPublic(method.getModifiers()))  
	               continue;  
	           String methodName = method.getName().intern();  
	           for (int j = 0; j < names.length; j++) {  
	               String name = names[j];  
	               if (!name.equals(methodName))  
	                   continue;  
	               if (methodName.startsWith("set")  
	                       || methodName.startsWith("read"))  
	                   setter = method;  
	               else  
	                   getter = method;  
	           }  
	       }  
	       result[0] = getter;  
	       result[1] = setter;  
	       return result;  
	   }  
	  
	   /** 
	   * 为bean自动注入数据 
	   *  
	   * @author Wilson 
	   * @param object 对象
	   * @param beanProperty 属性名称
	   * @param value 对应属性的值
	   */  
	   private static void beanRegister(Object object, String beanProperty, String value) {  
	       Object[] beanObject = beanMatch(object.getClass(), beanProperty);  
	       Object[] cache = new Object[1];  
	       Method getter = (Method) beanObject[0];  
	       Method setter = (Method) beanObject[1];  
	       try {  
	           // 通过get获得方法类型  
//	    	   System.out.println("beanProperty = " +beanProperty);
	    	   if(getter != null){
	    		   Class<?> cl= getter.getReturnType();
		           String methodType = cl.getName();  
		           if (methodType.equalsIgnoreCase("long") || methodType.equalsIgnoreCase("java.lang.Long")) {  
		               cache[0] = value == null ? null:new Long(value);  
		           } else if (methodType.equalsIgnoreCase("int")  
		                   || methodType.equalsIgnoreCase("integer") || methodType.equalsIgnoreCase("java.lang.Integer")) {  
		               cache[0] = value == null ? null:new Integer(value);  
		           } else if (methodType.equalsIgnoreCase("short") || methodType.equalsIgnoreCase("java.lang.Short")) {  
		               cache[0] = value == null ? null:new Short(value);  
		           } else if (methodType.equalsIgnoreCase("float") || methodType.equalsIgnoreCase("java.lang.Fload")) {  
		               cache[0] = value == null ? null:new Float(value);  
		           } else if (methodType.equalsIgnoreCase("double") || methodType.equalsIgnoreCase("java.lang.Double")) {  
		               cache[0] = value == null ? null:new Double(value);  
		           } else if (methodType.equalsIgnoreCase("boolean") || methodType.equalsIgnoreCase("java.lang.Boolean")) {  
		               cache[0] = value == null ? null:new Boolean(value);  
		           } else if (methodType.equalsIgnoreCase("java.lang.String")) {  
		               cache[0] = value;  
		           } else if (methodType.equalsIgnoreCase("java.io.InputStream")) {  
		           } else if (methodType.equalsIgnoreCase("char")) {  
		               cache[0] = value == null ? null:Character.valueOf(value.charAt(0));  
		           } else if (methodType.equalsIgnoreCase("java.sql.Timestamp")) {
		        	   if(value==null){
		        		   cache[0] = null; 
		        	   }else {
		        		   DateTime dTime =  DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(value);
			        	   cache[0] = new Timestamp(dTime.getMillis());
					   }
		        	   
				   }
		           else if (methodType.equalsIgnoreCase("java.math.BigDecimal")) {
		        	   cache[0] = value == null ? null:new BigDecimal(value);  
				   }
		           else if (methodType.equalsIgnoreCase("java.util.Date") || methodType.equalsIgnoreCase("java.sql.Date")) {
		        	   if(value == null){
		        		   cache[0] = null;
		        	   }else{
		        		   String formaterString = "yyyy-MM-dd";
		        		   if(value.length()>11 && value.length() < 20){
		        			   formaterString = "yyyy-MM-dd HH:mm:ss";
		        		   }else if(value.length() > 19){
		        			   formaterString = "yyyy-MM-dd HH:mm:ss.S";
						   }
		        		   DateTime dTime =  DateTimeFormat.forPattern(formaterString).parseDateTime(value);
		        		   if(methodType.equalsIgnoreCase("java.util.Date")){
		        			   cache[0] = dTime.toDate();
		        		   }else{
		        			   cache[0] = new java.sql.Date(dTime.toDate().getTime());
		        		   }
			        	   
		        	   }
		        	   
		           }else{
		        	   cache[0] = value;
		           }
		           setter.invoke(object, cache); 
		           
		           
	    	   }
	           
	       } catch (Exception e) {  
	    	   System.out.println("exception beanProperty is " + beanProperty);
	    	   System.out.println("exception beanProperty value is " + cache.toString());
	           e.printStackTrace();  
	       }  
	   }  
	  
	   /** 
	   * 转换connection查询结果为指定对象实体集合。 
	   *  
	   * @author Wilson 
	   * @param connection 
	   * @param clazz 
	   * @param sql 
	   * @return List 查询对象结果集
	   */  
	   @SuppressWarnings("rawtypes")
	public List get(final Connection connection, final Class clazz,  
	           final String sql) {  
	       // 创建PreparedStatement  
	       PreparedStatement ptmt = null;  
	       // 创建resultset  
	       ResultSet rset = null;  
	       // 创建collection  
	       List collection = Lists.newArrayList();  
	       try {  
	           // 赋予实例  
	           ptmt = connection.prepareStatement(sql);  
	           rset = ptmt.executeQuery();  
	           collection = get(rset, clazz);  
	       } catch (SQLException e) {  
	           System.err.println(e.getMessage());  
	       } finally {  
	           try {  
	               // 关闭rs并释放资源  
	               if (rset != null) {  
	                   rset.close();  
	                   rset = null;  
	               }  
	               // 关闭ps并释放资源  
	               if (ptmt != null) {  
	                   ptmt.close();  
	                   ptmt = null;  
	               }  
	           } catch (SQLException e) {  
	               System.err.println(e.getMessage());  
	           }  
	       }  
	       return collection;  
	   }  
	  
	   /**
	    * 根据结果集和对象类型解析转换成指定对象类型的List
	    * @param result 数据库结果集
	    * @param clazz 对象类型
	    * @return List 当clazz为null时，返回无固定泛型对象的List
	    */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List get(final ResultSet result, final Class clazz) {
		// 创建collection
		List collection = null;
		try {
			ResultSetMetaData rsmd = result.getMetaData();
			// 获得数据列数
			int cols = rsmd.getColumnCount();
			// 创建等同数据列数的arraylist类型collection实例
			collection = Lists.newArrayListWithCapacity(cols);
			// 遍历结果集
			while (result.next()) {
				if(clazz == null){
					List list = Lists.newArrayList();
					for (int i = 1; i <= cols; i++) {
						list.add(result.getObject(i));
					}
					
					// 将数据插入collection
					collection.add(list);
				}else if(clazz.getName()
						.equalsIgnoreCase("java.util.HashMap")){
					HashMap<String, Object> map = Maps.newHashMap();
					for (int i = 1; i <= cols; i++) {
						map.put(rsmd.getTableName(i)+"."+rsmd.getColumnName(i), result.getObject(i));
					}
					// 将数据插入collection
					collection.add(map);
				}else {
					// 创建对象
					Object object = null;
					if (clazz.getName().equalsIgnoreCase("java.lang.Long")) {
						object = new Long(result.getString(1));
					} else if (clazz.getName()
							.equalsIgnoreCase("java.lang.Integer")) {
						object = new Integer(result.getString(1));
					} else if (clazz.getName().equalsIgnoreCase("java.lang.String")) {
						object = new String(result.getString(1));
					} else if(clazz.getName().equalsIgnoreCase("java.lang.Double")){
						object = new Double(result.getString(1));
					}else {
						try {
							// 从class获得对象实体
							object = clazz.newInstance();
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 循环每条记录
						for (int i = 1; i <= cols; i++) {
							beanRegister(object, rsmd.getColumnName(i),
									result.getString(i));
						}
					}

					// 将数据插入collection
					collection.add(object);
				}
				
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {

		}
		return collection;
	}  
	
	/**
	 * 把指定对象实例转换成insert或update的sql语句和对应字段条件
	 * @param object 指定对象实例
	 * @param isUpdateSql 是否转换成update语句，true是，false否
	 * @return Object[2] 返回固定对象数组，其中object[0]是String类型的sql语句，object[1]是List<Object>类型的sql条件对象 
	 */
	public static Object[] convertSqlAndCondition(Object object, boolean isUpdateSql){
		isUpdateSql = Preconditions.checkNotNull(isUpdateSql);
		object = Preconditions.checkNotNull(object);
		@SuppressWarnings("rawtypes")
		Class clazz = object.getClass();
		String tableName = clazz.getSimpleName();
		tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);
		Field[] fieldsPrivate = clazz.getDeclaredFields();
		Field[] fieldsPublic = clazz.getFields();
		Field[] fields = concat(fieldsPrivate, fieldsPublic);
//		System.out.println("tableName="+tableName);
		
		String[] fieldsString = new String[fields.length];
		String[] vString = new String[fields.length];
		Joiner joiner = Joiner.on(",").skipNulls();
		
		List<Object> list = Lists.newArrayList();
		
		for(int i=0 ; i<fields.length; i++){
			Field field = fields[i];
			String fieldName = field.getName();
			if(!Objects.equal(fieldName, "id") && !Objects.equal(fieldName, "serialVersionUID")){
				boolean canUse = true;
				Properties properties = field.getAnnotation(Properties.class);
				if (properties != null && properties.ignore()) {
					canUse = false;
				}
				if (canUse) {
					if(!isUpdateSql){
						fieldsString[i] = "`"+CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName)+"`";
						vString[i] = "?";
					}else {
						fieldsString[i] = "`"+CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName) + "`=?";
					}
					
					try {
						Object value = null;
						if(!Modifier.isPublic(field.getModifiers())){
							value = ReflectionUtils.getPrivateProperty(object, fieldName);
						}else {
							value = ReflectionUtils.getProperty(object, fieldName);
						}
						list.add(value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
			
			
		}
		
		String sql = "";
		if(!isUpdateSql){
			sql = "INSERT INTO `"+tableName+"`(" + joiner.join(fieldsString) + ") values(" +joiner.join(vString)+ ");";
		}else {
			sql = "UPDATE `"+tableName+"` SET " + joiner.join(fieldsString) + " where id=?";
			try {
				list.add(ReflectionUtils.getProperty(object, "id"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		for (Object object2: list) {
//			System.out.println(object2);
//		}
//		System.out.println(sql);
		Object[] result = new Object[2];
		result[0] = sql;
		result[1] = list;
		return result;
	}
	
	private static Field[] concat(Field[]... arrays) {
	    int length = 0;
	    for (Field[] array : arrays) {
	      length += array.length;
	    }
	    
	    Field[] result = new Field[length];
	    int pos = 0;
	    for (Field[] array : arrays) {
	      System.arraycopy(array, 0, result, pos, array.length);
	      pos += array.length;
	    }
	    return result;
	  }
	
	/**
	 * 根据数据类型执行PreparedStatement的set方法
	 * @param p PreparedStatement
	 * @param index PreparedStatement的索引
	 * @param value 值
	 */
	public static void invokeSetter(PreparedStatement p, int index, Object value){
		Method[] methods = p.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			
			if (method.getName().equals(getSetterName(value)) && method.getParameterTypes().length == 2) {
//				Class[] paramTypes = method.getParameterTypes();
				try {
					if(value!= null && value.getClass().getName().equalsIgnoreCase("java.util.Date")){
						try {
							Method method2 = value.getClass().getMethod("getTime");
							java.sql.Date tmpDate = new java.sql.Date((Long)method2.invoke(value));
							value = tmpDate;
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					method.invoke(p, index,value);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					System.out.println("eror:"+value);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * 根据对象数据类型组成PreparedStatement的set方法
	 * @param o 对象
	 * @return String 返回set方法名
	 */
	public static String getSetterName(Object o){
		if(o == null){
			return "setString";
		}
    	String className = o.getClass().getName();
    	String setter = "set";
    	String setName = "";
    	if(className.equals("java.lang.Integer")){
    		setName = "Int";
    	}else if(className.equals("[B") || className.equals("byte[]")){
    		setName = "Bytes";
    	}else{
    		setName = className.substring(className.lastIndexOf(".")+1, className.length());
    	}
    	
    	return setter + setName;
	}
	
}
