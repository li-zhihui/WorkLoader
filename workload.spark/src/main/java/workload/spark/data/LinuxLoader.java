package workload.spark.data;

import workload.spark.Util;

public class LinuxLoader extends Loader {

	@Override
	public void loadData() throws Exception {
		String cmd = "/bin/mv " + Util.getWorkloadPath() + "*.csv "
				+ Util.getWorkPath();
		System.out.println(cmd);
		Util.runCmd(cmd);
	}

}
