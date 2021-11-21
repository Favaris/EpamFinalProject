<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - admin page"/>
<body>
    you are logged as ${sessionScope.user.login} admin.
    <form action="${root}/controller">
        <input type="hidden" name="command" value="downloadActivities"/>
        <input type="submit" value="Activities"/>
    </form>

    <form action="${root}/controller">
        <input type="hidden" name="command" value="showUsersRequests">
        <input type="submit" value="Users' requests">
    </form>

    <form action="${root}/controller">
        <input type="hidden" name="command" value="showAllUsers"/>
        <button type="submit">Show users</button>
    </form>

    <form action="${root}/controller">
        <input type="hidden" name="command" value="signOut"/>
        <button type="submit">Sign out.</button>
    </form>
</body>
</html>
