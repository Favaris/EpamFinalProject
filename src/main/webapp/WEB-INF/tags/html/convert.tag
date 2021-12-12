<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="minutes" required="true" type="java.lang.Integer" %>

<%@include file="/WEB-INF/jspf/taglibs.jspf" %>

<c:set var="hours" value="${minutes / 60}"/>
<c:set var="minutes" value="${minutes % 60}"/>

${hours.toString().split("\\.")[0]} <fmt:message key="tables.rows.hours_label"/> ${minutes} <fmt:message key="tables.rows.minutes_label"/>