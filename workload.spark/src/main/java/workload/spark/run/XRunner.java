package workload.spark.run;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.WorkloadConf;
import workload.spark.WorkloadContext;
import workload.spark.des.ChartDes;
import workload.spark.des.ChartGroupByType;
import workload.spark.des.ChartType;
import workload.spark.des.CommandDes;
import workload.spark.des.GroupDes;
import workload.spark.des.Regex;
public class XRunner extends Runner {

	/**
	 * build drun.sh
	 * read describe file to wordconfContext as command object
	 * @throws Exception 
	 */
	private void  prepareDRunSh() throws Exception{
		BufferedWriter br = new BufferedWriter(new FileWriter(new File(WorkloadConf.get(Constants.WORKLOAD_WORKDIR)
				+ "/drun_wl.sh ")));
		String str = WorkloadConf.get(Constants.WORKLOAD_RUNNER_COMMAND);
		String []command =  str.split(Constants.DATA_SPLIT);
		if(command.length == 1 && command[0].equals("")){
			System.out.println("NO COMMAND CONFIGURED");
		}
		else{
			Properties des = Util.buildProperties(WorkloadConf.get(Constants.WORKLOAD_WORKDIR) + "conf/runner.des");
			StringBuffer sb = new StringBuffer("");
			List<String> slaves = Util.getSlavesHost();
			sb.append("#!/bin/bash\n\n");
			//get slaves name
			sb.append("TARGET=(");
			for(int i=0 ; i < slaves.size(); i++){
				sb.append("\"" + slaves.get(i)+"\"");
				if( i < slaves.size()-1) 
					sb.append(" ");
			}
			sb.append(")\n\n");
			sb.append("for worker in ${TARGET[@]}\n"+ "do\n");
			for(int i = 0; i < command.length; i++){
				//FIXME Any better solution to output dstat
				if(command[i].equals(Constants.DSTAT_COMMAND))
					sb.append(des.getProperty(command[i]+".command") + " ${worker}_" + command[i] + ".dat " + WorkloadConf.get(Constants.WORKLOAD_RUNNER_FREQUENCY)
							+ " > " + "/dev/null &\n");
				else{
					sb.append(des.getProperty(command[i]+".command") + " " + WorkloadConf.get(Constants.WORKLOAD_RUNNER_FREQUENCY)
							+ " > " + "${worker}_" + command[i] + ".dat &\n");
				}
				//READ DES FILE AND WRAP INTO COMMANDDES OBJECT
				CommandDes cd = new CommandDes();
				List<GroupDes> groupDeses = new ArrayList<GroupDes>();
				List<ChartDes> chartDeses = new ArrayList<ChartDes>();
				cd.setStartSkip(Integer.parseInt(des.getProperty(command[i] + ".startSkip" )));
				cd.setCommandName(command[i]);
				String groupContent = des.getProperty(command[i]+".datagroup");
				if(groupContent.equals("null")){
					GroupDes gd = new GroupDes();
					gd.setGroupName("null");
					List<String> headDes = Util.getList(des.getProperty(command[i]+".datagroup.head"),",");
					gd.setHeadDes(headDes);
					gd.setSplit(des.getProperty(command[i]+".datagroup.split"));
					groupDeses.add(gd);
				}
				else{
					String[] group = groupContent.split("]");
					for(int k = 0; k < group.length; k++){
						GroupDes gd = new GroupDes();
						groupInfoHandler(gd,group[k].substring(1));
						List<String> headDes = Util.getList(des.getProperty(command[i]+".datagroup."+gd.getGroupName()+".head"),",");
						gd.setHeadDes(headDes);
						gd.setSplit(des.getProperty(command[i]+".datagroup."+gd.getGroupName()+".split"));
						groupDeses.add(gd);
					}
				}
				String[] chartList = des.getProperty(command[i] + ".chart.list").split(",");
				for(int m = 0; m < chartList.length; m++){
					ChartDes chd = new ChartDes();
					chd.setChartName(chartList[m]);
					String chartContent = des.getProperty(command[i] + ".chart." + chartList[m]);
					String chartInfo;
					if(chartContent.startsWith("group")){
						String[] chartItem = chartContent.split(";");
						if(chartItem.length != 2){
							System.out.println("Wrong Chart DES");
							System.exit(1);
						}
						chartInfo = chartItem[0];
						String groupInfo = chartItem[1];
						chartGroupInfoHandler(chd,groupInfo);
					}
					else
						chartInfo = chartContent;
					chartInfoHandler(chd,chartInfo);
					chartDeses.add(chd);
				}
				cd.setGroupDeses(groupDeses);
				cd.setChartDeses(chartDeses);
				WorkloadContext.put(command[i],cd);
			}
			sb.append("done\n");
			String workloadConfRun = Constants.WORKLOAD_CONF_PREFIX + "."
					+ WorkloadConf.get(Constants.WORKLOAD_NAME) + "."
					+ Constants.WORKLOAD_RUN_SUFFIX;
			String logFile = Util.getLogFileName();
			sb.append("./" + WorkloadConf.get(workloadConfRun) + " > " + logFile + " 2>&1\n");
			sb.append("sleep "+ WorkloadConf.get(Constants.WORKLOAD_RUNNER_SLEEPTIME)+"\n");
			//FIXME Should the kill command be in the des file
			sb.append("for worker in ${TARGET[@]}\n"
					+ "do\n");
			for(int i = 0; i < command.length; i++){
				sb.append("ssh $worker ps aux | grep -i " + "\"" + command[i] + "\"" 
						+ " | grep -v \"grep\" | awk '{print $2}' | xargs ssh $worker kill -9\n");
			}
			sb.append("done\n");
			br.write(sb.toString());
		}	
		br.close();
	}
	
	private void groupInfoHandler(GroupDes gd,String info){
		String[] groupItem =info.split(":");
		if(groupItem.length != 2){
			System.out.println("Wrong Group Format");
			System.exit(1);
		}
		gd.setGroupName(groupItem[0].split(",")[0]);
		//FIXME indexOf??
		gd.setRegex(Regex.startWith);
		gd.setRegexValue(groupItem[1]);
	}
	private void chartGroupInfoHandler(ChartDes chd, String groupInfo) {
		String groupType = groupInfo.split(" ")[0];
		if(groupType.endsWith("Row"))
				chd.setChartGroupByType(ChartGroupByType.row);
		else if(groupType.endsWith("Col"))
			chd.setChartGroupByType(ChartGroupByType.col);
		chd.setGroupByName(groupInfo.split(" ")[1].split(":")[0]);
		chd.setGroupByValue(groupInfo.split(" ")[1].split(":")[1]);
	}

	private void chartInfoHandler(ChartDes chd, String chartInfo) {
		if(chartInfo.split(":")[0].equalsIgnoreCase("line"))
			chd.setChartType(ChartType.line);
		else if (chartInfo.split(":")[0].equalsIgnoreCase("line"))
			chd.setChartType(ChartType.stack);
		chd.setColName(Util.getList(chartInfo.split(":")[1], ","));
	}

	/**
	 * copy drun.sh to workload folder
	 * @throws Exception 
	 */
	private void copyDRun2WorkloadPath() throws Exception{
		String drunCp = "/bin/cp "
				+ WorkloadConf.get(Constants.WORKLOAD_WORKDIR)
				+ "/drun_wl.sh " + Util.getWorkloadPath();
		Runtime runtime = Runtime.getRuntime();
		String[] cpCmd = { "/bin/sh", "-c", drunCp };
		System.out.println(drunCp);
		if (runtime.exec(cpCmd).waitFor() != 0) {
			throw new Exception("cp drun_wl.sh failed.");
		}
	}
	
	
	
	@Override
	public void run() throws Exception {
		prepareDRunSh();
//		copyDRun2WorkloadPath();
//		String runsh = Util.getWorkloadPath() + "drun_wl.sh ";
//		Runtime runtime = Runtime.getRuntime();
//		String[] runCmd = { "/bin/sh", "-c", runsh };
//		System.out.println(runsh);
//		if (runtime.exec(runCmd, null, new File(Util.getWorkloadPath())).waitFor() != 0) {
//			throw new Exception("drun failed.");
//		}
		//FIXME record the start time??
		WorkloadContext.put(Constants.WORKLOAD_RUNTIME, System.currentTimeMillis());
	}

}
