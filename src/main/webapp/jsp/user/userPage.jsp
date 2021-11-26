<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="user"/>
<my:html-carcass title="${sessionScope.user.login} - home">
    login: ${sessionScope.user.login}
    <br/>
    pass: ${sessionScope.user.password}
    <br/>
    name: ${sessionScope.user.name}
    <br/>
    surname: ${sessionScope.user.surname}
    <br/>
    role: ${sessionScope.user.role}
    <br/>
</my:html-carcass>
