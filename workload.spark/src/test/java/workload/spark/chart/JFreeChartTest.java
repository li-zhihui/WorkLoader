package workload.spark.chart;



import workload.spark.BaseTestCase;
import workload.spark.run.XRunner;

public class JFreeChartTest extends BaseTestCase {


	public void testCreateChart() throws Exception {
		setUp();
		try {
			SparkChart chart = new XChart();
			XRunner runner = new XRunner();
			runner.run();
			chart.setCsvFolder(testDataFolder);
			chart.setJpgFolder(testDataFolder);
			chart.createChart();
		} catch (Exception e) {
			e.printStackTrace();
			super.assertTrue(false);
		}
	}
}
