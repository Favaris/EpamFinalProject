<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - add new activity">
    <div>
        <form action="${root}/controller" method="post">
            <input type="hidden" name="command" value="addActivity">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" class="form-control" required="required" value="${sessionScope.invalidActivity.name}"/>
                <c:if test="${sessionScope.activityNameErrorMessage != null}">
                    Name can contain only cyrillic and/or latin letters and separators
                </c:if>
            </div>
            <div class="form-group">
                <label>Description</label>
                <input type="text" name="description" class="form-control" required="required" value="${sessionScope.invalidActivity.description}"/>
                <c:if test="${sessionScope.activityDescErrorMessage != null}">
                    Description length must be at least 1 and not more then 1000 symbols long
                </c:if>
            </div>
            <div class="form-group">
                <c:forEach var="category" items="${categories}">
                    <c:choose>
                        <c:when test="${sessionScope.invalidActivity.categories.contains(category)}">
                            <input type="checkbox" name="categoriesIds" value="${category.id}" id="${category.id}" checked/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="categoriesIds" value="${category.id}" id="${category.id}"/>
                        </c:otherwise>
                    </c:choose>
                    <label for="${category.id}">${category.name}</label> <br>
                </c:forEach>
                <c:if test="${sessionScope.activityCatsErrorMessage != null}">
                    You must choose at least one category
                </c:if>
            </div>
            <c:if test="${sessionScope.err_msg != null}">
                ${sessionScope.err_msg} <br>
            </c:if>
            <button type="submit" class="btn btn-black">Add</button>
        </form>
    </div>
</my:html-carcass>
<c:remove var="err_msg" scope="session"/>
<c:remove var="invalidActivity" scope="session"/>
<c:remove var="activityNameErrorMessage" scope="session"/>
<c:remove var="activityDescErrorMessage" scope="session"/>
<c:remove var="activityCatsErrorMessage" scope="session"/>
