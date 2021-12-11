<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<s:check role="${sessionScope.user.role}"  permission="admin"/>

<my:htmlCarcass title="${sessionScope.user.login} - create new user">
    <div class="managing sidenav">
        <div class="login-main-text">
            <button type="button" class="btn btn-black" onclick="location.href='${root}/controller?command=showAllUsers&${sessionScope.paginationQueryString}';">Go back</button>
        </div>
    </div>
    <div class="main">
        <div class="col-md-3 col-sm-9">
            <div class="login-form">
                <form action="${pageContext.request.contextPath}/controller" method="post">
                    <input type="hidden" name="command" value="addUser" />
                    <div class="form-group">
                        <label>Login</label>
                        <input type="text" name="login" required="required" class="form-control" minlength="4" maxlength="16" pattern="[A-Za-z]{4,16}?" value="${sessionScope.invalidUser.login}"/>
                        <c:if test="${sessionScope.loginErrorMessage != null}">
                            <br/>
                            <h3>Login can be only in latin and can contain numbers</h3>
                            <c:remove var="loginErrorMessage" scope="session"/>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="pass1" name="password" class="form-control" required="required" minlength="4" maxlength="32"/>
                        <c:if test="${sessionScope.passwordErrorMessage != null}">
                            <br/>
                            <h3>Password must be in length from 4 to 32 symbols</h3>
                            <c:remove var="passwordErrorMessage" scope="session"/>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label>Confirm password</label>
                        <input type="password" id="pass2" class="form-control" required="required" minlength="4" maxlength="32"/>
                    </div>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" class="form-control" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})" value="${sessionScope.invalidUser.name}"/>
                        <c:if test="${sessionScope.nameErrorMessage != null}">
                            <br/>
                            <h3>Name can be only in latin or cyrillic</h3>
                            <c:remove var="nameErrorMessage" scope="session"/>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label>Surname</label>
                        <input type="text" name="surname" class="form-control" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})" value="${sessionScope.invalidUser.surname}"/>
                        <c:if test="${sessionScope.surnameErrorMessage != null}">
                            <br/>
                            <h3>Name can be only in latin or cyrillic</h3>
                            <c:remove var="surnameErrorMessage" scope="session"/>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label>Role</label> <br/>
                        <select name="role">
                            <option value="user" selected>user</option>
                            <option value="admin">admin</option>
                        </select>
                        <c:if test="${sessionScope.surnameErrorMessage != null}">
                            <br/>
                            <h3>Name can be only in latin or cyrillic</h3>
                            <c:remove var="surnameErrorMessage" scope="session"/>
                        </c:if>
                    </div>
                    <c:if test="${not empty sessionScope.err_msg}">
                        ${sessionScope.err_msg}
                        <br/>
                        <c:remove var="err_msg" scope="session"/>
                    </c:if>
                    <button type="submit" class="btn btn-black">Create</button>
                </form>
            </div>
        </div>
    </div>
</my:htmlCarcass>
<c:remove var="invalidUser" scope="session"/>