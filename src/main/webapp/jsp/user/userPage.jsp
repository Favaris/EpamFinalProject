<%--
  Created by IntelliJ IDEA.
  User: Favaris
  Date: 11/12/2021
  Time: 1:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>user page</title>
</head>
<body>
login: ${sessionScope.user.login}
<br/>
pass: ${sessionScope.user.password}
<br/>
name: ${sessionScope.user.name}
<br/>
surname: ${sessionScope.user.surname}
<br/>
role: ${sessionScope.user.role}
<br/>
<form action="${pageContext.request.contextPath}/controller">
    <input type="hidden" name="command" value="signOut"/>
    <button type="submit">Sign out.</button>
</form>

<table class="table">
    <thead>
    <tr>
        <th>name</th>
        <th>categories</th>
        <th>description</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach var="activity" items="${sessionScope.activities}">
        <td>${activity.name}</td>
        <td>${activity.categories}</td>
        <td>${activity.description}</td>
    </c:forEach>

</table>
</body>
</html>
