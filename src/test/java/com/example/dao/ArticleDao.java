package com.example.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ArticleDao extends InitDao<Article> implements ArticleManager{

	@Override
	public Article getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from article where id = ?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Article> list = this.getResultSetByCondition(sql, conditions);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public List<Article> findAll() {
		// TODO Auto-generated method stub
		String sql = "select * from article";
		List<Object> conditions = Lists.newArrayList();
		List<Article> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public Article save(Article article) {
		// TODO Auto-generated method stub
		article = Preconditions.checkNotNull(article,"article对象不能为空");
		if (article.getId() != null) {
			this.modify(article);
		}else {
			int id = this.insert(article);
			article.setId(new Long(id));
		}
		return article;
	}

	@Override
	public boolean InsertAndUpdate(Article article, Long id) {//事务使用
		// TODO Auto-generated method stub
		Connection connection = this.getConnection();
		boolean result = false;
		try {
			connection.setAutoCommit(false);
			
			int newId = this.insert(connection, article);
			article.setId(new Long(newId));
			Article tmpArticle = this.getById(id);
			tmpArticle.setContent(article.getContent());
			result = this.modify(tmpArticle);
			
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}

	
}
