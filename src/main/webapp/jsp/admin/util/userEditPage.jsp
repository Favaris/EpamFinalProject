<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - edit user ${requestScope.userToEdit.login}"/>
<body>
${requestScope.userToEdit}
${requestScope.userToEditActivities}
</body>
</html>
