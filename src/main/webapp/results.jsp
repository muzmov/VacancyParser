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

<table>
    <tbody>
    <tr>
        <th>Title</th>
        <th>City</th>
        <th>Company Name</th>
        <th>Salary</th>
    </tr>

    <% for (Vacancy vacancy : (List<Vacancy>) request.getAttribute("vacancies")) { %>

    <tr class="vacancy">
        <td class="title"><a href="<%=vacancy.getUrl()%>"><%=vacancy.getTitle()%></a></td>
        <td class="city"><%=vacancy.getCity()%></td>
        <td class="companyName"><%=vacancy.getCompany().getName()%></td>
        <td class="salary"><%=vacancy.getSalary()%></td>
    </tr>

    <% } %>

    <tr class="vacancy template" style="display: none">
        <td class="title"><a href="url"></a></td>
        <td class="city"></td>
        <td class="companyName"></td>
        <td class="salary"></td>
    </tr>
    </tbody>
</table>
</body>
</html>