<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<ut:set-pagination-query includeSorting="true" includeSearching="true"/>
<my:html-carcass title="${sessionScope.user.login} - users list">
    <div class="managing sidenav">
        <div class="login-main-text">
            <form action="${root}/controller" method="get">
                <input type="hidden" name="command" value="showUsersReport">
                <button type="submit" class="btn btn-black">Get users report</button>
            </form>

            <form action="${root}/jsp/logged/admin/util/userAddPage.jsp" method="get">
                <button type="submit" class="btn btn-black">Create new user</button>
            </form>
        </div>

    <div class="sorting-panel container">
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showAllUsers">
            <input type="hidden" name="page" value="1">
            <input type="hidden" name="pageSize" value="5">
            <label>Sort by:</label><br>
            <c:choose>
                <c:when test="${'userLogin'.equals(requestScope.orderBy)}">
                    <input type="radio" name="orderBy" value="userLogin" id="userLoginId" checked>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="orderBy" value="userLogin" id="userLoginId">
                </c:otherwise>
            </c:choose>
            <label for="userLoginId">Login</label><br>
            <c:choose>
                <c:when test="${'activitiesCount'.equals(requestScope.orderBy)}">
                    <input type="radio" name="orderBy" value="activitiesCount" id="count" checked>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="orderBy" value="activitiesCount" id="count">
                </c:otherwise>
            </c:choose>
            <label for="count">Activities count</label><br>
            <c:choose>
                <c:when test="${'totalTime'.equals(requestScope.orderBy)}">
                    <input type="radio" name="orderBy" value="totalTime" id="time" checked>
                </c:when>
                <c:otherwise>
                    <input type="radio" name="orderBy" value="totalTime" id="time">
                </c:otherwise>
            </c:choose>
            <label for="time">Total time</label> <br>
            <label>Activities count less then:</label>
            <input type="number" name="countLessThen" value="${requestScope.countLessThen}">
            <br>
            <label>Activities count bigger then:</label>
            <input type="number" name="countBiggerThen" value="${requestScope.countBiggerThen}">
            <br>
            <label>Search by login: </label>
            <input type="text" name="searchBy" value="${requestScope.searchBy}">
            <br>
            <button type="submit" class="btn btn-black">OK</button>
        </form>
    </div>
    </div>
    <div class="tables">
    <form action="${root}/controller">
        <input type="hidden" name="command" value="showAllUsers">
        <input type="hidden" name="page" value="${requestScope.page - 1}">
        <input type="hidden" name="pageSize" value="5">
        <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
        <input type="hidden" name="countLessThen" value="${requestScope.countLessThen}">
        <input type="hidden" name="countBiggerThen" value="${requestScope.countBiggerThen}">
        <input type="hidden" name="searchBy" value="${requestScope.searchBy}">
        <c:choose>
            <c:when test="${requestScope.page - 1 > 0}">
                <button type="submit" class="btn btn-black">Prev</button>
            </c:when>
            <c:otherwise>
                <button type="submit" class="btn btn-black" disabled>Prev</button>
            </c:otherwise>
        </c:choose>
    </form>
    <form action="${root}/controller">
        <input type="hidden" name="command" value="showAllUsers">
        <input type="hidden" name="page" value="${requestScope.page + 1}">
        <input type="hidden" name="pageSize" value="5">
        <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
        <input type="hidden" name="countLessThen" value="${requestScope.countLessThen}">
        <input type="hidden" name="countBiggerThen" value="${requestScope.countBiggerThen}">
        <input type="hidden" name="searchBy" value="${requestScope.searchBy}">
        <c:choose>
            <c:when test="${requestScope.page < requestScope.pageCount}">
                <button type="submit" class="btn btn-black">Next</button>
            </c:when>
            <c:otherwise>
                <button type="submit" class="btn btn-black" disabled>Next</button>
            </c:otherwise>
        </c:choose>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th scope="col">login</th>
            <th scope="col">name</th>
            <th scope="col">surname</th>
            <th scope="col">options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${requestScope.usersList}">
            <tr>
                <td>${user.login}</td>
                <td>${user.name}</td>
                <td>${user.surname}</td>
                <td>
                    <a href="${root}/controller?command=showDetailedUserInfo&uId=${user.id}">show details</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</my:html-carcass>
