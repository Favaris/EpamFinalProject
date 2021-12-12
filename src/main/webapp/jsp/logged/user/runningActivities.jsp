<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="user"/>
<ut:set-pagination-query includeSorting="true" includeFiltering="true"/>
<my:htmlCarcass title="${sessionScope.user.login} - your activities">
    <div class="managing sidenav">
        <div class="login-main-text">
            <div class="sorting-panel container">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showRunningActivities"/>
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="pageSize" value="5">
                    <label><fmt:message key="activities_jsp.sidenav.sort_by.label"/> :</label><br>
                    <c:choose>
                        <c:when test="${'activityName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="activityName" id="sortByName" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="activityName" id="sortByName">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByName"><fmt:message key="activities_jsp.sidenav.sort_by.name"/></label><br>
                    <c:choose>
                        <c:when test="${'categoryName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByCategory"><fmt:message key="activities_jsp.sidenav.sort_by.category"/></label><br>
                    <c:choose>
                        <c:when test="${'timeSpent'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="timeSpent" id="sortByTimeSpent" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="timeSpent" id="sortByTimeSpent">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByTimeSpent"><fmt:message key="activities_jsp.sidenav.sort_by.time_spent"/></label><br>
                    <label><fmt:message key="activities_jsp.sidenav.filter_by.label"/>:</label><br>
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

                    <button type="submit" class="btn btn-black"><fmt:message key="activities_jsp.sidenav.ok"/></button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">
        <my:paginationNavigation command="showRunningActivities"/>

    <table class="table">
        <thead>
        <tr>
            <th scope="col"><fmt:message key="tables.titles.activity_name"/></th>
            <th scope="col"><fmt:message key="tables.titles.category"/></th>
            <th scope="col"><fmt:message key="tables.titles.description"/></th>
            <th scope="col"><fmt:message key="tables.titles.time_spent"/></th>
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
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#updateTime${activity.id}">
                        <fmt:message key="tables.rows.modal.add_time.label"/>
                    </button>
                    <div class="modal fade" id="updateTime${activity.id}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="updateSpentTime"/>
                                <input type="hidden" name="uId" value="${sessionScope.user.id}"/>
                                <input type="hidden" name="aId" value="${activity.id}">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title"><fmt:message key="tables.rows.modal.add_time.label"/></h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <label><fmt:message key="tables.rows.modal.add_time.message"/>.
                                            <strong><fmt:message key="tables.rows.modal.add_time.message_strong"/>.</strong>
                                        </label>
                                        <br>
                                        <label><fmt:message key="tables.rows.modal.add_time.hours"/>:</label>
                                        <input type="number" name="hours" min="0" value="0"/> <br>
                                        <label><fmt:message key="tables.rows.modal.add_time.minutes"/>:</label>
                                        <input type="number" name="minutes" min="0" max="60" value="0"/>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                        <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.save"/></button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#requestActivityAbandonment${activity.id}">
                        <fmt:message key="tables.rows.modal.request_abandon.label"/>
                    </button>
                    <div class="modal fade" id="requestActivityAbandonment${activity.id}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title"><fmt:message key="tables.rows.modal.request_abandon.label"/></h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <fmt:message key="tables.rows.modal.request_abandon.question"/><br>
                                        <strong>
                                            <fmt:message key="tables.rows.modal.request_abandon.warn"/>
                                        </strong>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                        <form action="${root}/controller" method="post">
                                            <input type="hidden" name="command" value="requestActivityAbandonment">
                                            <input type="hidden" name="aId" value="${activity.id}">
                                            <input type="hidden" name="uId" value="${sessionScope.user.id}">
                                            <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.confirm"/></button>
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
</my:htmlCarcass>
<c:remove var="runningActivities"/>