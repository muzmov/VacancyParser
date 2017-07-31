<%@ page import="java.util.List" %>
<%@ page import="net.kuryshev.model.entity.Vacancy" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ru">
<head>
  <meta charset="utf-8">
  <title>Вакансии</title>
</head>
<body>
<form action="search.do" method="post" id="nameform">
  Что ищем: <input type="text" name="searchString"><br>
  <label><input type="checkbox" name="title" value="value">title</label>
  <label><input type="checkbox" name="description" value="value">description</label><br>
  <input type="submit" value="Поиск">
</form>
<table>
  <tbody>

  <%List<Vacancy> vacancies =  (List<Vacancy>) request.getAttribute("vacancies");
    if (vacancies != null) { %>

  <tr>
    <th>Title</th>
    <th>City</th>
    <th>Company Name</th>
<th>Company Rating</th>
<th>Company Reviews</th>
<th>Salary</th>
</tr>

<%for (Vacancy vacancy : (List<Vacancy>) request.getAttribute("vacancies")) { %>

<tr class="vacancy">
  <td class="title"><a href="<%=vacancy.getUrl()%>"><%=vacancy.getTitle()%></a></td>
  <td class="city"><%=vacancy.getCity()%></td>
  <td class="companyName"><a href = "<%=vacancy.getCompany().getUrl()%>"><%=vacancy.getCompany().getName()%></a></td>
  <td class = "rating"><%=vacancy.getCompany().getRating() == 0 ? "" : vacancy.getCompany().getRating() + ""%></td>
  <td class="reviews"><a href = "<%=vacancy.getCompany().getRewiewsUrl()%>"><%=vacancy.getCompany().getRewiewsUrl()%></a></td>
  <td class="salary"><%=vacancy.getSalary()%></td>
</tr>

<% }} %>

</tbody>
</table>
</body>
</html>