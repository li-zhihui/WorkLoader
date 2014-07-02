package workload.spark.chart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jfree.chart.JFreeChart;

import workload.spark.Constants;

public class JSTChart extends SparkChart{
	/***
	 * generate job,stage,task graph and set job and stage bound as a double array
	 */
	List<List<Double>> jobData;
	List<List<Double>> stageData;
	List<List<Double>> taskData;
	static double[] jobMarker = null;
	static double[] stageMarker = null;
	static Long startTime;
	
	
	public double[] findTimeEnd(List<List<Double>> list){
		int n = list.get(0).size();
		double[] timeend = new double[n];
		for (int i = 0; i < n ; i++){
				StringTokenizer st = new StringTokenizer(list.get(2).get(i).toString(),".");
				String end = st.nextToken();
				timeend[i] = (Long.parseLong(end)/1000);
		}
		return timeend;
	}
	
	public List<List<Double>> loadJSTCSV(String name,int sortCriteria) throws Exception{
		//sortCSVFile(name,sortCriteria);
		FileReader fr = new FileReader(csvFolder + name + Constants.CSV_SUFFIX);
		BufferedReader br = new BufferedReader(fr);
		List<List<Double>> tempData = new ArrayList<List<Double>>(3);
		List<Double> startList = new ArrayList<Double>();
		List<Double> durationList = new ArrayList<Double>();
		List<Double> endList = new ArrayList<Double>();
		String line;
		StringTokenizer st;
		Double start, end;
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
			StringTokenizer st1 = new StringTokenizer(st.nextToken(), ".");
			start = Double.parseDouble(st1.nextToken().trim());
			st1 = new StringTokenizer(st.nextToken(), ".");
			end = Double.parseDouble(st1.nextToken().trim());
			startList.add(start - startTime);
			durationList.add(end - start);
			endList.add(end - startTime);
		}
		br.close();
		tempData.add(startList);
		tempData.add(durationList);
		tempData.add(endList);
		return tempData;
	}
	
	public long setStartTime() throws Exception {
		//sortCSVFile(Constants.JOB_NAME, 2);
		FileReader fr = new FileReader(csvFolder + Constants.JOB_NAME + Constants.CSV_SUFFIX);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while (line == "") {
			line = br.readLine();
			if (line == null) {
				throw new Exception("nothing in job.csv.");
			}
		}
		br.close();
		StringTokenizer st = new StringTokenizer(line, Constants.DATA_SPLIT);
		st.nextToken();
		String start = st.nextToken();
		br.close();
		return Long.parseLong(start.trim());
	}

//	 * sort filename.csv according to the column indexed at sortCriteria
//	 * 
//	 * @param filename
//	 * @param sortCriteria
//	 * @throws Exception
//	 */
//	public void sortCSVFile(String filename, int sortCriteria) throws Exception {
//		String taskSort = "sort -t \",\" -g -k " + Integer.toString(sortCriteria) + 
//				" " + csvFolder + filename + Constants.CSV_SUFFIX + 
//				" > " + csvFolder + "sorttmp" + Constants.CSV_SUFFIX;
//		Util.runCmd(taskSort);
//		String taskCp = "/bin/cp " + csvFolder + "sorttmp" + Constants.CSV_SUFFIX
//				+ " " + csvFolder + filename + Constants.CSV_SUFFIX;
//		Util.runCmd(taskCp);
//	}
	public static long getStartTime(){
		return startTime;
	}
	
	public void getChartBound() throws Exception{
		startTime = setStartTime();
		jobData = loadJSTCSV(Constants.JOB_NAME,2);
		stageData = loadJSTCSV(Constants.STAGE_NAME,3);
		taskData = loadJSTCSV(Constants.TASK_NAME,4);
		jobMarker = findTimeEnd(jobData);
		stageMarker = findTimeEnd(stageData);
		JFreeChart chart = ChartUtil.ganttaChart(new ChartSource(jobData, null,freq,Constants.JOB_NAME.toUpperCase()+"-TIME",Constants.JOB_NAME.toUpperCase(), "TIME(s)",jobMarker, stageMarker));
		outputGraph(Constants.JOB_NAME,chart,width,height);
		chart = ChartUtil.ganttaChart(new ChartSource(stageData, null,freq,Constants.STAGE_NAME.toUpperCase()+"-TIME",Constants.STAGE_NAME.toUpperCase(),"TIME(s)",jobMarker, stageMarker));
		outputGraph(Constants.STAGE_NAME,chart,width,height);
		chart = ChartUtil.ganttaChart(new ChartSource(taskData, null,freq,Constants.TASK_NAME.toUpperCase()+"-TIME",Constants.TASK_NAME.toUpperCase(),"TIME(s)",jobMarker, stageMarker));
		outputGraph(Constants.TASK_NAME,chart,width,height);
	}
	@Override
	public void createChart() throws Exception {
		
	}
}
