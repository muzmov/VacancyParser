<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <form action="search.do" method="post" id="nameform">
    Что ищем: <input type="text" name="searchString"><br>
    <label><input type="checkbox" name="title" value="value">Search in title</label><br>
    <label><input type="checkbox" name="description" value="value">Search in description</label><br>
  </form>
  <button type="submit" form="nameform" value="Submit">Отправить</button>
  </body>
</html>
