<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<form action="admin_parse.do" method="post" id="nameform">
    Что ищем: <input type="text" name="searchString"><br>
    <label><input type="checkbox" name="hh" value="value">hh.ru</label><br>
    <label><input type="checkbox" name="moikrug" value="value">moikrug.ru</label><br>
</form>
<button type="submit" form="nameform" value="Submit">Отправить</button>
<br>
<br>
<br>
<form action = "admin_parse.do", method = "post", id = "deleteform">
    <input type="hidden" name = "delete" value="delete">
    <input type="submit" value="Очистить базу данных">
</form>
</body>
</html>