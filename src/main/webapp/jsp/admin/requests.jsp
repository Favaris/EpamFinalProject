<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>

<html>
<head>
    <title>${sessionScope.user.login} - requests</title>
</head>
<body>
<table class="table">
    <thead>
    <tr>
        <th>user login</th>
        <th>activity name</th>
        <th>request type</th>
        <th>options</th>
    </tr>
    </thead>
    <c:forEach var="request" items="${requestScope.requests}">
        <td>${request.key.login}</td>
        <td>${request.value.name}</td>
        <td>
            <c:choose>
                <c:when test="${request.value.accepted eq false}">
                    accept
                </c:when>
                <c:otherwise>
                    abandon
                </c:otherwise>
            </c:choose>
        </td>
        <td>
            <form></form>
        </td>

    </c:forEach>

</table>
</body>
</html>
