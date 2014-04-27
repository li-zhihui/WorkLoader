package chart;



import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
 
public class gantaChart{
	 private static final boolean SHOW_LEGEND = false;
	 private static final boolean SHOW_TOOLTIPS = false;
	 private static final boolean GENERATE_URLS = false;
    public static void main(String[] args) throws IOException {
    	createGanttchart("JOB");
    	createGanttchart("TASK");
    	createGanttchart("STAGE"); 
    }
     
 
    /** 
     *
     * @name of csv file
     * @throws IOException 
     */
    private static void createGanttchart(String name) throws IOException {
    	 FileReader fr = new FileReader("..\\..\\resources\\" + name + ".csv");
         BufferedReader br = new BufferedReader(fr);
         TaskSeries s1 = new TaskSeries(name + "_TIME");
         String line;
         StringTokenizer st;
         String id,start,end;
         int i = 1 ;
         while (true) {
             line = br.readLine();
             if (line == null) break;
             st = new StringTokenizer(line, ",");  
             st.nextToken();
             if(name.compareTo("JOB") == 0);
             else if(name.compareTo("STAGE") == 0)st.nextToken();
             else if(name.compareTo("TASK") == 0){
            	 st.nextToken();
            	 st.nextToken();
             }
             id = name +" "+ i;
             i++;
             StringTokenizer st1 = new StringTokenizer(st.nextToken(),".");
             start = st1.nextToken();
             st1 = new StringTokenizer(st.nextToken(),".");
             end = st1.nextToken();
             //System.out.println(Long.parseLong(start)+ "\t" + Long.parseLong(end));
             Task t = new Task(id,new Date(Long.parseLong(start)),new Date(Long.parseLong(end)) );
             s1.add(t);
         }
         br.close();
         
         TaskSeriesCollection dataset = new TaskSeriesCollection();
         dataset.add(s1);
         JFreeChart chart = ChartFactory.createGanttChart(name +"_TIME", name+"_ID", "TIME",dataset,SHOW_LEGEND, SHOW_TOOLTIPS, GENERATE_URLS);
         CategoryPlot plot=chart.getCategoryPlot();
         plot.setBackgroundPaint(Color.white);
	     plot.setRangeGridlinePaint(Color.gray);
         if(name.compareTo("TASK") == 0){
           CategoryAxis domainAxis=plot.getDomainAxis();
           domainAxis.setTickLabelsVisible(false);
         }
         DateAxis da = (DateAxis)plot.getRangeAxis(0);
         da.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 20, new SimpleDateFormat("mm:ss")));
         //undone:unify the time-line
         int width = 800;
         int height = 350;
         ChartUtilities.saveChartAsPNG( new File(name + ".jpg"), chart, width, height);
    }

}