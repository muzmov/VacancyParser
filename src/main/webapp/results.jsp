<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="requestString" scope="request" type="java.lang.String"/>
<jsp:useBean id="vacancies" scope="request" type="java.util.List"/>

<table>
    <tbody>

    <tr>
        <th>
            <a href="?${requestString}&sortBy=${"title" eq param.sortBy ? "-title" : "title"}">Title</a>
        </th>
        <th>
            <a href="?${requestString}&sortBy=${"city" eq param.sortBy ? "-city" : "city"}">City</a>
        </th>
        <th>
            <a href="?${requestString}&sortBy=${"company" eq param.sortBy ? "-company" : "company"}">Company Name</a>
        </th>
        <th>
            <a href="?${requestString}&sortBy=${"rating" eq param.sortBy ? "-rating" : "rating"}">Company Rating</a>
        </th>
        <th>
            <a href="?${requestString}&sortBy=${"reviews" eq param.sortBy ? "-reviews" : "reviews"}">Company Reviews</a>
        </th>
        <th>
            <a href="?${requestString}&sortBy=${"salary" eq param.sortBy ? "-salary" : "salary"}">Salary</a>
        </th>
    </tr>

    <c:forEach var="vacancy" items="${vacancies}">
        <tr class="vacancy">
            <td class="title">
                <a href="${vacancy.url}">${vacancy.title}</a>
            </td>
            <td class="city">
                    ${vacancy.city}
            </td>
            <td class="companyName">
                <a href="${vacancy.company.url}">${vacancy.company.name}</a>
            </td>
            <td class="rating">
                    ${vacancy.company.rating eq 0 ? '' : vacancy.company.rating}
            </td>
            <td class="reviews">
                <a href="${vacancy.company.reviewsUrl}">${vacancy.company.reviewsUrl}</a>
            </td>
            <td class="salary">
                    ${vacancy.salary}
            </td>
        </tr>
    </c:forEach>

    </tbody>
</table>

<script>
    setHandler();
</script>
