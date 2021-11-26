<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

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

    ${sessionScope.userToEditActivities}
</my:html-carcass>
