<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<my:html-carcass title="${sessionScope.user.login} - error occured">
    ${sessionScope.err_msg}
</my:html-carcass>
<c:remove var="err_msg" scope="session"/>