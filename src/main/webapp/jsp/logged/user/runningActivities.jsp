<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="user"/>
<c:if test="${runningActivities == null}">
    <c:redirect url="${root}/controller?command=showActivitiesPage"/>
</c:if>
<my:html-carcass title="${sessionScope.user.login} - your activities">
    <table class="table">
        <thead>
        <tr>
            <th scope="col">name</th>
            <th scope="col">category</th>
            <th scope="col">description</th>
            <th scope="col">time spent</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="activity" items="${requestScope.runningActivities}">
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
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#${'updateTime'.concat(activity.id)}">
                        Add time
                    </button>
                    <div class="modal fade" id="${'updateTime'.concat(activity.id)}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="updateSpentTime"/>
                                <input type="hidden" name="uId" value="${sessionScope.user.id}"/>
                                <input type="hidden" name="aId" value="${activity.id}">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Update spent time on ${activity.name}</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <label>Enter new time that you have spent. <strong>Warn: this values will be added to your current time span.</strong></label> <br>
                                        <label>Hours:</label>
                                        <input type="number" name="hours" min="0" value="0"/> <br>
                                        <label>Minutes:</label>
                                        <input type="number" name="minutes" min="0" max="60" value="0"/>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-black">Save</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#${'requestActivityAbandonment'.concat(activity.id)}">
                        Request abandonment
                    </button>
                    <div class="modal fade" id="${'requestActivityAbandonment'.concat(activity.id)}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Confirm abandoning ${activity.name}</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure that you want to abandon this activity?<br>
                                        <strong>
                                            Warn: this action will only send a request for abandoning this activity. Only admin can decide whether accept or deny your request.
                                            Remember that you always can cancel this request in window 'Your requests'.
                                        </strong>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                        <form action="${root}/controller" method="post">
                                            <input type="hidden" name="command" value="requestActivityAbandonment">
                                            <input type="hidden" name="aId" value="${activity.id}">
                                            <input type="hidden" name="uId" value="${sessionScope.user.id}">
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
<c:remove var="runningActivities"/>