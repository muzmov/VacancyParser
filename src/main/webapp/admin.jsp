<%@ page import="net.kuryshev.listener.SessionCounter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="js/main.js"></script>
</head>
<body>
Active sessions: <%=SessionCounter.getSessions()%> <br>

<form action="admin_parse.do" method="post" id="nameform">
    Что ищем:
    <label>
        <input name="searchString">
    </label>
    <br>
    Число потоков (1 - 100):
    <label>
        <input name="numThreads">
    </label>
    <br>
    <label><input type="checkbox" name="hh" value="value">hh.ru</label><br>
    <label><input type="checkbox" name="moikrug" value="value">moikrug.ru</label><br>
    <input type="submit" value="Отправить">
</form>
<div id="parsingProgress"></div>
<br>
<br>
<br>
<form action="admin_parse.do" method="post" id="deleteform">
    <input type="hidden" name="delete" value="delete">
    <input type="submit" value="Очистить базу данных">
</form>
<form action="admin_parse.do" method="post" id="parsereviewsform">
    <input type="hidden" name="parsereviews" value="parse">
    <input type="submit" value="Парсить отзывы">
    <div id="rewiewsProgress"></div>
</form>
<form action="login.do" method="post" id="exitform">
    <input type="hidden" name="exit" value="exit">
    <input type="submit" value="Выход">
</form>
<br>
<form action="proxy.do" method="post" id="proxyform">
    <label>
        <textarea rows="15" cols="40" name="proxies"></textarea>
    </label>
    <br>
    <input type="submit" value="Проверить и добавить прокси">
    <div id="proxyProgress"></div>
</form>
</body>
</html>