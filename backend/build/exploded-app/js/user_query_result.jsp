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
		<form name="input" action="/userQuery.do" method="get">
			Name: <input type="text" name="name"> <input type="submit"
				value="OK">
		</form>
	</center>
	<b>

	<h1>User Account List</h1>
	<table border="1">
	<tr>
		<td>User Name</td> 
		<td>Password</td>
	</tr>
		---------------------------------------------------------------------<br>
		<%
			ArrayList<UserAccount> resultList = (ArrayList<UserAccount>) request
					.getAttribute("result");
			if (resultList != null) {
				for (UserAccount account : resultList) {
		%> 	
						<tr>
							<td><%=account.getUsername()%></td> 
							<td><%=account.getPassword()%></td>
						</tr>
		<%

 				}
 			}
 %>
</body>
</html>