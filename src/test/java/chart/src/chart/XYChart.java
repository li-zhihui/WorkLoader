package chart;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;



public class XYChart {
    private static final boolean SHOW_LEGEND = true;
    private static final boolean SHOW_TOOLTIPS = false;
    private static final boolean GENERATE_URLS = false;
    private static final int count = 7;
    private static int row = 1000;
    private final static int col = 20;
    private static int linenum = 0;//num of data instances
	double[][] data = new double[row][col];
	ArrayList<String> list = new ArrayList<String>();// store item name
	
	/***
	 * 
	 * read csv file and store the data in 2-D double matrix and item name in arraylist
	 * @throws IOException
	 */
    public  void readCSV() throws IOException {
    		FileReader fr = new FileReader("..\\..\\resources\\"+ "sr479_dstat.csv");
    		BufferedReader br = new BufferedReader(fr);
    		String line;
    		StringTokenizer st;
    		int i = 0;
    		for(int m = 0; m < row; m++)
    			for(int j = 0 ; j < col; j++)
    				data[m][j] = 0;
    		while(i < count){
    			line = br.readLine();
    			if(i == count-1){
    				st = new StringTokenizer(line, ",");  
    				for(int k = 0; k < col; k++)
    					list.add(st.nextToken());
    			}		
    			i++;
    		}	
    		i = 0; 
    		while(true){
    			line = br.readLine();
    			if (line == null) break;
    			st = new StringTokenizer(line, ",");  
    			for(int k = 0 ; k < col; k++){
    				data[i][k] = Double.parseDouble(st.nextToken());
    			}
    			i++;	
    		}
    		linenum = i;
    		br.close();
     }
        
    /***
     * 
     * @param start - start col in matrix data
     * @param num - number of series
     * @param name - name of chart
     * @param flag - distinguish between stackedareachart and linechart
     * @throws IOException
     */
    public void createChart(int start,int num,String name,boolean flag) throws IOException {
    	DefaultTableXYDataset dataset = new DefaultTableXYDataset();
    	for(int i = 0; i < num; i++){
	    	XYSeries s = new XYSeries(list.get(start+i),true,false);
	    	for(int j = 0; j < linenum ; j++)
	    			s.add(j*2,data[j][start+i]);
	    	dataset.addSeries(s);
    	}
    	JFreeChart chart;
    	if(flag == false){
    			chart = ChartFactory.createStackedXYAreaChart(
    	              name + " Utilization",      // chart title
    	              "time-line",                // domain axis label
    	              name,                   // range axis label
    	              dataset,                   // data
    	              PlotOrientation.VERTICAL,  // orientation
    	              SHOW_LEGEND ,                      // include legend
    	              SHOW_TOOLTIPS,
    	              GENERATE_URLS
    	        );
    			 chart.setBackgroundPaint(Color.white);
    	         final XYPlot plot = (XYPlot) chart.getPlot();
    	         plot.setForegroundAlpha(0.6f);
    	         final ValueAxis domainAxis = plot.getDomainAxis();
    	         domainAxis.setLowerMargin(0.0);
    	         domainAxis.setUpperMargin(0.0);
    	         //change the auto tick unit selection to integer units only...
    	         final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    	         rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    	         rangeAxis.setAutoRange(true);
    	         double max = 0;
    	         for(int i = 0 ; i < num; i++){
    	         	max += dataset.getYValue(i, linenum-1);
    	         }
    	         rangeAxis.setUpperBound(max);
    	}
    	else
	    	{
	    		chart = ChartFactory.createXYLineChart(
	    				name + " Throughput", 
	    				"time-line", 
	    				name, dataset,
	    				PlotOrientation.VERTICAL, 
	    				SHOW_LEGEND ,              
	    	            SHOW_TOOLTIPS,
	    	            GENERATE_URLS
	    	     );
	    		
	    		final XYPlot plot = (XYPlot) chart.getPlot();
	    		chart.setBackgroundPaint(Color.white);
	    		plot.setBackgroundPaint(Color.WHITE);
	    		plot.setDomainGridlinePaint(Color.gray);
    	        plot.setRangeGridlinePaint(Color.gray);
	    		XYItemRenderer renderer = plot.getRenderer();
	    		BasicStroke stroke = new BasicStroke(2f);
	    		for(int i = 0 ; i < num; i++)
	    			renderer.setSeriesStroke(i, stroke);//set the width of line
	    	}
    	
	         int width = 800;
	         int height = 350;
	         ChartUtilities.saveChartAsPNG( new File(name + ".jpg"), chart, width, height);
      }
        public static void main(final String[] args) throws IOException {
        	XYChart sac = new XYChart();
        	sac.readCSV();
        	sac.createChart(0, 4,"MEMORY",false);
        	sac.createChart(6, 6, "CPU",false);
        	sac.createChart(14, 2, "NETWORK ETH1", true);
        	sac.createChart(18, 2, "DISK", true);
        }

}
 