<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - running activities"/>
<body>
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>

<table class="table">
    <thead>
    <tr>
        <th scope="col">name</th>
        <th scope="col">categories</th>
        <th scope="col">description</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="activity" items="${requestScope.runningActivities}">
        <tr>
            <td>${activity.name}</td>
            <td>
                <c:forEach var="cat" items="${activity.categories}">
                    ${cat.name},
                </c:forEach>
            </td>
            <td>${activity.description}</td>
            <td>
               <form action="${root}/controller" method="post">
                    <%--..........--%>
                   <input type="submit" value="Add time"/>
               </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
