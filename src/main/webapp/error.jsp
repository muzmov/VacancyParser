<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 29.06.2017
  Time: 13:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Some error occurred!</title>
</head>
<body>
    Some error occurred:<br>
    <%=request.getAttribute("error")%><br>
    <a href = 'index.jsp'>go back to main page</a>
</body>
</html>
