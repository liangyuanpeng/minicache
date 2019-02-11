package com.lan.minicache.cache.abstracts;


import com.lan.minicache.util.LoggerUtil;

import java.util.Date;


/**
 * @author lyp
 *
 */
public abstract class DbCache {
	
	protected int WEIGHT_HEIGHT = 1;
	protected int WEIGHT_MID = 5;
	protected int WEIGHT_LOW = 10;
	
	private Date lastUpdateTime = new Date();

	public abstract String name();
	
	public abstract void flushData() throws Exception;
	
	public abstract int weight();
	
	public final void flush(){
		long t1 = System.currentTimeMillis();
		try {
	   		LoggerUtil.info(String.format("%s flush start!", name()));
			flushData();
			lastUpdateTime = new Date();
			long t2 = System.currentTimeMillis();
			LoggerUtil.info(String.format("%s flush finish!cost time:"+(t2-t1), name()));
		} catch (Throwable e) {
			long t2 = System.currentTimeMillis();
			LoggerUtil.info(String.format("%s flush fail!cost time:"+(t2-t1), name()));
			LoggerUtil.error(e.getMessage(), e);
		}
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	
}
