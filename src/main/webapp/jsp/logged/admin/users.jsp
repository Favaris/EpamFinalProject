<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - users list">
    <form action="${root}/controller">
        <input type="hidden" name="command" value="showAllUsers">
        <input type="hidden" name="page" value="${requestScope.page - 1}">
        <input type="hidden" name="pageSize" value="5">
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
        <c:choose>
            <c:when test="${requestScope.page < requestScope.pageCount}">
                <button type="submit" class="btn btn-black">Next</button>
            </c:when>
            <c:otherwise>
                <button type="submit" class="btn btn-black" disabled>Next</button>
            </c:otherwise>
        </c:choose>
    </form>
    <form action="${root}/controller" method="get">
        <input type="hidden" name="command" value="showUsersReport">
        <button type="submit" class="btn btn-black">Get users report</button>
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
</my:html-carcass>
