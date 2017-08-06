<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html lang="ru">
<head>
  <meta charset="utf-8">
  <title>Вакансии</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="js/results.js"></script>
</head>
<body>
<form action="search.do" id="nameform">
  Что ищем:
  <label><input name="searchString"></label><br>
  <label><input type="checkbox" name="title" value="value">title</label>
  <label><input type="checkbox" name="description" value="value">description</label><br>
  <input type="submit" value="Поиск">
</form>

  <div id = "results"></div>

</body>
</html>