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
                    <h1 class="text-center">Welcome back, ${sessionScope.user.name} ${sessionScope.user.surname}!</h1>
                    <c:choose>
                        <c:when test="${sessionScope.user.role eq 'user'}">
                            <h3 class="mt-5">There are currently ${sessionScope.user.info.activitiesCount} ongoing activities on your account.</h3>
                            <p>You can access them <a href="${root}/controller?command=showRunningActivities">here</a>. </p>
                            <p>You can request the addition of new activities on your account <a href="${root}/controller?command=showActivitiesPage">here</a>.</p>
                            <p>Or you can manage your requests <a href="${root}/controller?command=showUsersRequests">here</a>.</p>
                            <h3 class="mt-5">In total, you have spent <ut:convert minutes="${sessionScope.user.info.totalTime}" minutesLabel="minutes" hoursLabel="hours and"/> doing these activities!</h3>
                        </c:when>
                        <c:otherwise>
                            <h3 class="mt-5">You can manage activities  <a href="${root}/controller?command=showActivitiesPage">here</a>.</h3>
                            <p class="my-3">or</p>
                            <h3 >You can manage users' requests  <a href="${root}/controller?command=showUsersRequests">here</a>.</h3>
                            <p class="my-3">or</p>
                            <h3 >You can manage users <a href="${root}/controller?command=showAllUsers">here</a>.</h3>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</my:htmlCarcass>
