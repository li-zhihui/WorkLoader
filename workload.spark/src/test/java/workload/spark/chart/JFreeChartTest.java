package workload.spark.chart;

import workload.spark.BaseTestCase;

public class JFreeChartTest extends BaseTestCase {

	SparkChart chart = new JFreeSparkChart();

	public void testCreateChart() {
		try {
			chart.setCsvFolder(testDataFolder);
			chart.setJpgFolder(testDataFolder);
			chart.createChart();
		} catch (Exception e) {
			e.printStackTrace();
			super.assertTrue(false);
		}
	}
}
