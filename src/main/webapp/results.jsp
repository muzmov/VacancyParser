<%@ page import="net.kuryshev.model.entity.Vacancy" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%
    List<Vacancy> vacancies = (List<Vacancy>) request.getAttribute("vacancies");
    if (vacancies != null) {
%>

<% Map<String, String[]> params = request.getParameterMap();
String requestString = "";
for (Map.Entry<String, String[]> entry : params.entrySet()) {
    if (!entry.getKey().equals("sortBy"))
        requestString += entry.getKey() + "=" + entry.getValue()[0] + "&";
}
requestString = requestString.substring(0, requestString.length() - 1);
%>

<tr>
    <th><a href="<%= "?" + requestString + "&sortBy=" + ("title".equals(request.getParameter("sortBy")) ? "-title" : "title") %>">Title</a></th>
    <th><a href="<%= "?" + requestString + "&sortBy=" + ("city".equals(request.getParameter("sortBy")) ? "-city" : "city") %>">City</a></th>
    <th><a href="<%= "?" + requestString + "&sortBy=" + ("company".equals(request.getParameter("sortBy")) ? "-company" : "company") %>">Company Name</a></th>
    <th><a href="<%= "?" + requestString + "&sortBy=" + ("rating".equals(request.getParameter("sortBy")) ? "-rating" : "rating") %>">Company Rating</a></th>
    <th><a href="<%= "?" + requestString + "&sortBy=" + ("reviews".equals(request.getParameter("sortBy")) ? "-reviews" : "reviews") %>">Company Reviews</a></th>
    <th><a href="<%= "?" + requestString + "&sortBy=" + ("salary".equals(request.getParameter("sortBy")) ? "-salary" : "salary") %>">Salary</a></th>
</tr>

<%for (Vacancy vacancy : (List<Vacancy>) request.getAttribute("vacancies")) { %>

<tr class="vacancy">
    <td class="title"><a href="<%=vacancy.getUrl()%>"><%=vacancy.getTitle()%>
    </a></td>
    <td class="city"><%=vacancy.getCity()%>
    </td>
    <td class="companyName"><a href="<%=vacancy.getCompany().getUrl()%>"><%=vacancy.getCompany().getName()%>
    </a></td>
    <td class="rating"><%=vacancy.getCompany().getRating() == 0 ? "" : vacancy.getCompany().getRating() + ""%>
    </td>
    <td class="reviews"><a href="<%=vacancy.getCompany().getRewiewsUrl()%>"><%=vacancy.getCompany().getRewiewsUrl()%>
    </a></td>
    <td class="salary"><%=vacancy.getSalary()%>
    </td>
</tr>

<% }
} %>

<script>
    setHandler();
</script>
