<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>

<html>
<head>
    <title>${sessionScope.user.login} - activities</title>
</head>
<body>
<table class="table">
    <thead>
    <tr>
        <th>name</th>
        <th>categories</th>
        <th>description</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach var="activity" items="${requestScope.activities}">
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

    </c:forEach>

</table>
</body>
</html>
