<%@ page language="java"  import="java.util.*" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>Insert title here</title>
		<script src="js/jquery-1.9.1.js"></script>
		<script type="text/javascript">
		function startWork(){
			$("input").attr("disabled",true);
			var select = document.getElementById("workloadname");
			var index = select.selectedIndex; // Ñ¡ÖÐË÷Òý
			var value = select.options[index].value; // Ñ¡ÖÐÖµ
			$.ajax({
	             type: "GET",
	             url: "showProcessServlet?workloadname="+value,
	             success: function(data){
	                         if(data == "done")
	                         {
	                         $("input").attr("disabled",false);
	                         document.forms[0].action="showlinkpicServlet";
	                 		 document.forms[0].submit();
	                 		 }  
	                      }
	         });
		}
		</script>
		
	</head>
	<body>
		Please choose the WorkLoad which you want to run
		<br>
		
		<% ArrayList<String> workloadlist = new ArrayList<String>(); 
		   workloadlist = (ArrayList)request.getAttribute("workloadlist");
		%>
		
		<form action="" type="get">
			<select id="workloadname">
			<%for (String intel : workloadlist)
			{
				out.println("<option value=\""+ intel+"\">" + intel + "</option>");
			}
			%>	
			</select>	
			<br>
			<input type="button" value="run"  onclick="startWork()">
			<input type="reset" value="reset">
			<p></p>
		</form>
	</body>
</html>