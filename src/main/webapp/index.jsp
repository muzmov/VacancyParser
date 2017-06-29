<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 25.06.2017
  Time: 10:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <form action="search.do" method="post" id="nameform">
    Что ищем: <input type="text" name="searchString"><br>
    <label><input type="checkbox" name="hh" value="value">hh.ru</label><br>
    <label><input type="checkbox" name="moikrug" value="value">moikrug.ru</label><br>
    <label><input type="checkbox" name="title" value="value">Search in title</label><br>
    <label><input type="checkbox" name="description" value="value">Search in description</label><br>
  </form>
  <button type="submit" form="nameform" value="Submit">Отправить</button>
  </body>
</html>
