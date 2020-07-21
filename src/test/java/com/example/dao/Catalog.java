package com.example.dao;


import java.sql.Timestamp;
import java.util.List;

import com.cmwebgame.dao.IdEntity;
import com.cmwebgame.dao.Properties;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * 目录实体
 * @author wilson
 *
 */
public class Catalog extends IdEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 父亲Id
	 */
	private long fatherId;

	/**
	 * 英文名称
	 */
	private String ename;

	/**
	 * 目录名称
	 */
	private String name;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 路径
	 */
	private String path;
	/**
	 * 层级
	 */
	private int level;

	/**
	 * 排序
	 */
	private int sort;

	/**
	 * 文件数
	 */
	private int count;

	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 拥有者
	 */
	private String owner;
	
	private String status;
	
	private String rank;
	
	private Timestamp createTime;
	
	@Properties(ignore=true)
	private List<Catalog> catalogList = Lists.newArrayList();

	@Properties(ignore=true)
	private List<String> catalogPathList = Lists.newArrayList();
	

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<Catalog> getCatalogList() {
		return catalogList;
	}

	public void setCatalogList(List<Catalog> catalogList) {
		this.catalogList = catalogList;
	}

	public List<String> getCatalogPathList() {
		return catalogPathList;
	}

	public void setCatalogPathList(List<String> catalogPathList) {
		this.catalogPathList = catalogPathList;
	}

	public long getFatherId() {
		return fatherId;
	}

	public void setFatherId(long fatherId) {
		this.fatherId = fatherId;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	

	@Override
	public String toString() {
		return "Catalog [id=" + id + ", fatherId=" + fatherId + ", ename=" + ename + ", name="
				+ name + ", content=" + content + ", path=" + path + ", level="
				+ level + ", sort=" + sort + ", count=" + count + ", type="
				+ type + ", owner=" + owner + ", createTime=" + createTime + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, name, path, ename, status);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Catalog) {
			Catalog catalog = (Catalog) obj;
			return Objects.equal(id, catalog.id)
					&& Objects.equal(name, catalog.name)
					&& Objects.equal(path, catalog.path)
			&& Objects.equal(ename, catalog.ename)
			&& Objects.equal(status, catalog.status);
		}
		
		return false;
		
	}
	
	

}
