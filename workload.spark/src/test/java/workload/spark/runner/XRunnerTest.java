package workload.spark.runner;


import workload.spark.BaseTestCase;
import workload.spark.run.XRunner;


public class XRunnerTest extends BaseTestCase {

	public void testXRunner() throws Exception{
		setUp();
		XRunner runner = new XRunner();
		runner.run();
		//runner.testCommand();
	}

}
