<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="dartmouth.edu.dartreminder.backend.data.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Query Result</title>
</head>
<body>
	<%
		String retStr = (String) request.getAttribute("_retStr");
		if (retStr != null) {
	%>
	<%=retStr%><br>
	<%
		}
	%>
	<center>
		<b>Query Result</b>
		<form name="input" action="/querySchedule.do" method="get">
			Name: <input type="text" name="name"> <input type="submit"
				value="OK">
		</form>
	</center>
	<b>

	<h1>Schedule List</h1>
	<table border="1">
	<tr>
		<td>ID</td>
		<td>Title</td>
		<td>Notes</td>
		<td>UseTime</td>
		<td>Time</td>
		<td>LocationName</td>
		<td>Lat</td>
		<td>Lng</td>
		<td>Arrive</td>
		<td>Radius</td>
		<td>Priority</td>
		<td>Repeat</td>
		<td>isCompleted</td>
		<td>userName</td>
		<td>sender</td>
	</tr>
		---------------------------------------------------------------------<br>
		<%
			ArrayList<Schedule> resultList = (ArrayList<Schedule>) request
					.getAttribute("result");
			if (resultList != null) {
				for (Schedule schedule : resultList) {
		%>
						<tr>
							<td><%=schedule.getId()%></td>
							<td><%=schedule.getTitle()%></td>
							<td><%=schedule.getNotes()%></td>
							<td><%=schedule.getUseTime()%></td>
							<td><%=schedule.getTime()%></td>
							<td><%=schedule.getLocationName()%></td>
							<td><%=schedule.getLat()%></td>
							<td><%=schedule.getLng()%></td>
							<td><%=schedule.getArrive()%></td>
							<td><%=schedule.getRadius()%></td>
							<td><%=schedule.getPriority()%></td>
							<td><%=schedule.getRepeat()%></td>
							<td><%=schedule.getCompleted()%></td>
							<td><%=schedule.getUserName()%></td>
							<td><%=schedule.getSender()%></td>
						</tr>
		<%
 				}
 			}
 %>
</body>
</html>