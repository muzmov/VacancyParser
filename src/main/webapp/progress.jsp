<%@ page import="net.kuryshev.model.TaskProgress" %>
<% TaskProgress progress = (TaskProgress) session.getAttribute("Progress");
    if (progress != null) {
        if (progress.isDone()) session.removeAttribute("Progress");
%>
<%= progress.getProgress() %>
<%
    }
%>
