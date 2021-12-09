<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="user"/>
<ut:set-pagination-query includeSorting="true" includeFiltering="true"/>
<my:html-carcass title="${sessionScope.user.login} - your activities">
    <div class="managing sidenav">
        <div class="login-main-text">
            <div class="sorting-panel container">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showRunningActivities"/>
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="pageSize" value="5">
                    <label>Sort by:</label><br>
                    <c:choose>
                        <c:when test="${'activityName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="activityName" id="sortByName" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="activityName" id="sortByName">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByName">name</label><br>
                    <c:choose>
                        <c:when test="${'categoryName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByCategory">category</label><br>
                    <c:choose>
                        <c:when test="${'timeSpent'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="timeSpent" id="sortByTimeSpent" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="timeSpent" id="sortByTimeSpent">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByTimeSpent">time spent</label><br>
                    <label>Filter by:</label><br>
                    <c:choose>
                        <c:when test="${requestScope.filterBy != null}">
                            <c:forEach var="category" items="${requestScope.categories}">

                                <c:set var="contains" value="${false}"/>
                                <c:forEach var="catId" items="${requestScope.filterBy}">
                                    <c:if test="${not contains}">
                                        <c:set var="contains" value="${catId.equals(category.id.toString())}"/>
                                    </c:if>
                                </c:forEach>

                                <c:choose>
                                    <c:when test="${contains}">
                                        <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" checked>
                                        <label for="${category.id}">${category.name}</label><br>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}">
                                        <label for="${category.id}">${category.name}</label><br>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="category" items="${requestScope.categories}">
                                <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" checked>
                                <label for="${category.id}">${category.name}</label><br>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    <button type="submit" class="btn btn-black">OK</button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showRunningActivities">
            <input type="hidden" name="page" value="${requestScope.page - 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${empty requestScope.orderBy}">
                    <input type="hidden" name="orderBy" value="activityName">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${requestScope.page - 1 > 0}">
                    <button type="submit" class="btn btn-black">Prev</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Prev</button>
                </c:otherwise>
            </c:choose>
            <c:forEach var="catId" items="${requestScope.filterBy}">
                <input type="hidden" name="filterBy" value="${catId}">
            </c:forEach>
        </form>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showRunningActivities">
            <input type="hidden" name="page" value="${requestScope.page + 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${empty requestScope.orderBy}">
                    <input type="hidden" name="orderBy" value="activityName">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${requestScope.page < requestScope.pageCount}">
                    <button type="submit" class="btn btn-black">Next</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Next</button>
                </c:otherwise>
            </c:choose>
            <c:forEach var="catId" items="${requestScope.filterBy}">
                <input type="hidden" name="filterBy" value="${catId}">
            </c:forEach>
        </form>
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
    </div>
</my:html-carcass>
<c:remove var="runningActivities"/>