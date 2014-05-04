package workload.spark;

import java.util.Properties;

/**
 * conf
 * 
 */
public class WorkloadConf {
	private static ThreadLocal<Properties> threadLocal = new ThreadLocal<Properties>();

	public static void set(Properties properties) {
		threadLocal.set(properties);
	}

	public static String get(String key) {
		return threadLocal.get().getProperty(key);
	}

	public static void clean() {
		threadLocal.remove();
	}
}
