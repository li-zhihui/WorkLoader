package workload.spark.data.parser;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class LogParserFactory {
	public static LogParser getLogParser() {

		if (!WorkloadConf.get(Constants.WORKLOAD_STEP_PARSER).equals("false")) {
			return new DriverLogParser();
		}
		return new EmptyLogParser();
	}

}
