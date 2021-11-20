<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - user page"/>
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
<form action="${root}/controller">
    <input type="hidden" name="command" value="signOut"/>
    <button type="submit">Sign out.</button>
</form>

<form action="${root}/controller">
    <input type="hidden" name="command" value="downloadActivities"/>
    <input type="submit" value="Add activities"/>
</form>

<form action="${root}/controller">
    <input type="hidden" name="command" value="downloadUsersActivities"/>
    <input type="submit" value="Your activities"/>
</form>

</body>
</html>
