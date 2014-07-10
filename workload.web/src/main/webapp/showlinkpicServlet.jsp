<%@ page language="java" import="java.util.*" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>


Show the log file link(s) as follows.
<br>
<c:forEach var="log" items="${workloadlist.logList}">
<a href='<c:out value="./LogPic/${log}"/>' target="_blank"><c:out value="${log}"/></a>
<br>
</c:forEach>

<br>
<br>
Show the Picture of analysis result as follow.
<br>

<c:forEach var="pic" items="${workloadlist.imgList}">
<img src='<c:out value="./LogPic/${pic}"/>' width="128" height="128" />
<br>
</c:forEach>

<input type="button" value="return" onclick="window.location='index.jsp'"/>

</body>
</html>