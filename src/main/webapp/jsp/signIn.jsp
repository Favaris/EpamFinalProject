<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="guest"/>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<my:htmlCarcass title="Time accounting">
    <div class="sidenav login-sidenav">
        <div class="login-main-text">
            <%@include file="/WEB-INF/jspf/changeLocaleForm.jspf" %>
            <h2><fmt:message key="sign_in_jsp.sidenav.label"/></h2>
            <p><fmt:message key="sign_in_jsp.sidenav.p"/></p>
        </div>
    </div>
    <div class="main">
        <div class="col-md-6 col-sm-12">
            <div class="login-form">
                <form action="${root}/controller" method="post">
                    <input type="hidden" name="command" value="signIn"/>
                    <div class="form-group">
                        <label><fmt:message key="sign_in_jsp.main.login_label"/></label>
                        <input type="text" name="login" class="form-control" required="required" placeholder="<fmt:message key="sign_in_jsp.main.login_label"/>" value="${sessionScope.invalidLogin}"/>
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="sign_in_jsp.main.password_label"/></label>
                        <input type="password" name="password" class="form-control" required="required" placeholder="<fmt:message key="sign_in_jsp.main.password_label"/>"/>
                    </div>
                    <c:if test="${sessionScope.err_msg != null}">
                        ${sessionScope.err_msg} <br>
                        <c:remove var="err_msg" scope="session"/>
                    </c:if>
                    <button type="submit" class="btn btn-black"><fmt:message key="sign_in_jsp.main.sign_in_label"/></button>
                    <button type="button" onclick="location.href='${root}/jsp/signUp.jsp';" class="btn btn-secondary"><fmt:message key="sign_in_jsp.main.or_sign_up_label"/></button>
                </form>
            </div>
        </div>
    </div>
</my:htmlCarcass>
<c:remove var="invalidLogin" scope="session"/>
