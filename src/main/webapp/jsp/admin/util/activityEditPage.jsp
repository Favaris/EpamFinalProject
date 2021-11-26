<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - edit activity ${sessionScope.activityToEdit.name}">
    <div>
        <form action="${root}/controller" method="post">
            <input type="hidden" name="command" value="updateActivity">
            <input type="hidden" name="id" value="${sessionScope.activityToEdit.id}">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" class="form-control" required="required" value="${sessionScope.activityToEdit.name}"/>
            </div>
            <div class="form-group">
                <label>Description</label>
                <input type="text" name="description" class="form-control" required="required" value="${sessionScope.activityToEdit.description}"/>
            </div>
            <div class="form-group">
                <c:forEach var="category" items="${sessionScope.categories}">
                    <c:choose>
                        <c:when test="${sessionScope.activityToEdit.categories.contains(category)}">
                            <input type="checkbox" name="categoriesIds" value="${category.id}" checked id="${category.id}">
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="categoriesIds" value="${category.id}" id="${category.id}">
                        </c:otherwise>
                    </c:choose>
                    <label for="${category.id}">${category.name}</label> <br>
                </c:forEach>
            </div>
            <c:if test="${requestScope.err_msg != null}">
                ${requestScope.err_msg} <br>
            </c:if>
            <button type="submit" class="btn btn-black">Save</button>
        </form>
    </div>
</my:html-carcass>
