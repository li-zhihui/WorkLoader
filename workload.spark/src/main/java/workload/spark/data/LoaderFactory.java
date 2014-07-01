package workload.spark.data;

import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.WorkloadConf;

public class LoaderFactory {
	public static Loader getLoader() {

		if (WorkloadConf.get(Constants.WORKLOAD_STEP_LOADER).equals("emptyLoader")) {
			return new EmptyLoader();
		}
		else if(WorkloadConf.get(Constants.WORKLOAD_STEP_LOADER).equals("linuxLoader"))
			return new LinuxLoader();
		else 
			return new XLoader();
	}
}
