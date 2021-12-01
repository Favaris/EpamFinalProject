<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<ut:set-pagination-query/>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - edit user ${sessionScope.userToEdit.login}">
    <form action="${root}/controller" method="post">
        <input type="hidden" value="updateUser">

    </form>
    <div>
        <form action="${root}/controller" method="post">
            <input type="hidden" name="command" value="updateUser">
            <input type="hidden" name="id" value="${sessionScope.userToEdit.id}">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" class="form-control" required="required" value="${sessionScope.userToEdit.name}"/>
            </div>
            <div class="form-group">
                <label>Surname</label>
                <input type="text" name="surname" class="form-control" required="required" value="${sessionScope.userToEdit.surname}"/>
            </div>
            <c:if test="${sessionScope.err_msg != null}">
                ${sessionScope.err_msg} <br>
            </c:if>
            <button type="submit" class="btn btn-black">Save</button>
        </form>
    </div>

    <label>User's activities</label>
    <form action="${root}/controller" method="get">
        <input type="hidden" name="command" value="showActivitiesPage">
        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
        <button type="submit" class="btn btn-black">Add activities</button>
    </form>
    <div>
    <form action="${root}/controller">
        <input type="hidden" name="command" value="showEditUserPage">
        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
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
        <input type="hidden" name="command" value="showEditUserPage">
        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
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
    </div>
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
        <c:forEach var="activity" items="${requestScope.paginatedActivities}">
            <tr>
                <td>${activity.name}</td>
                <td>
                    ${activity.category.name}
                </td>
                <td>${activity.description}</td>
                <td>
                    <ut:convert minutes="${activity.minutesSpent}" minutesLabel="mins" hoursLabel="hrs"/>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${sessionScope.removedActivities.contains(activity)}">
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="addUserActivity">
                                <input type="hidden" name="aId" value="${activity.id}">
                                <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
                                <button type="submit" class="btn btn-black">Add back</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="removeUserActivity">
                                <input type="hidden" name="aId" value="${activity.id}">
                                <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
                                <button type="submit" class="btn btn-black">Remove</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>
