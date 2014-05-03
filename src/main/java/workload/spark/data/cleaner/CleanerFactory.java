package workload.spark.data.cleaner;

public class CleanerFactory {
	public static Cleaner getCleaner() {
		return new DstatCleaner(new EmptyCleaner());
	}
}
