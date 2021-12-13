<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:htmlCarcass title="${sessionScope.user.login} - add activities to a user">
    <div class="managing sidenav">
        <div class="login-main-text">
            <div class="sorting-panel container">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showAddActivitiesForUserPage"/>
                    <input type="hidden" name="uId" value="${requestScope.uId}">
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="pageSize" value="5">
                    <label><fmt:message key="activities_jsp.sidenav.sort_by.label"/>:</label><br>
                    <c:choose>
                        <c:when test="${'activityName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="activityName" id="sortByName" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="activityName" id="sortByName">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByName"><fmt:message key="activities_jsp.sidenav.sort_by.name"/></label><br>
                    <c:choose>
                        <c:when test="${'categoryName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByCategory"><fmt:message key="activities_jsp.sidenav.sort_by.category"/></label><br>
                    <c:choose>
                        <c:when test="${'usersCount'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="usersCount" id="sortByUserAmount" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="usersCount" id="sortByUserAmount">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByUserAmount"><fmt:message key="activities_jsp.sidenav.sort_by.user_amount"/></label><br>
                    <label><fmt:message key="activities_jsp.sidenav.filter_by.label"/>:</label><br>
                    <c:choose>
                        <c:when test="${requestScope.filterBy != null}">
                            <c:forEach var="category" items="${requestScope.categories}">

                                <c:set var="contains" value="${false}"/>
                                <c:forEach var="catId" items="${requestScope.filterBy}">
                                    <c:if test="${not contains}">
                                        <c:set var="contains" value="${catId.equals(category.id.toString())}"/>
                                    </c:if>
                                </c:forEach>

                                <c:choose>
                                    <c:when test="${contains}">
                                        <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" checked>
                                        <label for="${category.id}">${category.name}</label><br>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}">
                                        <label for="${category.id}">${category.name}</label><br>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="category" items="${requestScope.categories}">
                                <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" checked>
                                <label for="${category.id}">${category.name}</label><br>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    <button type="submit" class="btn btn-black"><fmt:message key="activities_jsp.sidenav.ok"/></button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">
    <form action="${root}/controller" method="get">
        <input type="hidden" name="command" value="manageUsersActivities">
        <input type="hidden" name="uId" value="${requestScope.uId}">
        <button type="submit" class="btn btn-black"><fmt:message key="add_activities_for_user_page.back_to_managing"/></button>
    </form>
        <br>
        <my:paginationNavigation command="showAddActivitiesForUserPage"/>
    <table class="table">
        <thead>
        <tr>
            <th scope="col"><fmt:message key="tables.titles.name"/></th>
            <th scope="col"><fmt:message key="tables.titles.category"/></th>
            <th scope="col"><fmt:message key="tables.titles.description"/></th>
            <th scope="col"><fmt:message key="tables.titles.amount_of_users"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="activity" items="${requestScope.notTakenActivities}">
            <tr>
                <td>${activity.name}</td>
                <td>
                    ${activity.category.name}
                </td>
                <td>${activity.description}</td>
                <td>${activity.usersCount}</td>
                <td>
                    <form action="${root}/controller" method="post">
                        <input type="hidden" name="command" value="addUserActivity"/>
                        <input type="hidden" name="uId" value="${requestScope.uId}">
                        <input type="hidden" name="aId" value="${activity.id}">
                        <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.add_activity.title"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</my:htmlCarcass>
