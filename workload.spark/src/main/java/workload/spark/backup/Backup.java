package workload.spark.backup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class Backup {

	public static void backup() throws Exception {
               if (WorkloadConf.get(Constants.WORKLOAD_EXECUTE_BACKUP).equals("false")) {
                       return;
               }

		Runtime run = Runtime.getRuntime();
		String backupName = WorkloadConf.get(Constants.WORKLOAD_NAME) + "_"
				+ System.currentTimeMillis() + "/";
		String backupPath = WorkloadConf.get(Constants.WORKLOAD_BACKUP_PATH)
				+ backupName;
		run.exec("mkdir " + backupPath).waitFor();
		String cmdS = "/bin/cp "
				+ WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH) + "*.* "
				+ backupPath ;
		String[] cmd = { "/bin/sh", "-c", cmdS };
		System.out.println(cmdS);
		Process p = run.exec(cmd);
		if (p.waitFor() != 0) {
			throw new Exception("backup failed.");
		}
		
		String webPath = System.getProperty("user.dir") +  "/workload.web/src/main/webapp/LogPic";
		Runtime.getRuntime().exec("mkdir " + webPath).waitFor();
		String cmdT = "/bin/mv "
				+ WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH) + "*.* "
				+ webPath ;
		System.out.println(cmdT);
		String[] cmdW = { "/bin/sh", "-c", cmdT };
		p = Runtime.getRuntime().exec(cmdW);
		//clean the buffered area to avoid deadlock
		BufferedInputStream inErr = new BufferedInputStream(p.getErrorStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(inErr));
		BufferedInputStream inInput = new BufferedInputStream(p.getInputStream());
		BufferedReader br1 = new BufferedReader(new InputStreamReader(inInput));
		String line = null;
		while((line = br.readLine())!= null)
		while((line = br1.readLine())!= null)
		if (p.waitFor() != 0) {
			throw new Exception("web copy failed.");
		}
	}
	
	
}
