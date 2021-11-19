<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>

<html>
<head>
    <title>sign up</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="signUp" >
    Login: <input type="text" name="login" required="required" minlength="4" maxlength="16" pattern="[A-Za-z]{4,16}?"/>
    <c:if test="${not empty requestScope.loginErrorMessage}">
        <br/>
        <h3>${requestScope.loginErrorMessage}</h3>
    </c:if>
    <br/>
    Pass: <input type="password" name="password" required="required" minlength="4" maxlength="32"/>
    <c:if test="${not empty requestScope.passwordErrorMessage}">
        <br/>
        <h3>${requestScope.passwordErrorMessage}</h3>
    </c:if>
    <br/>
    Name: <input type="text" name="name" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})"/>
    <c:if test="${not empty requestScope.nameErrorMessage}">
        <br/>
        <h3>${requestScope.nameErrorMessage}</h3>
    </c:if>
    <br/>
    Surname: <input type="text" name="surname" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})"/>
    <c:if test="${not empty requestScope.surnameErrorMessage}">
        <br/>
        <h3>${requestScope.surnameErrorMessage}</h3>
    </c:if>
    <br/>
    <c:if test="${requestScope.err_msg != null}">
        <h2>${requestScope.err_msg}</h2>
    </c:if>
    <input type="submit" value="Sign up"/>
</form>
</body>
</html>
