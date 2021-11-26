<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="guest"/>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<my:html-carcass title="Time accounting">
    <div class="sidenav">
        <div class="login-main-text">
            <h2>Time accounting</h2>
            <p>Sign in or sign up from here to access.</p>
        </div>
    </div>
    <div class="main">
        <div class="col-md-6 col-sm-12">
            <div class="login-form">
                <form action="${root}/controller" method="post">
                    <input type="hidden" name="command" value="signIn"/>
                    <div class="form-group">
                        <label>Login</label>
                        <input type="text" name="login" class="form-control" required="required" placeholder="Login" value="${sessionScope.invalidLogin}"/>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" class="form-control" required="required" placeholder="Password"/>
                    </div>
                    <c:if test="${sessionScope.err_msg != null}">
                        ${sessionScope.err_msg} <br>
                        <c:remove var="err_msg" scope="session"/>
                    </c:if>
                    <button type="submit" class="btn btn-black">Sign in</button>
                    <button type="button" onclick="location.href='${root}/jsp/signUp.jsp';" class="btn btn-secondary">Or sign up</button>
                </form>
            </div>
        </div>
    </div>
</my:html-carcass>
<c:remove var="invalidLogin" scope="session"/>
