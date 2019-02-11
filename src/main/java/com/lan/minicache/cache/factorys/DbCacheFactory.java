package com.lan.minicache.cache.factorys;


import com.lan.minicache.cache.abstracts.AbstractDbCache;
import com.lan.minicache.cache.abstracts.DbCache;
import com.lan.minicache.schedule.DBCacheSchedule;
import com.lan.minicache.util.LoggerUtil;
import com.lan.minicache.util.ReflectorUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.*;

/**
 * 内存缓存工厂类
 * 
 * @author lyp
 *
 */
public class DbCacheFactory {

	private static ConcurrentMap<String, DbCache> dataLoaderMap = new ConcurrentHashMap<String, DbCache>();

	static ExecutorService singleThreadExecutor = Executors
			.newSingleThreadExecutor();

	static ExecutorService reloadCacheThreadExecutor = Executors
			.newSingleThreadExecutor();

//	static {
//		init();
//	}

	private static String dbpack = "";

	public DbCacheFactory(String dbpack){
		this.dbpack = dbpack;
		init();
	}

	private static void init() {
		String basepack = "com.lan.minicache.cache.abstracts.DbCache";
		Set<Class<?>> baseset = ReflectorUtils.getClasses(basepack);

		String pack = dbpack;
		Set<Class<?>> set = ReflectorUtils.getClasses(pack);
		set.addAll(baseset);
		// Class<DbCache> dbCacheClass = DbCache.class;
		for (Class<?> claes : set) {
			try {
				if (claes.getName().contains("$")) {
					continue;
				}
				if (Modifier.isAbstract(claes.getModifiers())) {
					continue;
				}
				if (Modifier.isInterface(claes.getModifiers())) {
					continue;
				}
				if (!AbstractDbCache.class.isAssignableFrom(claes)
						|| claes.toString().equals(
								AbstractDbCache.class.toString())) {
					continue;
				}
				LoggerUtil.info("begin newinstance {}", claes.toString());
				claes.newInstance();
			} catch (Exception e) {
				LoggerUtil.error(e.getMessage(), e);
			}
		}
	}

	public static <T extends DbCache> DbCache getDataLoader(
			final Class<T> requiredClass) {
		return dataLoaderMap.get(requiredClass.getName());
	}

	public static <T extends DbCache> void register(DbCache dbCache) {
		dataLoaderMap.putIfAbsent(dbCache.getClass().getName(), dbCache);
	}

	public static Collection<DbCache> getAllCache() {
		return dataLoaderMap.values();
	}

	public static <T> void reload(Class<T> claes) {

		final DbCache dbCache = (DbCache) getCache(claes);

		LoggerUtil.info("DbCacheFactory.begin.reload:{}", dbCache.getClass());
		reloadCacheThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				dbCache.flush();
			}
		});
	}

	public static void reload(final DbCache dbCache) {
		LoggerUtil.info("DbCacheFactory.begin.reload:{}", dbCache.getClass());
		reloadCacheThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				dbCache.flush();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T> T getCache(Class<T> claes) {
		if (claes == null) {
			return null;
		}
		DbCache cache = dataLoaderMap.get(claes.getName());
		if (cache != null) {
			return (T) cache;
		} else {
			LoggerUtil.error(String.format("Get cache fail,class:%s",
					claes.getName()));
			return null;
		}
	}

	public static void flushAll() {
		 List<DbCache> caches = new LinkedList<DbCache>(dataLoaderMap.values());
		Collections.sort(caches, new Comparator<DbCache>() {
			@Override
			public int compare(DbCache o1, DbCache o2) {
				if (o1.weight() > o2.weight()) {
					return 1;
				}
				return -1;
			}
		});
		for (final DbCache dbCache : caches) {
			singleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					dbCache.flush();
				}
			});
		}
	}

	public void startBase(long initialDelay, long delay, TimeUnit unit){
		DBCacheSchedule.getInstance().start(initialDelay,delay,unit);
	}
}
