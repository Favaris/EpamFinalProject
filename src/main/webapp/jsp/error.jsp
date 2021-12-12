<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<my:htmlCarcass title="${sessionScope.user.login} - error occured">
    <div class="row">
        <div class="col-2"></div>
        <div class="col-8">
            <h1><fmt:message key="error_jsp.error_notification"/></h1>
            <h2><fmt:message key="error_jsp.error_message"/>: ${sessionScope.err_msg}</h2>
        </div>
        <div class="col-2"></div>
    </div>
</my:htmlCarcass>
<c:remove var="err_msg" scope="session"/>