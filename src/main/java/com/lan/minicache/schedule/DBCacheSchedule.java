package com.lan.minicache.schedule;


import com.lan.minicache.cache.factorys.DbCacheFactory;

/**
 * @author lyp
 * 内存缓存定时刷新
 *
 */
public class DBCacheSchedule extends AbstractDemandSchedule{
	
	public static DBCacheSchedule instance = new DBCacheSchedule();
	
	public static DBCacheSchedule getInstance(){
		return instance;
	}

	@Override
	protected void process() throws Exception {
		DbCacheFactory.flushAll();
	}

}
