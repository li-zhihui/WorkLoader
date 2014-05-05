package workload.spark.data.cleaner;

/**
 * Clean data from Loader, make them easily use to SparkChart.
 * 
 */
public abstract class Cleaner {
	public abstract void clean() throws Exception;
}
