<%@ page import="java.util.List" %>
<%@ page import="net.kuryshev.model.entity.Vacancy" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ru">
<head>
  <meta charset="utf-8">
  <title>Вакансии</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="js/results.js"></script>
</head>
<body>
<form action="search.do" method="get" id="nameform">
  Что ищем: <input type="text" name="searchString"><br>
  <label><input type="checkbox" name="title" value="value">title</label>
  <label><input type="checkbox" name="description" value="value">description</label><br>
  <input type="submit" value="Поиск">
</form>
<table>
  <tbody>

  <div id = "results"></div>

</tbody>
</table>
</body>
</html>