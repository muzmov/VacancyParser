<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
<form action="admin_parse.do" method="post" id="nameform">
    Что ищем: <input type="text" name="searchString"><br>
    Число потоков (1 - 100): <input type="text" name="numThreads"><br>
    <label><input type="checkbox" name="hh" value="value">hh.ru</label><br>
    <label><input type="checkbox" name="moikrug" value="value">moikrug.ru</label><br>
</form>
<button type="submit" form="nameform" value="Submit">Отправить</button>
<br>
<br>
<br>
<form action = "admin_parse.do" method = "post" id = "deleteform">
    <input type="hidden" name = "delete" value="delete">
    <input type="submit" value="Очистить базу данных">
</form>
<form action = "admin_parse.do" method = "post" id = "parsereviewsform">
    <input type="hidden" name = "parsereviews" value="parse">
    <input type="submit" value="Парсить отзывы">
</form>
<form action = "login.do" method = "post" id = "exitform">
    <input type="hidden" name = "exit" value="exit">
    <input type="submit" value="Выход">
</form>
<br>
<form action = "proxy.do" method = "post" id = "proxyform">
    <textarea rows = "15" cols = "40" name="proxies"></textarea>
    <br>
    <input type="submit" value="Проверить и добавить прокси">
</form>
</body>
</html>