package com.example.dao;

import java.util.List;

public interface ArticleManager {

	public Article getById(Long id);
	
	public List<Article> findAll();
	
	public Article save(Article article);
	
	public boolean InsertAndUpdate(Article article, Long id);
}
