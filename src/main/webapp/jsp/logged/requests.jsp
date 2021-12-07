<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="logged"/>
<ut:set-pagination-query/>
<my:html-carcass title="${sessionScope.user.login} - requests">
    <form action="${root}/controller">
        <input type="hidden" name="command" value="showUsersRequests">
        <input type="hidden" name="page" value="${requestScope.page - 1}">
        <input type="hidden" name="pageSize" value="5">
        <c:choose>
            <c:when test="${requestScope.page - 1 > 0}">
                <button type="submit" class="btn btn-black">Prev</button>
            </c:when>
            <c:otherwise>
                <button type="submit" class="btn btn-black" disabled>Prev</button>
            </c:otherwise>
        </c:choose>
    </form>
    <form action="${root}/controller">
        <input type="hidden" name="command" value="showUsersRequests">
        <input type="hidden" name="page" value="${requestScope.page + 1}">
        <input type="hidden" name="pageSize" value="5">
        <c:choose>
            <c:when test="${requestScope.page < requestScope.pageCount}">
                <button type="submit" class="btn btn-black">Next</button>
            </c:when>
            <c:otherwise>
                <button type="submit" class="btn btn-black" disabled>Next</button>
            </c:otherwise>
        </c:choose>
    </form>
    <table class="table">
        <thead>
        <tr>
            <c:if test="${sessionScope.user.role eq 'admin'}">
                <th scope="col">user login</th>
            </c:if>
            <th scope="col">activity name</th>
            <th scope="col">time spent</th>
            <th scope="col">request type</th>
            <th scope="col">options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="request" items="${requestScope.requests}">
            <tr>
                <c:if test="${sessionScope.user.role eq 'admin'}">
                    <td>${request.value}</td>
                </c:if>
                <td>${request.key.name}</td>
                <td><ut:convert minutes="${request.key.minutesSpent}" minutesLabel="mins" hoursLabel="hrs"/></td>
                <td>
                    <c:choose>
                        <c:when test="${request.key.accepted eq false}">
                            accept
                        </c:when>
                        <c:otherwise>
                            abandon
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
                                <button type="submit" class="btn btn-black">Accept</button>
                            </form>
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="denyRequest">
                                <input type="hidden" name="aId" value="${request.key.activityId}"/>
                                <input type="hidden" name="uId" value="${request.key.userId}"/>
                                <button type="submit" class="btn btn-black">Deny</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                        <form action="${root}/controller" method="post">
                            <input type="hidden" name="command" value="cancelRequest">
                            <input type="hidden" name="aId" value="${request.key.activityId}"/>
                            <input type="hidden" name="uId" value="${request.key.userId}"/>
                            <button type="submit" class="btn btn-black">Cancel</button>
                        </form>
                        </c:otherwise>
                    </c:choose>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</my:html-carcass>
