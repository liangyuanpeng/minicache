package com.lan.minicache.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 按需执行的定时器
 * <p>
 * Title:AbstractDemandCrawler
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author liangyuanpeng
 * @since 2018-05-23 17:43:25
 */
public abstract class AbstractDemandSchedule {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final ConcurrentHashMap<String, ScheduledFuture<?>> SCHEDULE_CONCURRENT_HASH_MAP = new ConcurrentHashMap<String, ScheduledFuture<?>>();

	// 线程池
	private static final ScheduledThreadPoolExecutor SCHEDULE_EXECUTOR = new ScheduledThreadPoolExecutor(50);
	
	private String className;

	public AbstractDemandSchedule() {
		className = this.getClass().getName();
	}

	/**
	 * 开始定时任务
	 * 
	 * @param initialDelay
	 *            开始延时
	 * @param delay
	 *            间隔
	 * @param unit
	 *            延时单位
	 */
	public synchronized void start(long initialDelay, long delay, TimeUnit unit) {

		if (!SCHEDULE_CONCURRENT_HASH_MAP.containsKey(className)) {
			// 执行爬取定时器
			ScheduledFuture<?> scheduledFuture = init(initialDelay, delay, unit);
			SCHEDULE_CONCURRENT_HASH_MAP.put(className, scheduledFuture);

			logger.info(className + "  START ! ");
		}

	}

	/**
	 * 结束定时任务
	 */
	public synchronized void shutdown() {

		if (SCHEDULE_CONCURRENT_HASH_MAP.containsKey(className)) {
			ScheduledFuture<?> scheduledFuture = SCHEDULE_CONCURRENT_HASH_MAP.get(className);
			scheduledFuture.cancel(false);
			SCHEDULE_CONCURRENT_HASH_MAP.remove(className);
			logger.info(className + " SHUT DOWN ! ");
		}

	}

	private  ScheduledFuture<?> init(long initialDelay, long delay, TimeUnit unit) {

		return SCHEDULE_EXECUTOR.scheduleWithFixedDelay(new Runnable() {
//			@Override
			public void run() {
				try {
					logger.debug(className +".process begin!");
					process();
					logger.debug(className +".process end!");
				} catch (Exception e) {
					logger.error(className + " process fail ! ", e);
				}

			}
		}, initialDelay, delay, unit);

	}

	protected abstract void process() throws Exception;

}
