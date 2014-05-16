package workload.spark.data.parser;

import workload.spark.BaseTestCase;

import com.intel.spark.log.DriverlogMain;
public class DriverLogParserTest extends BaseTestCase {
	String filename = "/home/liyezhan/work/zhihui-github-workloader/workload.spark/src/test/logParser/billboardlog.log";

	public void testDriverLogParser() throws Exception {
		DriverlogMain.processFile(filename);
	}


}
