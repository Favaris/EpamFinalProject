<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - ${requestScope.userToShow.login}'s detailed info">
    <div class="managing sidenav">
        <div class="login-main-text">
            <button type="button" class="btn btn-black" onclick="location.href='${root}/controller?command=showAllUsers&${sessionScope.paginationQueryString}';">Go back</button>
        </div>
    </div>
    <div class="container">
        <label>
            ${requestScope.userToShow.login}'s info
        </label>
        <br>
            <label>
            Name:
                <input type="text" value="${requestScope.userToShow.name}" disabled>
                <button type="button" class="btn btn-black" data-toggle="modal" data-target="#editUserName">
                    Edit
                </button>
            </label>
            <div class="modal fade" id="editUserName" tabindex="-1" role="dialog" aria-labelledby="Edit user name" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <form action="${root}/controller" method="post">
                        <input type="hidden" name="command" value="updateUser">
                        <input type="hidden" name="uId" value="${requestScope.userToShow.id}">
                        <input type="hidden" name="surname" value="${requestScope.userToShow.surname}">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Edit</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <label>Name</label>
                                <input type="text" name="name" value="${requestScope.userToShow.name}" required>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-black">Save</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <br>
        <label>
            Surname:
            <input type="text" value="${requestScope.userToShow.surname}" disabled>
            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#editUserSurname">
                Edit
            </button>
        </label>
        <div class="modal fade" id="editUserSurname" tabindex="-1" role="dialog" aria-labelledby="Edit user name" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <form action="${root}/controller" method="post">
                    <input type="hidden" name="command" value="updateUser">
                    <input type="hidden" name="uId" value="${requestScope.userToShow.id}">
                    <input type="hidden" name="name" value="${requestScope.userToShow.name}">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Edit</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <label>Surname</label>
                            <input type="text" name="surname" value="${requestScope.userToShow.surname}" required>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-black">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <br>
        <label>
            Activities count: ${requestScope.activitiesCount}
        </label>

        <br>
        <label>
            Total time spent: <ut:convert minutes="${requestScope.totalTime}" minutesLabel="mins" hoursLabel="hrs"/>
        </label>
<br>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="manageUsersActivities">
            <input type="hidden" name="uId" value="${requestScope.userToShow.id}">
            <button type="submit" class="btn btn-black">Manage ${requestScope.userToShow.login}'s activities</button>
        </form>
    </div>
</my:html-carcass>
