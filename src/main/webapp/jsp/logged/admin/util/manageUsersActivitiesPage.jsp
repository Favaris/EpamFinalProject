<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<ut:set-pagination-query includeSortingParameters="true"/>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - manage user's activities">
    <div class="managing sidenav">
        <div class="login-main-text">
            <div class="sorting-panel container">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="manageUsersActivities"/>
                    <input type="hidden" name="uId" value="${requestScope.uId}">
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="pageSize" value="5">
                    <label>Sort by:</label><br>
                    <c:choose>
                        <c:when test="${'activityName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="activityName" id="sortByName" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="activityName" id="sortByName">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByName">name</label><br>
                    <c:choose>
                        <c:when test="${'categoryName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByCategory">category</label><br>
                    <c:choose>
                        <c:when test="${'timeSpent'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="timeSpent" id="sortByTimeSpent" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="timeSpent" id="sortByTimeSpent">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByTimeSpent">time spent</label><br>
                    <label>Filter by:</label><br>
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

                    <button type="submit" class="btn btn-black">OK</button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">
        <form action="${root}/controller" method="get">
            <input type="hidden" name="command" value="showDetailedUserInfo">
            <input type="hidden" name="uId" value="${requestScope.uId}">
            <button type="submit" class="btn btn-black">Back to editing user ${requestScope.userToShow.login}</button>
        </form>
        <br>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="manageUsersActivities">
            <input type="hidden" name="uId" value="${requestScope.uId}">
            <input type="hidden" name="page" value="${requestScope.page - 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${empty requestScope.orderBy}">
                    <input type="hidden" name="orderBy" value="activityName">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${requestScope.page - 1 > 0}">
                    <button type="submit" class="btn btn-black">Prev</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Prev</button>
                </c:otherwise>
            </c:choose>
            <c:forEach var="catId" items="${requestScope.filterBy}">
                <input type="hidden" name="filterBy" value="${catId}">
            </c:forEach>
        </form>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="manageUsersActivities">
            <input type="hidden" name="uId" value="${requestScope.uId}">
            <input type="hidden" name="page" value="${requestScope.page + 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${empty requestScope.orderBy}">
                    <input type="hidden" name="orderBy" value="activityName">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${requestScope.page < requestScope.pageCount}">
                    <button type="submit" class="btn btn-black">Next</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Next</button>
                </c:otherwise>
            </c:choose>
            <c:forEach var="catId" items="${requestScope.filterBy}">
                <input type="hidden" name="filterBy" value="${catId}">
            </c:forEach>
        </form>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">name</th>
                <th scope="col">categories</th>
                <th scope="col">description</th>
                <th scope="col">time spent</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="activity" items="${requestScope.usersActivities}">
                <tr>
                    <td>${activity.name}</td>
                    <td>
                            ${activity.category.name}
                    </td>
                    <td>${activity.description}</td>
                    <td><ut:convert minutes="${activity.minutesSpent}" minutesLabel="mins" hoursLabel="hrs"/></td>
                    <td>
                        <form action="${root}/controller" method="post">
                            <input type="hidden" name="command" value="removeUserActivity"/>
                            <input type="hidden" name="uId" value="${requestScope.uId}">
                            <input type="hidden" name="aId" value="${activity.id}">
                            <button type="submit" class="btn btn-black">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</my:html-carcass>