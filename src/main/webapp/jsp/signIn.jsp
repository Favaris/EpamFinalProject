<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<!DOCTYPE html>
<html>
<my:header title="Time accounting"/>
<body>
<div class="sidenav">
    <div class="login-main-text">
        <h2>Time accounting</h2>
        <p>Sign in or sign up from here to access.</p>
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="command" value="signIn"/>
                <div class="form-group">
                    <label>Login</label>
                    <input type="text" name="login" class="form-control" required="required" placeholder="Login"/>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" required="required" placeholder="Password"/>
                </div>
                <c:if test="${requestScope.err_msg != null}">
                    ${requestScope.err_msg} <br>
                </c:if>
                <button type="submit" class="btn btn-black">Sign in</button>
                <button type="button" onclick="location.href='${pageContext.request.contextPath}/jsp/signUp.jsp';" class="btn btn-secondary">Or sign up</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
