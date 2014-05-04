package workload.spark.data.cleaner;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class CleanerFactory {
	public static Cleaner getCleaner() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_CLEANER).equals("false")) {
			return new EmptyCleaner();
		}
		return new DstatCleaner(new DriverLogCleaner(new EmptyCleaner()));
	}
}
