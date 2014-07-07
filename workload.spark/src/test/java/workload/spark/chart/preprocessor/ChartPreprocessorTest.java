package workload.spark.chart.preprocessor;



import java.util.List;

import workload.spark.BaseTestCase;
import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.WorkloadConf;
import workload.spark.WorkloadContext;
import workload.spark.des.CommandDes;
import workload.spark.runner.XRunnerTest;

public class ChartPreprocessorTest extends BaseTestCase{
    XRunnerTest run = new XRunnerTest();
	public void testChartPreprocessor() throws Exception{
		setUp();
		run.testXRunner();
		//System.out.println(testDataFolder);
		String[] command = WorkloadConf.get(Constants.WORKLOAD_RUNNER_COMMAND).split(Constants.DATA_SPLIT);
		for(int k = 0 ; k < command.length; k++){
			XChartPreprocessor xp = new XChartPreprocessor();
			xp.setCSVFolder(testDataFolder);
			CommandDes cd = (CommandDes) WorkloadContext.get(command[k]);
			List<String> slaves = Util.getSlavesHost();
			for (String slave : slaves) {
				WorkloadContext.put(slave + "_" + command[k],xp.getDataList(cd,slave));
			}
		}
	}
}

