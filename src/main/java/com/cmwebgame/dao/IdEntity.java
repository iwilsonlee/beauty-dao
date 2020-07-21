package com.cmwebgame.dao;

import java.io.Serializable;


/**
 * 统一定义id的entity基类.
 * 
 * @author wilson
 */
public class IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
