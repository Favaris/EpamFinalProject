<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<s:check role="${sessionScope.user.role}"  permission="admin"/>

<my:html-carcass title="${sessionScope.user.login} - edit activity ${sessionScope.activityToEdit.name}">
    <div class="container">

        <form action="${root}/controller" method="post">
            <input type="hidden" name="command" value="updateActivity">
            <c:choose>
                <c:when test="${not empty sessionScope.invalidActivity}">
                    <input type="hidden" name="id" value="${sessionScope.invalidActivity.id}">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" class="form-control" required="required" value="${sessionScope.invalidActivity.name}"/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input type="text" name="description" class="form-control" required="required" value="${sessionScope.invalidActivity.description}"/>
                    </div>
                    <div class="form-group">
                        <c:forEach var="category" items="${requestScope.categories}">
                            <c:choose>
                                <c:when test="${sessionScope.invalidActivity.category.equals(category)}">
                                    <input type="radio" name="cId" value="${category.id}" checked id="${category.id}">
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="cId" value="${category.id}" id="${category.id}">
                                </c:otherwise>
                            </c:choose>
                            <label for="${category.id}">${category.name}</label> <br>
                        </c:forEach>
                    </div>
                    <label>Activity with this name is taken. Try another one.</label>
                    <c:remove var="invalidActivity" scope="session"/>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="id" value="${requestScope.activityToEdit.id}">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" class="form-control" required="required" value="${requestScope.activityToEdit.name}"/>
                        <c:if test="${sessionScope.activityNameErrorMessage != null}">
                            Name can contain only cyrillic and/or latin letters and separators
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input type="text" name="description" class="form-control" required="required" value="${requestScope.activityToEdit.description}"/>
                        <c:if test="${sessionScope.activityDescErrorMessage != null}">
                            Description length must be at least 1 and not more then 1000 symbols long
                        </c:if>
                    </div>
                    <div class="form-group">
                        <c:forEach var="category" items="${requestScope.categories}">
                            <c:choose>
                                <c:when test="${requestScope.activityToEdit.category.equals(category)}">
                                    <input type="radio" name="cId" value="${category.id}" checked id="${category.id}">
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="cId" value="${category.id}" id="${category.id}">
                                </c:otherwise>
                            </c:choose>
                            <label for="${category.id}">${category.name}</label> <br>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
            <button type="submit" class="btn btn-black">Save</button>
        </form>
    </div>
</my:html-carcass>
<c:remove var="err_msg" scope="session"/>
<c:remove var="activityNameErrorMessage" scope="session"/>
<c:remove var="activityDescErrorMessage" scope="session"/>
<c:remove var="activityCatsErrorMessage" scope="session"/>