package com.cmwebgame.repo;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cmwebgame.connection.ConnectionManager;
import com.cmwebgame.dao.util.DataHelper;
import com.cmwebgame.dao.util.DbUtils;
import com.cmwebgame.dao.util.ReflectionUtils;
import com.example.connection.PooledDruidConnection;
import com.example.dao.Article;
import com.example.dao.ArticleDao;
import com.example.dao.ArticleManager;
import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;


public class TestData {
	
	private Connection connection;
	
	private boolean useConn = true;
	
	@Before
	public void before(){
		if(useConn){
			if(this.connection == null){
				PooledDruidConnection pooledDruidConnection = new PooledDruidConnection();
				pooledDruidConnection.init();
				ConnectionManager.createInstance(pooledDruidConnection);
				this.connection = ConnectionManager.getImplementation().getConnection();
			}
		}
	}
	
	@Test
	public void testArticleDao(){//测试增改查
		ArticleManager articleManager = new ArticleDao();
		Article article = new Article();
		article.setContent("hello test content !!!");
		article.setCreateTime(new Timestamp(System.currentTimeMillis()));
		article.setEname("english name");
		article.setName("测试");
		article.setTitle("测试标题");
		article = articleManager.save(article);
		System.out.println("增加一篇文章：");
		System.out.println(article);
		
		System.out.println();
		
		Integer viewCount = article.getViewCount() == null? 0 :article.getViewCount();
		article.setViewCount(viewCount + 1);
		article.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		article = articleManager.save(article);
		System.out.println("修改一篇文章：");
		System.out.println(article);
		
		System.out.println();
		
		System.out.println("查找文章" + article.getId() + "：");
		System.out.println(articleManager.getById(article.getId()));
		
		System.out.println();
		
		System.out.println("查找所有文章：");
		System.out.println(articleManager.findAll());
		
	}
	
//	@Test
	public void testArticleDaoTransaction() {//事务方法测试
		ArticleManager articleManager = new ArticleDao();
		Article article = new Article();
		article.setContent("aaaaa hello test content !!!");
		article.setCreateTime(new Timestamp(System.currentTimeMillis()));
		article.setEname("english name");
		article.setName("测试");
		article.setTitle("测试标题");
		
		Long id = new Long(11);
		boolean result = articleManager.InsertAndUpdate(article, id);
		System.out.println("added article's id is : " + article.getId());
		System.out.println("result : " + result);
//		System.out.println(articleManager.getById(article.getId()));
//		System.out.println(articleManager.getById(id));
	}
	
	@After
	public void after(){
		try {
			ConnectionManager.getImplementation().realReleaseAllConnections();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
