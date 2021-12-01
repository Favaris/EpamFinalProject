<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="user"/>
<my:html-carcass title="${sessionScope.user.login} - active requests">
    <table class="table">
        <thead>
        <tr>
            <th scope="col">activity name</th>
            <th scope="col">category</th>
            <th scope="col">request type</th>
            <th scope="col">options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="request" items="${sessionScope.requests}">
            <tr>
                <td>${request.value.login}</td>
                <td>${request.key.name}</td>
                <td>
                    <c:choose>
                        <c:when test="${request.key.accepted eq false}">
                            accept
                        </c:when>
                        <c:otherwise>
                            abandon
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <form action="${root}/controller" method="post">
                        <input type="hidden" name="command" value="acceptRequest">
                        <input type="hidden" name="aId" value="${request.key.activityId}"/>
                        <input type="hidden" name="uId" value="${request.value.id}"/>
                        <input type="submit" value="Accept"/>
                    </form>
                    <form action="${root}/controller" method="post">
                        <input type="hidden" name="command" value="denyRequest">
                        <input type="hidden" name="aId" value="${request.key.activityId}"/>
                        <input type="hidden" name="uId" value="${request.value.id}"/>
                        <input type="submit" value="Deny"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>