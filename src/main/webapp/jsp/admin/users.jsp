<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="${sessionScope.user.login} - users list"/>
<body>
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>

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
                <a href="${root}/controller?command=showEditUserPage&id=${user.id}">edit</a>
<%--                <a href="${root}/controller?command=showDeleteUserConfirmationPage&id=${user.id}">delete</a>--%>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
