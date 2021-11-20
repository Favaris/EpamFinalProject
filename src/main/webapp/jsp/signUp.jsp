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
        <p>Sign up page</p>
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="command" value="signUp" />
                <div class="form-group">
                    <label>Login</label>
                    <input type="text" name="login" required="required" class="form-control" minlength="4" maxlength="16" pattern="[A-Za-z]{4,16}?"/>
                    <c:if test="${not empty requestScope.loginErrorMessage}">
                        <br/>
                        <h3>${requestScope.loginErrorMessage}</h3>
                    </c:if>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" required="required" minlength="4" maxlength="32"/>
                    <c:if test="${not empty requestScope.passwordErrorMessage}">
                        <br/>
                        <h3>${requestScope.passwordErrorMessage}</h3>
                    </c:if>
                </div>
                <div class="form-group">
                    <label>Confirm password</label>
                    <input type="password" class="form-control" required="required" minlength="4" maxlength="32"/>
                </div>
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="name" class="form-control" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})"/>
                    <c:if test="${not empty requestScope.nameErrorMessage}">
                        <br/>
                        <h3>${requestScope.nameErrorMessage}</h3>
                    </c:if>
                </div>
                <div class="form-group">
                    <label>Surname</label>
                    <input type="text" name="surname" class="form-control" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})"/>
                    <c:if test="${not empty requestScope.surnameErrorMessage}">
                        <br/>
                        <h3>${requestScope.surnameErrorMessage}</h3>
                    </c:if>
                </div>

                <button type="submit" class="btn btn-black">Sign up</button>
                <button type="button" onclick="location.href='${pageContext.request.contextPath}/jsp/signIn.jsp';" class="btn btn-secondary">Or sign in</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
