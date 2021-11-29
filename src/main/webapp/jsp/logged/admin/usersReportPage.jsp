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
        <c:forEach var="entry" items="${requestScope.report}">
            <tr>
                <td>${entry.key.login}</td>
                <td>${entry.key.name}</td>
                <td>${entry.key.surname}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty entry.value}">
                            ${entry.value.size()}
<%--                            <table border="10" class="table">--%>
<%--                                <tbody>--%>
<%--                                <c:forEach var="activity" items="${entry.value}">--%>
<%--                                    <tr>--%>
<%--                                        <td> ${activity.name}</td>--%>
<%--                                        <td><ut:convert minutes="${activity.minutesSpent}" minutesLabel="mins" hoursLabel="hrs"/></td>--%>
<%--                                    </tr>--%>
<%--                                </c:forEach>--%>
<%--                                </tbody>--%>
<%--                            </table>--%>
                        </c:when>
                        <c:otherwise>
                            No activities yet
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:set var="summ" value="0" scope="page"/>
                    <c:forEach var="activity" items="${entry.value}">
                        <c:set var="summ" value="${summ + activity.minutesSpent}"/>
                    </c:forEach>
                    <ut:convert minutes="${summ}" minutesLabel="mins" hoursLabel="hrs"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>