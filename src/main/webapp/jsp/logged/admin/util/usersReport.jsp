<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>

<my:htmlCarcass title="${sessionScope.user.login} - users report">
    <div class="managing sidenav">
        <div class="login-main-text">
            <button type="button" class="btn btn-black" onclick="location.href='${root}/controller?command=showAllUsers&${sessionScope.backPage}';"><fmt:message key="user_detailed_page_jsp.go_back"/> </button>
        </div>
    </div>
    <div class="tables">
        <table class="table">
            <thead>
            <tr>
                <th scope="col"><fmt:message key="tables.titles.user_login"/> </th>
                <th scope="col"><fmt:message key="tables.titles.name"/></th>
                <th scope="col"><fmt:message key="tables.titles.surname"/></th>
                <th scope="col"><fmt:message key="tables.titles.activities_count"/></th>
                <th scope="col"><fmt:message key="tables.titles.time_spent"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${requestScope.usersList}">
                <tr>
                    <td>${user.login}</td>
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.info.activitiesCount == 0}">
                                <fmt:message key="tables.rows.no_activities_yet"/>
                            </c:when>
                            <c:otherwise>
                                ${user.info.activitiesCount}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><my:convert minutes="${user.info.totalTime}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</my:htmlCarcass>
