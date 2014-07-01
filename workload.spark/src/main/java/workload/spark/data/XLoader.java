package workload.spark.data;

import workload.spark.Util;

public class XLoader extends Loader{

	@Override
	public void loadData() throws Exception {
		//FIXME SAME AS linuxLoader
		String cmd = "/bin/cp " + Util.getWorkloadPath() + "*.csv " + Util.getWorkloadPath() + "*.dat " + Util.getWorkloadPath() + "*.log "
				+ Util.getWorkPath();
		System.out.println(cmd);
		Util.runCmd(cmd);
	}

}
