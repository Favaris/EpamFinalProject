<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - add activities to a user ${sessionScope.userToEdit.login}">
    <form action="${root}/controller" method="get">
        <input type="hidden" name="command" value="showEditUserPage">
        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
        <button type="submit" class="btn btn-black">Back to editing user ${sessionScope.userToEdit.login}</button>
    </form>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">name</th>
            <th scope="col">categories</th>
            <th scope="col">description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="activity" items="${requestScope.activities}">
            <tr>
                <td>${activity.name}</td>
                <td>
                    <c:forEach var="cat" items="${activity.categories}">
                        ${cat.name},
                    </c:forEach>
                </td>
                <td>${activity.description}</td>
                <td>
                    <form action="${root}/controller" method="post">
                        <input type="hidden" name="command" value="addUserActivity"/>
                        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
                        <input type="hidden" name="aId" value="${activity.id}">
                        <button type="submit" class="btn btn-black">Add</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>
