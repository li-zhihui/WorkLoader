package workload.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class showlinkpicServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		ArrayList<String> loglink = new ArrayList<String>();
		loglink = getdatlink(req.getSession().getServletContext().getRealPath("")+"\\LogPic");
		ArrayList<String> piclink = new ArrayList<String>();
		piclink = getpiclink(req.getSession().getServletContext().getRealPath("")+"\\LogPic");
     
        Map map = new HashMap();
        map.put("logList", loglink);
        map.put("imgList",piclink);
		req.setAttribute("workloadlist",map);
		System.out.println(map.toString());
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("showlinkpicServlet.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doPost(req, resp);
	}
	
	static public ArrayList<String> getdatlink(String filePath)
	{
		ArrayList<String> temp = new ArrayList<String>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for(File file:files)
		{
			if(file.getName().endsWith(".dat") || file.getName().endsWith(".csv"))
			{
				String[] splitArray = file.getName().replaceAll("\\\\", "/").split("/"); 
				temp.add(splitArray[splitArray.length-1]);
			}
		}
		
		return temp;
	}
	
	static public ArrayList<String> getpiclink(String filePath)
	{
		ArrayList<String> temp = new ArrayList<String>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for(File file:files)
		{
			if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))
			{
				String[] splitArray = file.getName().replaceAll("\\\\", "/").split("/"); 
				temp.add(splitArray[splitArray.length-1]);
			}
		}
		
		return temp;
	}
}
