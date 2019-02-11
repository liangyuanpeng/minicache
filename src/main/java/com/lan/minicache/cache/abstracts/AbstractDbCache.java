package com.lan.minicache.cache.abstracts;


import com.lan.minicache.cache.factorys.DbCacheFactory;

/**
 * @author lyp
 *
 */
public abstract class AbstractDbCache extends DbCache{

	private String name = "";
	
	public AbstractDbCache(){
		this.name = this.getClass().getName();
		DbCacheFactory.register(this);
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public int weight() {
		return WEIGHT_LOW;
	}
	
	protected void reload(){
		DbCacheFactory.reload(this);
	}
}
