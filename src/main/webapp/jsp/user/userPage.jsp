<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>

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

<form action="${pageContext.request.contextPath}/controller">
    <input type="hidden" name="command" value="downloadActivities"/>
    <input type="submit" value="Activities"/>
</form>

</body>
</html>
