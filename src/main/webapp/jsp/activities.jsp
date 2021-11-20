<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - activities"/>
<body>
<table class="table">
    <thead>
    <tr>
        <th scope="col">name</th>
        <th scope="col">categories</th>
        <th scope="col">description</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="activity" items="${requestScope.activities}">
        <tr>
        <td>${activity.name}</td>
        <td>
            <c:forEach var="cat" items="${activity.categories}">
                ${cat.name},
            </c:forEach>
        </td>
        <td>${activity.description}</td>
        <td>
            <c:choose>
                <c:when test="${sessionScope.user.role eq 'user'}">
                    <form action="${pageContext.request.contextPath}/controller" method="post">
                        <input type="hidden" name="command" value="addUserActivityRequest"/>
                        <input type="hidden" name="uId" value="${sessionScope.user.id}"/>
                        <input type="hidden" name="aId" value="${activity.id}"/>
                        <input type="submit" value="add"/>
                    </form>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/controller?command=showActivityEditForm&id=${activity.id}">edit</a>
                    <a href="${pageContext.request.contextPath}/confirmDeleteActivity&id=${activity.id}">delete</a>
                </c:otherwise>
            </c:choose>
        </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
