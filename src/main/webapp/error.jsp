<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="goBackUrl" scope="request" type="java.lang.String"/>
<jsp:useBean id="error" scope="request" type="java.lang.String"/>
<html>
<head>
    <title>Some error occurred!</title>
</head>
<body>
    Some error occurred:<br>
    ${error}<br>
    <a href = '${goBackUrl}'>go back</a>
</body>
</html>
