<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>

<html>
<head>
    <title>sign in</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="signIn"/>
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
