package workload.spark.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.Layer;
import org.jfree.ui.TextAnchor;

import workload.spark.Constants;
import workload.spark.Util;

//FIXME please extract all local String val into Constants.
public class JFreeSparkChart extends SparkChart {

	// FIXME fixed row & col?
	private static final int count = 7;
	private static int row = 2000;
	private final static int col = 20;
	private int linenum = 0;// num of data instances
	private String currentSlave;
	double[][] data = new double[row][col];
	ArrayList<String> list = new ArrayList<String>();// store item name
	double offset = 0.0;

	/***
	 * 
	 * read csv file and store the data in 2-D double matrix and item name in
	 * arraylist
	 * 
	 * @throws IOException
	 */
	public void readCSV() throws IOException {
		FileReader fr = new FileReader(csvFolder + currentSlave
				+ Constants.DSTAT_FILE);
		BufferedReader br = new BufferedReader(fr);
		String line;
		StringTokenizer st;
		int i = 0;
		for (int m = 0; m < row; m++)
			for (int j = 0; j < col; j++)
				data[m][j] = 0;
		while (i < count) {
			line = br.readLine();
			if (i == count - 1) {
				st = new StringTokenizer(line, Constants.DATA_SPLIT);
				for (int k = 0; k < col; k++)
					list.add(st.nextToken());
			}
			i++;
		}
		i = 0;
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			st = new StringTokenizer(line, Constants.DATA_SPLIT);
			for (int k = 0; k < col; k++) {
				data[i][k] = Double.parseDouble(st.nextToken());
			}
			i++;
		}
		linenum = i;
		br.close();
	}

	public void createCatogoryChart(String name) throws IOException {
		FileReader fr = new FileReader(csvFolder + name + Constants.CSV_SUFFIX);
		BufferedReader br = new BufferedReader(fr);
		String line;
		StringTokenizer st;
		Double start, end;
		ArrayList<String> list = new ArrayList<String>();
		double[][] tempData = new double[3][row];
		int count = 0;
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			st = new StringTokenizer(line, Constants.DATA_SPLIT);
			st.nextToken();
			if (name.compareTo(Constants.JOB_NAME) == 0)
				;
			else if (name.compareTo(Constants.STAGE_NAME) == 0)
				st.nextToken();
			else if (name.compareTo(Constants.TASK_NAME) == 0) {
				st.nextToken();
				st.nextToken();
			}
			list.add(name + " " + count);
			StringTokenizer st1 = new StringTokenizer(st.nextToken(), ".");
			start = Double.parseDouble(st1.nextToken().trim());// in case of the
			// inputFormat
			// exception
			st1 = new StringTokenizer(st.nextToken(), ".");
			end = Double.parseDouble(st1.nextToken().trim());

			start = start / 1000;
			end = end / 1000;

			if (count == 0)
				offset = start;
			tempData[0][count] = start - offset;
			tempData[1][count] = end - start;
			tempData[2][count] = end - offset;
			count++;
		}
		String[] column = new String[count];
		for (int i = 0; i < count; i++) {
			column[i] = list.get(i);
		}
		String[] row = new String[3];
		row[0] = "Start";
		row[1] = "Duration";
		row[2] = "End";
		double[][] newData = new double[3][count];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < count; j++) {
				newData[i][j] = tempData[i][j];
			}
		}
		br.close();
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(row,
				column, newData);
		final JFreeChart chart = ChartFactory.createStackedBarChart(name
				+ "-TIME", name, // domain axis label
				"TIME", // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // the plot orientation
				false, // include legend
				false, // tooltips
				false // urls
				);

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		final StackedBarRenderer renderer = (StackedBarRenderer) plot
				.getRenderer();
		renderer.setSeriesPaint(0, Color.white);
		renderer.setSeriesPaint(2, Color.white);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		// insert gridline
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setLowerBound(0);
		rangeAxis.setUpperBound(linenum * 2);
		double[] marker = findTimeEnd(Constants.JOB_NAME);
		for (int i = 0; i < marker.length; i++) {
			Marker m = new ValueMarker(marker[i], Color.gray,
					new BasicStroke(1), null, null, 0.5f);
			plot.addRangeMarker(m, Layer.FOREGROUND);
		}
		double[] marker1 = findTimeEnd(Constants.STAGE_NAME);
		BasicStroke dashedStroke = new BasicStroke(1.0f,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
				new float[] { 5.0f, 5.0f }, 0.0f);
		for (int i = 0; i < marker1.length; i++) {
			Marker m = new ValueMarker(marker1[i] / 1000, Color.gray,
					dashedStroke, null, null, 0.5f);
			plot.addRangeMarker(m, Layer.FOREGROUND);
		}
		if (name.compareTo(Constants.TASK_NAME) == 0) {
			CategoryAxis domainAxis = plot.getDomainAxis();
			domainAxis.setTickLabelsVisible(false);
		}
		int width = 800;
		int height = 350;
		ChartUtilities.saveChartAsPNG(new File(jpgFolder + name
				+ Constants.GRAPH_SUFFIX), chart, width, height);
	}

	/***
	 * @param start
	 *            - start col in matrix data
	 * @param num
	 *            - number of series
	 * @param flag
	 *            - distinguish between stackedareachart and linechart * @param
	 *            name - name of chart
	 * @throws IOException
	 */
	public void createChart(int start, int num, boolean flag, String name)
			throws IOException {
		JFreeChart chart;
		DefaultTableXYDataset dataset = new DefaultTableXYDataset();
		for (int i = 0; i < num; i++) {
			XYSeries s = new XYSeries(list.get(start + i), true, false);
			int j = 0;
			for (j = 0; j < linenum; j++) {
				s.add(j * 2, data[j][start + i]);
			}
			dataset.addSeries(s);
		}
		final XYPlot plot;
		if (flag == true) {
			chart = ChartFactory.createStackedXYAreaChart(
					name + " Utilization", // chart title
					"", // domain axis label
					name, // range axis label
					dataset, // data
					PlotOrientation.VERTICAL, // orientation
					true, // include legend
					false, false);
			chart.setBackgroundPaint(Color.white);
			plot = (XYPlot) chart.getPlot();
			plot.setForegroundAlpha(0.6f);
			final ValueAxis domainAxis = plot.getDomainAxis();
			domainAxis.setLowerMargin(0.0);
			domainAxis.setUpperMargin(0.0);
			// change the auto tick unit selection to integer units only...
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setAutoRange(true);
			double max = 0;
			for (int i = 0; i < dataset.getSeriesCount(); i++) {
				max += dataset.getYValue(i, linenum - 1);
			}
			rangeAxis.setUpperBound(max);
		} else {
			chart = ChartFactory.createXYLineChart(name + " Throughput",
					"time-line", name, dataset, PlotOrientation.VERTICAL, true,
					false, false);

			plot = (XYPlot) chart.getPlot();
			chart.setBackgroundPaint(Color.white);
			plot.setBackgroundPaint(Color.WHITE);
			plot.setDomainGridlinePaint(Color.gray);
			plot.setRangeGridlinePaint(Color.gray);
			XYItemRenderer renderer = plot.getRenderer();
			BasicStroke stroke = new BasicStroke(2f);
			for (int i = 0; i < num; i++)
				renderer.setSeriesStroke(i, stroke);// set the width of line

		}
		double[] marker = findTimeEnd(Constants.JOB_NAME);
		ValueAxis rangeAxis = plot.getRangeAxis();
		for (int i = 0; i < marker.length; i++) {
			Marker m = new ValueMarker(marker[i], Color.gray,
					new BasicStroke(1), null, null, 0.5f);
			XYTextAnnotation label = new XYTextAnnotation(Constants.JOB_NAME
					+ i, marker[i], rangeAxis.getUpperBound());
			label.setRotationAnchor(TextAnchor.TOP_CENTER);
			label.setTextAnchor(TextAnchor.TOP_CENTER);
			label.setPaint(Color.black);
			// FIXME invisible code at my macbook.
			label.setFont(new Font("����", Font.PLAIN, 10));
			plot.addDomainMarker(m, Layer.FOREGROUND);
			plot.addAnnotation(label);
		}
		double[] marker1 = findTimeEnd(Constants.STAGE_NAME);
		BasicStroke dashedStroke = new BasicStroke(1.0f,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
				new float[] { 5.0f, 5.0f }, 0.0f);
		for (int i = 0; i < marker1.length; i++) {
			Marker m = new ValueMarker(marker1[i] / 1000, Color.gray,
					dashedStroke, null, null, 0.5f);
			plot.addDomainMarker(m, Layer.FOREGROUND);
		}
		plot.setDomainGridlinesVisible(false);
		int width = 800;
		int height = 350;
		ChartUtilities.saveChartAsPNG(new File(jpgFolder + currentSlave + "_"
				+ name + Constants.GRAPH_SUFFIX), chart, width, height);
	}

	/**
	 * @param name
	 *            of file
	 * @return double array with job and stage end time
	 * @throws IOException
	 *             *
	 * 
	 */
	public double[] findTimeEnd(String name) throws IOException {
		FileReader fr = new FileReader(csvFolder + name + Constants.CSV_SUFFIX);
		BufferedReader br = new BufferedReader(fr);
		Long end, duration = (long) 0;
		String line;
		StringTokenizer st;
		int n = 0;
		ArrayList<Long> list = new ArrayList<Long>();
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			st = new StringTokenizer(line, Constants.DATA_SPLIT);
			st.nextToken();
			if (name.compareTo(Constants.STAGE_NAME) == 0)
				st.nextToken();
			if (n == 0)
				offset = Long.parseLong(st.nextToken().trim());
			end = Long.parseLong(st.nextToken().trim());
			duration = (long) (end - offset);
			list.add(duration);
			n++;
		}
		br.close();
		double[] timeend = new double[n];
		for (int i = 0; i < n; i++)
			timeend[i] = list.get(i);
		return timeend;
	}

	@Override
	public void createChart() throws Exception {
		List<String> slaves = Util.getSlavesHost();
		for (String slave : slaves) {
			currentSlave = slave;
			readCSV();
			createChart(0, 4, true, "MEMORY");
			createChart(6, 6, true, "CPU");
			createChart(14, 2, false, "NETWORK");
			createChart(18, 2, false, "DISK");
		}
		createCatogoryChart(Constants.JOB_NAME);
		createCatogoryChart(Constants.STAGE_NAME);
		createCatogoryChart(Constants.TASK_NAME);
	}

}
