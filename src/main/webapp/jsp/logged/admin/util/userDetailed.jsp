<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:htmlCarcass title="${sessionScope.user.login} - ${requestScope.userToShow.login}'s detailed info">
    <div class="managing sidenav">
        <div class="login-main-text">
            <button type="button" class="btn btn-black" onclick="location.href='${root}/controller?command=showAllUsers&${sessionScope.backPage}';"><fmt:message key="user_detailed_page_jsp.go_back"/> </button>
        </div>
    </div>
    <div class="container">
        <label>
            <fmt:message key="user_detailed_jsp.label"/> ${requestScope.userToShow.login}
        </label>
        <br>
            <label>
            <fmt:message key="entities.fields.name"/> :
                <input type="text" value="${requestScope.userToShow.name}" disabled>
                <button type="button" class="btn btn-black" data-toggle="modal" data-target="#editUserName">
                    <fmt:message key="tables.rows.modal.edit_activity.label"/>
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
                                <h5 class="modal-title"><fmt:message key="tables.rows.modal.edit_activity.label"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <label><fmt:message key="entities.fields.name"/></label>
                                <input type="text" name="name" value="${requestScope.userToShow.name}" required>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.save"/> </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <br>
        <label>
            <fmt:message key="entities.fields.surname"/>:
            <input type="text" value="${requestScope.userToShow.surname}" disabled>
            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#editUserSurname">
                <fmt:message key="tables.rows.modal.edit_activity.label"/>
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
                            <h5 class="modal-title"><fmt:message key="tables.rows.modal.edit_activity.label"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <label><fmt:message key="entities.fields.surname"/></label>
                            <input type="text" name="surname" value="${requestScope.userToShow.surname}" required>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                            <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.save"/> </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <br>
        <label>
            <fmt:message key="entities.fields.activities_count"/>: ${requestScope.userToShow.info.activitiesCount}
        </label>

        <br>
        <label>
            <fmt:message key="entities.fields.total_time_spent"/>: <my:convert minutes="${requestScope.userToShow.info.totalTime}"/>
        </label>
<br>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="manageUsersActivities">
            <input type="hidden" name="uId" value="${requestScope.userToShow.id}">
            <button type="submit" class="btn btn-black"><fmt:message key="user_detailed_jsp.manage_activities_pt1"/> ${requestScope.userToShow.login}<fmt:message key="user_detailed_jsp.manage_activities_pt2"/></button>
        </form>
    </div>
</my:htmlCarcass>
