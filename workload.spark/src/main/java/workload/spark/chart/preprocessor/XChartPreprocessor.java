package workload.spark.chart.preprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.des.CommandDes;
import workload.spark.des.Regex;

public class XChartPreprocessor{

	/**
	 * Read Log File and store it as 4-D list as required by the command des
	 */
	String csvFolder;
	
	public void setCSVFolder(String csvFolder){
		this.csvFolder = csvFolder;
	}
	List<Map<String,List<List<String>>>> dataList = new ArrayList<Map<String,List<List<String>>>>();
	
	private void readFile(CommandDes des,String currentSlave) throws Exception{
		System.out.println(csvFolder + currentSlave+"_" + des.getCommandName()+".dat");
		FileReader fr = new FileReader(csvFolder + currentSlave+"_" + des.getCommandName()+".dat");
		BufferedReader br = new BufferedReader(fr);
		String line;
		int i = 0; 
		while(i < des.getStartSkip()){
			br.readLine();
			i++;
		}
//		long startTime = JSTChart.getStartTime();
		
//		long time = (Long) WorkloadContext.get(Constants.WORKLOAD_RUNTIME) - startTime;
//		//discard records preceding the start of job
//		if(time < 0){
//			long count = time / Integer.parseInt((WorkloadConf.get(Constants.WORKLOAD_RUNNER_FREQUENCY)));
//			i = 0;
//			while( i < count){
//				br.readLine();
//				i++;
//			}	
//		}
		line = br.readLine();
		while(true){
			if(line.equals("")){
				Map<String,List<List<String>>> map = new HashMap<String,List<List<String>>>();
				for(int m = 0; m < des.getGroupDes().size(); m++){
					List<List<String>> groupTable = new ArrayList<List<String>>();
					Regex regex = des.getGroupDes().get(m).getRegex();
				  	String regexValue = des.getGroupDes().get(m).getRegexValue();
				  	line = br.readLine();
				  	if(checkRegex(line,regex,regexValue)){
				  			line = br.readLine();
				  			while(line != null&& !line.equals("") ){
				  				//System.out.println("Valid Line:" + line);
				  				List<String> record = Util.getList(line, Constants.DATA_SPLIT);
				  				line = br.readLine();
				  				groupTable.add(record);
				  			}
				  	}
				  	else{
				  		System.out.println("Wrong Regex: " + regexValue);
				  		return;
				  	}
				  	map.put(des.getGroupDes().get(m).getGroupName(), groupTable);
				}
				dataList.add(map);
			}
			else if(des.getGroupDes().get(0).getGroupName().equals("null")){
				Map<String,List<List<String>>> map = new HashMap<String,List<List<String>>>();
				List<List<String>> table = new ArrayList<List<String>>();
				List<String> record = Util.getList(line, Constants.DATA_SPLIT);
				table.add(record);
				map.put("null",table);
				line = br.readLine();
				dataList.add(map);
			}
			else{
				System.out.println("Cannot process the file format");
				return;
			}
			if(line == null)
				break;
		}
		br.close();	
	}
	
	private boolean checkRegex(String line, Regex regex, String regexValue) {
		//System.out.println(regex);
		if(line.indexOf(regexValue)!= -1)
			return true;
		else if(regex.equals(Regex.startWith)&&line.startsWith(regexValue))
			return true;
		return false;
	}
	
	public  List<Map<String, List<List<String>>>> getDataList(CommandDes cd) throws Exception{
//		String[] command = WorkloadConf.get(Constants.WORKLOAD_RUNNER_COMMAND).split(Constants.DATA_SPLIT);
//		for(int k = 0 ; k < command.length; k++){
			//CommandDes cd = (CommandDes) WorkloadContext.get(command[k]);
//			List<String> slaves = Util.getSlavesHost();
//			for (String slave : slaves) {
				readFile(cd, "sr479");
				System.out.println("dataList Size: " + dataList.size());
				for(int i = 0; i < dataList.size(); i++){
					Map<String,List<List<String>>> map = dataList.get(i);
					for(int j = 0 ; j < cd.getGroupDes().size(); j++){
						System.out.println("Group Name: " + cd.getGroupDes().get(j).getGroupName());
						List<List<String>> table = map.get(cd.getGroupDes().get(j).getGroupName());
						for(int m = 0; m < table.size(); m++){
							List<String> record = table.get(m);
							for(int n = 0 ; n < table.get(0).size(); n++)
								System.out.print(record.get(n) + " ");
							System.out.println();
						}
					}
				}
//			}
//		}
		return dataList;
	}
}

