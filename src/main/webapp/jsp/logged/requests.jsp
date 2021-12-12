<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="logged"/>

<my:htmlCarcass title="${sessionScope.user.login} - requests">
    <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
    <my:paginationNavigation command="showUsersRequests"/>
    <table class="table">
        <thead>
        <tr>
            <c:if test="${sessionScope.user.role eq 'admin'}">
                <th scope="col"><fmt:message key="tables.titles.user_login"/> </th>
            </c:if>
            <th scope="col"><fmt:message key="tables.titles.activity_name"/> </th>
            <th scope="col"><fmt:message key="tables.titles.time_spent"/></th>
            <th scope="col"><fmt:message key="tables.titles.request_type"/></th>
            <th scope="col"><fmt:message key="tables.titles.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="request" items="${requestScope.requests}">
            <tr>
                <c:if test="${sessionScope.user.role eq 'admin'}">
                    <td>${request.value}</td>
                </c:if>
                <td>${request.key.name}</td>
                <td><my:convert minutes="${request.key.minutesSpent}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${request.key.accepted eq false}">
                            <fmt:message key="tables.rows.accept_type"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="tables.rows.abandonment_type"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${sessionScope.user.role eq 'admin'}">
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="acceptRequest">
                                <input type="hidden" name="aId" value="${request.key.activityId}"/>
                                <input type="hidden" name="uId" value="${request.key.userId}"/>
                                <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.accept_action"/></button>
                            </form>
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="denyRequest">
                                <input type="hidden" name="aId" value="${request.key.activityId}"/>
                                <input type="hidden" name="uId" value="${request.key.userId}"/>
                                <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.deny_action"/></button>
                            </form>
                        </c:when>
                        <c:otherwise>
                        <form action="${root}/controller" method="post">
                            <input type="hidden" name="command" value="cancelRequest">
                            <input type="hidden" name="aId" value="${request.key.activityId}"/>
                            <input type="hidden" name="uId" value="${request.key.userId}"/>
                            <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.cancel_action"/></button>
                        </form>
                        </c:otherwise>
                    </c:choose>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>
        <div class="col-1"></div>

    </div>
</my:htmlCarcass>
