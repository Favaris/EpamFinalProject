<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - users list">

    <table class="table">
        <thead>
        <tr>
            <th scope="col">login</th>
            <th scope="col">name</th>
            <th scope="col">surname</th>
            <th scope="col">activities count</th>
            <th scope="col">summary time spent</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="i" begin="0" end="${requestScope.users.size() - 1}" step="1">
            <tr>
                <td>${requestScope.users.get(i).login}</td>
                <td>${requestScope.users.get(i).name}</td>
                <td>${requestScope.users.get(i).surname}</td>
                <td>
                    <c:choose>
                        <c:when test="${requestScope.activitiesCounts.get(i) != 0}">
                            ${requestScope.activitiesCounts.get(i)}
                        </c:when>
                        <c:otherwise>
                            No activities yet
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <ut:convert minutes="${requestScope.totalTimeList.get(i)}" minutesLabel="mins" hoursLabel="hrs"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>