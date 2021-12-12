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
                        <label><fmt:message key="sign_in_jsp.main.login_label"/></label>
                        <input type="text" placeholder="<fmt:message key="sign_in_jsp.main.login_label"/>" name="login" required="required" class="form-control" minlength="4" maxlength="16" pattern="[A-Za-z0-9]{4,16}?" value="${sessionScope.invalidUser.login}"/>

                    </div>
                    <div class="form-group">
                        <label><fmt:message key="sign_in_jsp.main.password_label"/></label>
                        <input placeholder="<fmt:message key="sign_in_jsp.main.password_label"/>" type="password" id="pass1" name="password" class="form-control" required="required" minlength="4" maxlength="32"/>

                    </div>
                    <div class="form-group">
                        <label><fmt:message key="sign_up_jsp.main.confirm_password_label"/></label>
                        <input placeholder="<fmt:message key="sign_up_jsp.main.confirm_password_label"/>" type="password" id="pass2" class="form-control" required="required" minlength="4" maxlength="32"/>
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="sign_up_jsp.main.name_label"/></label>
                        <input placeholder="<fmt:message key="sign_up_jsp.main.name_label"/>" type="text" name="name" class="form-control" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})" value="${sessionScope.invalidUser.name}"/>

                    </div>
                    <div class="form-group">
                        <label><fmt:message key="sign_up_jsp.main.surname_label"/></label>
                        <input placeholder="<fmt:message key="sign_up_jsp.main.surname_label"/>" type="text" name="surname" class="form-control" minlength="2" maxlength="30" pattern="([A-Z][a-z]{1,30}|[А-ЯІЇЄЁ][а-яіїєґё]{1,30})" value="${sessionScope.invalidUser.surname}"/>

                    </div>

                    <div class="form-group">
                        <label><fmt:message key="user_add_page_jsp.role"/> </label> <br/>
                        <select name="role">
                            <option value="user" selected><fmt:message key="user_add_page_jsp.role.user"/></option>
                            <option value="admin"><fmt:message key="user_add_page_jsp.role.admin"/></option>
                        </select>
                    </div>
                    <c:if test="${not empty sessionScope.invalidFields}">
                        <fmt:message key="sign_up_jsp.error.invalid_input"/>${sessionScope.invalidFields}
                        <br/>
                        <c:remove var="invalidFields" scope="session"/>
                    </c:if>
                    <c:if test="${not empty sessionScope.err_msg}">
                        <fmt:message key="sign_up_jsp.error.login_taken"/>
                        <br/>
                        <c:remove var="err_msg" scope="session"/>
                    </c:if>
                    <button type="submit" class="btn btn-black"><fmt:message key="user_add_page_jsp.create"/></button>
                </form>
            </div>
        </div>
    </div>
</my:htmlCarcass>
<c:remove var="invalidUser" scope="session"/>