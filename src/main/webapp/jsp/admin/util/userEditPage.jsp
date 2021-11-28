<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
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
        <input type="hidden" name="command" value="downloadAllActivities">
        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
        <button type="submit" class="btn btn-black">Add activities</button>
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
        <c:forEach var="activity" items="${requestScope.userToEditActivities}">
            <tr>
                <td>${activity.name}</td>
                <td>
                    <c:forEach var="cat" items="${activity.categories}">
                        ${cat.name},
                    </c:forEach>
                </td>
                <td>${activity.description}</td>
                <td>
                    <ut:convert minutes="${activity.minutesSpent}" minutesLabel="mins" hoursLabel="hrs"/>
                </td>
                <td>
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#${'requestActivityAbandonment'.concat(activity.id)}">
                        Remove
                    </button>
                    <div class="modal fade" id="${'requestActivityAbandonment'.concat(activity.id)}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Confirm removing ${activity.name} from ${sessionScope.userToEdit.login}</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    Are you sure that you want to remove this activity from ${sessionScope.userToEdit.login}?<br>
                                    <strong>
                                        Warn: this action is irreversible. It will delete this activity from user and all time spent on it.
                                    </strong>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                    <form action="${root}/controller" method="post">
                                        <input type="hidden" name="command" value="removeUserActivity">
                                        <input type="hidden" name="aId" value="${activity.id}">
                                        <input type="hidden" name="uId" value="${sessionScope.userToEdit.id}">
                                        <button type="submit" class="btn btn-black">Confirm</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>
