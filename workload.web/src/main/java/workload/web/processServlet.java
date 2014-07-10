package workload.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class processServlet extends HttpServlet
{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String result;
		System.out.println("set result done");
		
		String workloadname = req.getParameter("workloadname");
		
		String[] cmd = {"../run.sh",workloadname};
		BufferedReader br=null;   
        try 
        {   
            Process proc=Runtime.getRuntime().exec(cmd);   
            br=new BufferedReader(new InputStreamReader(proc.getInputStream()));   
            @SuppressWarnings("unused")   
            String line=null;   
            System.out.println("打印所有正在运行的进程信息");   
            //create a process
            while((line=br.readLine())!=null)
            {   
                System.out.println(br.readLine());   
            }
            result="done";
        } 
        catch (IOException e) 
        {   
            e.printStackTrace();
            result="error";
        }
        finally
        {   
            if(br!=null)
            {   
                try 
                {   
                    br.close();   
                } 
                catch (Exception e) 
                {   
                    e.printStackTrace();   
                    result="error";
                }   
            }   
        }
        resp.getWriter().write(result);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doPost(req, resp);
	}

}
