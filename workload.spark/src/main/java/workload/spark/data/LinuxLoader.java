package workload.spark.data;

import workload.spark.Util;

public class LinuxLoader extends Loader {

	@Override
	public void loadData() throws Exception {
		String cmd = "/bin/cp " + Util.getWorkloadPath() + "*.csv " + Util.getWorkloadPath() + "*.dat " + Util.getWorkloadPath() + "*.log "
				+ Util.getWorkPath();
		System.out.println(cmd);
		Util.runCmd(cmd);
	}

}
