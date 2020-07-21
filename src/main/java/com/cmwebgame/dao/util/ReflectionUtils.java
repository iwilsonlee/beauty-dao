/*
 * Copyright (c) CMWEBGAME Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * This file creation date: 27/09/2004 23:59:10
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.dao.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.log4j.Logger;

/**
 * 反射機制控制
 * Java Reflection Cookbook
 * 
 * @author Wilson
 * 
 */

public class ReflectionUtils {
	private static Logger logger = Logger.getLogger(ReflectionUtils.class);
    /**
     * 得到某个对象的公共属性
     *
     * @param owner
     * @param fieldName
     * @return Object 返回该属性对象
     * @throws Exception
     *
     */
    public static Object getProperty(Object owner, String fieldName) throws Exception {
        @SuppressWarnings("rawtypes")
		Class ownerClass = owner.getClass();

        Field field = ownerClass.getField(fieldName);

        Object property = field.get(owner);

        return property;
    }

    /**
     * 得到某个对象的私有属性
     *
     * @param object
     * @param fieldName
     * @return Object 返回该属性对象
     * @throws Exception
     *
     */
    public static Object getPrivateProperty(Object object, String fieldName) throws Exception {
		@SuppressWarnings("rawtypes")
		Class ownerClass = object.getClass();
		
		Field field = ownerClass.getDeclaredField(fieldName);
			field.setAccessible(true);//私有属性必须加上这个才有效
		Object property = field.get(object);
		
		return property;
	}
    
    /**
     * 得到某类的静态公共属性
     *
     * @param className   类名
     * @param fieldName   属性名
     * @return 该属性对象
     * @throws Exception
     */
    public static Object getStaticProperty(String className, String fieldName)
            throws Exception {
        @SuppressWarnings("rawtypes")
		Class ownerClass = Class.forName(className);

        Field field = ownerClass.getField(fieldName);

        Object property = field.get(ownerClass);

        return property;
    }


    /**
     * 执行某对象方法
     *
     * @param owner
     *            对象
     * @param methodName
     *            方法名
     * @param args
     *            参数
     * @return 方法返回值
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object owner, String methodName, Object[] args)
            throws Exception {

        Class ownerClass = owner.getClass();

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(owner, args);
    }


      /**
     * 执行某类的静态方法
     *
     * @param className
     *            类名
     * @param methodName
     *            方法名
     * @param args
     *            参数数组
     * @return 执行方法返回的结果
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object invokeStaticMethod(String className, String methodName,
            Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(null, args);
    }



    /**
     * 新建实例
     *
     * @param className
     *            类名
     * @param args
     *            构造函数的参数
     * @return 新建的实例
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Constructor cons = newoneClass.getConstructor(argsClass);

        return cons.newInstance(args);

    }


    
    /**
     * 是不是某个类的实例
     * @param obj 实例
     * @param cls 类
     * @return 如果 obj 是此类的实例，则返回 true
     */
    @SuppressWarnings({ "rawtypes" })
    public static boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }
    
    /**
     * 得到数组中的某个元素
     * @param array 数组
     * @param index 索引
     * @return 返回指定数组对象中索引组件的值
     */
    public static Object getByArray(Object array, int index) {
        return Array.get(array,index);
    }
    
    /**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
    @SuppressWarnings({ "rawtypes"})
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}
}
