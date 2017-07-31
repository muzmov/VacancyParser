<%@ page import="net.kuryshev.model.TaskProgress" %>
<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 30.07.2017
  Time: 18:34
  To change this template use File | Settings | File Templates.
--%>
<% TaskProgress progress = (TaskProgress) session.getAttribute("Progress");
    if (progress != null) {
        if (progress.isDone()) session.removeAttribute("Progress");
%>
<%= progress.getProgress() %>
<%
    }
%>
