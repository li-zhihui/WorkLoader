package workload.spark.data;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class LoaderFactory {
	public static Loader getLoader() {
		if(WorkloadConf.get(Constants.WORKLOAD_STEP_LOADER).equals("linuxLoader"))
			return new LinuxLoader();
		else if(WorkloadConf.get(Constants.WORKLOAD_STEP_LOADER).equals("XLoader"))
			return new XLoader();
		else
				return new EmptyLoader();
	}
}
