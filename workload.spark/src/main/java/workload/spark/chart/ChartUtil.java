package workload.spark.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
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
import org.jfree.chart.annotations.XYTextAnnotation;
import java.awt.Font;
import org.jfree.ui.TextAnchor;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class ChartUtil {
	

	public static JFreeChart lineChart(ChartSource cs) {
		DefaultTableXYDataset dataset = createXYDataset(cs);
		JFreeChart chart = ChartFactory.createXYLineChart(cs.chartName.toUpperCase() + " THROUGHOUTPUT",
				cs.xAxisName, cs.yAxisName, dataset, PlotOrientation.VERTICAL, true,
				false, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		chart.setBackgroundPaint(Color.white);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.gray);
		plot.setRangeGridlinePaint(Color.gray);
		XYItemRenderer renderer = plot.getRenderer();
		for (int i = 0; i < dataset.getSeriesCount(); i++){
			renderer.setSeriesStroke(i, new BasicStroke(2f));// set the width of line 
			renderer.setSeriesPaint(i, Color.getHSBColor((float) (i+1)*(360/dataset.getSeriesCount())/360, 1, 1));
		}
		ValueAxis rangeAxis = plot.getRangeAxis();
		ValueAxis domainAxis = plot.getDomainAxis();
		rangeAxis.setLowerBound(0);
		domainAxis.setUpperBound(Math.max(Double.parseDouble(WorkloadConf.get(Constants.WORKLOAD_CHART_XMAX))
				, cs.dataList.size()*cs.freq + 2));
		paintMarker(cs.marker,chart,"XYPLOT");
		return chart;
	}
	
	private static void paintMarker(double[][] marker, JFreeChart chart,
			String type) {
		//FIXME FIND A SET OF DIFFERENT COLORS
		Paint[] paints = {Color.black,Color.darkGray,Color.gray,Color.lightGray};
		boolean flag;
		for(int k=0; k<marker.length; k++)
			if(marker[k] != null){
				for(int i=0; i<marker[k].length;i++){
					flag = false;
					if(k>0){
						for(int j=0; j<marker[k-1].length; j++)
							if(marker[k][i] == marker[k-1][j]){
								flag = true;
								break;
							}
					}
					if(flag == false){
						Marker m = new ValueMarker(marker[k][i],paints[k],
								new BasicStroke((float) ((marker.length-k)*0.5)), null, null, 0.6f);
						if(type.equals("CATEGORYPLOT"))
							chart.getCategoryPlot().addRangeMarker(m, Layer.FOREGROUND);
						else if(type.equals("XYPLOT"))
							chart.getXYPlot().addDomainMarker(m, Layer.FOREGROUND);
					}
				}
			}
	}

	public static JFreeChart stackChart(ChartSource cs) {
		DefaultTableXYDataset dataset = createXYDataset(cs);
		JFreeChart chart = ChartFactory.createStackedXYAreaChart(cs.chartName.toUpperCase() + " UTILIAZATION", cs.xAxisName, cs.yAxisName, 
				dataset,PlotOrientation.VERTICAL,true,false, false);
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setForegroundAlpha(0.6f);
		plot.setBackgroundPaint(Color.white);
		final ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLowerMargin(0.0);
		domainAxis.setUpperMargin(0.0);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRange(true);
		
		if(cs.yAxisName.equals("PERCENTAGE"))
			rangeAxis.setUpperBound(100);
		else{
			double max = 0;
			for (int i = 0; i < dataset.getSeriesCount(); i++) {
				max += dataset.getYValue(i,cs.dataList.size() - 2);
			}
			rangeAxis.setUpperBound(max);
		}
		domainAxis.setUpperBound(Math.max(Double.parseDouble(WorkloadConf.get(Constants.WORKLOAD_CHART_XMAX))
				, cs.dataList.size()*cs.freq + 2));
		paintMarker(cs.marker,chart,"XYPLOT");
		return chart;
	}
	
	public static JFreeChart ganttaChart(ChartSource cs) {
		CategoryDataset dataset = createCategoryDataset(cs);
		final JFreeChart chart = ChartFactory.createStackedBarChart(cs.chartName, 
				cs.xAxisName, // domain axis label
				cs.yAxisName, // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // the plot orientation
				false, // include legend
				false, // tooltips
				false // urls
				);
		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		final StackedBarRenderer renderer = (StackedBarRenderer) plot
				.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setSeriesPaint(0, Color.white);
		renderer.setSeriesPaint(2, Color.white);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setLowerBound(0);
		rangeAxis.setUpperBound(Math.max(Double.parseDouble(WorkloadConf.get(Constants.WORKLOAD_CHART_XMAX))
								, dataset.getValue(2, dataset.getColumnCount()-1).doubleValue()));
		if(cs.xAxisName.toLowerCase().equals(Constants.TASK_NAME)){
			CategoryAxis domainAxis = plot.getDomainAxis();
			domainAxis.setTickLabelsVisible(false);
		}
		paintMarker(cs.marker,chart,"CATEGORYPLOT");
		return chart;
	}
	
	private static CategoryDataset createCategoryDataset(ChartSource cs){
		int count = cs.dataList.get(0).size();
		String[] column = new String[count];
		for (int i = 0; i < count; i++) {
			column[i] = cs.xAxisName + " " + i;
		}
		String[] row = new String[3];
		row[0] = "Start";
		row[1] = "Duration";
		row[2] = "End";
		double[][] newData = new double[3][count];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < count; j++) {
				newData[i][j] = cs.dataList.get(i).get(j) / 1000;
			}
		}
		return DatasetUtilities.createCategoryDataset(row,column, newData);
	}
	
	private static DefaultTableXYDataset createXYDataset(ChartSource cs){
		DefaultTableXYDataset dataset = new DefaultTableXYDataset();
		for(int i=0; i < cs.dataList.get(0).size();i++){
			XYSeries s = new XYSeries(cs.dataNameList.get(i),true,false);
			for(int j = 0 ; j < cs.dataList.size();j++){
				s.add(j*cs.freq,cs.dataList.get(j).get(i));
			}
			dataset.addSeries(s);
		}
		return dataset;
	}
	
	
//	public static void addAnnoLabels(double[] XValue, XYPlot plot, double YValue){
//		for(int i=0; i<XValue.length;i++){
//			//the annotation can sometimes overlap
//			XYTextAnnotation label = new XYTextAnnotation(Constants.JOB_NAME
//					+ i, XValue[i], YValue);
//			label.setRotationAnchor(TextAnchor.TOP_CENTER);
//			label.setTextAnchor(TextAnchor.TOP_CENTER);
//			label.setPaint(Color.black);
//			label.setFont(new Font("TimesRoman", Font.BOLD, 15));
//			plot.addAnnotation(label);
//		}
//	}
	
	
}
