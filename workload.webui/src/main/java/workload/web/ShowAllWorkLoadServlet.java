package workload.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowAllWorkLoadServlet extends HttpServlet
{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		File path = new File(req.getSession().getServletContext().getRealPath("")+"\\LogPic");
		deleteFolder(path);
		
		ArrayList<String> workloadlist =  new ArrayList<String>();
		workloadlist.add("nweight");
		
		req.setAttribute("workloadlist", workloadlist);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("workloadchoose.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doPost(req, resp);
	}
	
	static public void deleteFolder(File dir)
	{
		File filelist[] = dir.listFiles();
		int listlen = filelist.length;
		for (int i = 0; i < listlen; i++)
		{
			if (filelist[i].isDirectory())
			{
				deleteFolder(filelist[i]);
			}
			else
			{
				filelist[i].delete();
			}
		}
	}

}
