package workload.spark.data.cleaner;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class CleanerFactory {
	public static Cleaner getCleaner() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_CLEANER).equals("emptyCleaner")) {
			return new EmptyCleaner();
		}
		else if(WorkloadConf.get(Constants.WORKLOAD_STEP_CLEANER).equals("linuxCleaner"))
			return new DstatCleaner(new DriverLogCleaner(new EmptyCleaner()));
		else 
			return new XCleaner(new DriverLogCleaner(new EmptyCleaner()));
	}
}
