<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - user page"/>
<body>
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>

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


</body>
</html>
