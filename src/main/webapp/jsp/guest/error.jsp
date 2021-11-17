<%--
  Created by IntelliJ IDEA.
  User: Favaris
  Date: 11/16/2021
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Error!</title>
</head>
<body>
    <c:if test="${sessionScope.err_msg != null}">
        <h2>
            You are already logged in. If you want to log in as another user, please log out firstly.
        </h2>
        <form action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="signOut"/>
            <button type="submit">Sign out.</button>
        </form>
    </c:if>
</body>
</html>
