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
