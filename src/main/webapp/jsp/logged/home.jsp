<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="logged"/>
<my:htmlCarcass title="${sessionScope.user.login} - home">
    <div class="container text-left">
        <div class="container" style="margin-top:30px">
            <div class="row">
                <div class="col-sm-12">
                    <h1 class="text-center"><fmt:message key="home_jsp.welcome_title"/> , ${sessionScope.user.name} ${sessionScope.user.surname}!</h1>
                    <c:choose>
                        <c:when test="${sessionScope.user.role eq 'user'}">
                            <h3 class="mt-5"><fmt:message key="home_jsp.ongoing_activities_pt1"/> ${sessionScope.user.info.activitiesCount} <fmt:message key="home_jsp.ongoing_activities_pt2"/>.</h3>
                            <p><fmt:message key="home_jsp.acces_activities"/> <a href="${root}/controller?command=showRunningActivities"><fmt:message key="home_jsp.here"/></a>. </p>
                            <p><fmt:message key="home_jsp.request_addition"/> <a href="${root}/controller?command=showActivitiesPage"><fmt:message key="home_jsp.here"/></a>.</p>
                            <p><fmt:message key="home_jsp.user_manage_requests"/> <a href="${root}/controller?command=showUsersRequests"><fmt:message key="home_jsp.here"/></a>.</p>
                            <h3 class="mt-5"><fmt:message key="home_jsp.in_total_spent_pt1"/> <my:convert minutes="${sessionScope.user.info.totalTime}"/> <fmt:message key="home_jsp.in_total_spent_pt2"/>!</h3>
                        </c:when>
                        <c:otherwise>
                            <h3 class="mt-5"><fmt:message key="home_jsp.manage_activities"/>  <a href="${root}/controller?command=showActivitiesPage"><fmt:message key="home_jsp.here"/></a>.</h3>
                            <p class="my-3"><fmt:message key="home_jsp.or"/></p>
                            <h3 ><fmt:message key="home_jsp.manage_requests"/>  <a href="${root}/controller?command=showUsersRequests"><fmt:message key="home_jsp.here"/></a>.</h3>
                            <p class="my-3"><fmt:message key="home_jsp.or"/></p>
                            <h3 ><fmt:message key="home_jsp.manage_users"/> <a href="${root}/controller?command=showAllUsers"><fmt:message key="home_jsp.here"/></a>.</h3>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</my:htmlCarcass>
