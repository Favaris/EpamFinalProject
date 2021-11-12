<%--
  Created by IntelliJ IDEA.
  User: Favaris
  Date: 11/12/2021
  Time: 5:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sign in</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="sign_in"/>
    <label>
        <input type="text" name="login" required="required"/>
    </label>
    <br/>
    <label>
        <input type="password" name="password" required="required"/>
    </label>
    <br/>
    <c:if test="${requestScope.err_msg != null}">
        <h1>${requestScope.err_msg}</h1>
    </c:if>
    <input type="submit"/>
</form>
</body>
</html>
