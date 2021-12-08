<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="logged"/>
<my:html-carcass title="${sessionScope.user.login} - home">
    you are logged as ${sessionScope.user.login}.
<c:if test="${sessionScope.user.role eq 'user'}">
    <br>
    You currently have ${sessionScope.user.info.activitiesCount} activities on your count
    <br>
    You have spent <ut:convert minutes="${sessionScope.user.info.totalTime}" minutesLabel="mins" hoursLabel="hrs"/> on them in total!
</c:if>
</my:html-carcass>
