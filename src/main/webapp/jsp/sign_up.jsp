<%--
  Created by IntelliJ IDEA.
  User: Favaris
  Date: 11/12/2021
  Time: 12:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sign up</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="sign_up" >
    Login: <input type="text" name="login" required="required"/>
    <br/>
    Pass: <input type="password" name="password" required="required"/>
    <br/>
    Name: <input type="text" name="name" required="required"/>
    <br/>
    Surname: <input type="text" name="surname" required="required"/>
    <br/>
    <c:if test="${requestScope.err_msg != null}">
        <h2>${requestScope.err_msg}</h2>
    </c:if>
    <input type="submit" value="Sign up"/>
</form>
</body>
</html>
