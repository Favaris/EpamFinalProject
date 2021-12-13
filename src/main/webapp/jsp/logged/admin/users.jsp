<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>

<c:set var="backPage" scope="session" value="page=${requestScope.page}&pageSize=${requestScope.pageSize}&orderBy=${requestScope.orderBy}&countLessThan=${requestScope.countLessThan}&countBiggerThan=${requestScope.countBiggerThan}&searchBy=${requestScope.searchBy}"/>

<my:htmlCarcass title="${sessionScope.user.login} - users list">
    <div class="managing sidenav">
        <div class="login-main-text">
            <form action="${root}/controller" method="get">
                <input type="hidden" name="command" value="showUsersReport">
                <input type="hidden" name="countLessThan" value="${requestScope.countLessThan}">
                <input type="hidden" name="countBiggerThan" value="${requestScope.countBiggerThan}">
                <input type="hidden" name="searchBy" value="${requestScope.searchBy}">
                <button type="submit" class="btn btn-black"><fmt:message key="users_jsp.sidenav.get_users_report"/></button>
            </form>

            <form action="${root}/jsp/logged/admin/util/userAddPage.jsp" method="get">
                <button type="submit" class="btn btn-black"><fmt:message key="users_jsp.sidenav.create_new_user"/></button>
            </form>
        </div>

    <div class="sorting-panel container">
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showAllUsers">
            <input type="hidden" name="page" value="1">
            <input type="hidden" name="pageSize" value="5">
            <label><fmt:message key="activities_jsp.sidenav.sort_by.label"/>:</label><br>
            <c:choose>
                <c:when test="${'userLogin'.equals(requestScope.orderBy)}">
                    <input type="radio" name="orderBy" value="userLogin" id="userLoginId" checked>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="orderBy" value="userLogin" id="userLoginId">
                </c:otherwise>
            </c:choose>
            <label for="userLoginId"><fmt:message key="users_jsp.sidenav.sort_by.login"/></label><br>
            <c:choose>
                <c:when test="${'activitiesCount'.equals(requestScope.orderBy)}">
                    <input type="radio" name="orderBy" value="activitiesCount" id="count" checked>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="orderBy" value="activitiesCount" id="count">
                </c:otherwise>
            </c:choose>
            <label for="count"><fmt:message key="users_jsp.sidenav.sort_by.activities_count"/></label><br>
            <c:choose>
                <c:when test="${'totalTime'.equals(requestScope.orderBy)}">
                    <input type="radio" name="orderBy" value="totalTime" id="time" checked>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="orderBy" value="totalTime" id="time">
                </c:otherwise>
            </c:choose>
            <label for="time"><fmt:message key="users_jsp.sidenav.sort_by.total_time"/></label> <br>
            <label><fmt:message key="users_jsp.sidenav.ac_count_less"/>:</label>
            <input type="number" name="countLessThan" value="${requestScope.countLessThan}" min="0">
            <br>
            <label><fmt:message key="users_jsp.sidenav.ac_count_bigger"/>:</label>
            <input type="number" name="countBiggerThan" value="${requestScope.countBiggerThan}" min="0">
            <br>
            <label><fmt:message key="users_jsp.sidenav.search_by_login"/>: </label><br>
            <input type="text" name="searchBy" value="${requestScope.searchBy}">
            <br>
            <button type="submit" class="btn btn-black">OK</button>
        </form>
    </div>
    </div>
    <div class="tables">
        <my:paginationNavigation command="showAllUsers"/>
    <table class="table">
        <thead>
        <tr>
            <th scope="col"><fmt:message key="tables.titles.user_login"/> </th>
            <th scope="col"><fmt:message key="tables.titles.name"/></th>
            <th scope="col"><fmt:message key="tables.titles.surname"/></th>
            <th scope="col"><fmt:message key="tables.titles.activities_count"/></th>
            <th scope="col"><fmt:message key="tables.titles.time_spent"/></th>
            <th scope="col"><fmt:message key="tables.titles.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${requestScope.usersList}">
            <tr>
                <td><a href="${root}/controller?command=showDetailedUserInfo&uId=${user.id}">${user.login}</a></td>
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
                <td>
                    <a href="${root}/controller?command=showDetailedUserInfo&uId=${user.id}"><fmt:message key="tables.rows.show_details"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</my:htmlCarcass>
