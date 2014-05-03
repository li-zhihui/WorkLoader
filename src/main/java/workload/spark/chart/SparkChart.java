package workload.spark.chart;

import java.io.IOException;

/**
 * Read CSV files, then output JPG files.
 * 
 */
public abstract class SparkChart {
	String csvFolder;
	String jpgFolder;

	public abstract void createChart() throws IOException;

	public void setCsvFolder(String csvFolder) {
		this.csvFolder = csvFolder;
	}

	public void setJpgFolder(String jpgFolder) {
		this.jpgFolder = jpgFolder;
	}
}
