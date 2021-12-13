<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}" permission="logged"/>
<my:htmlCarcass title="${sessionScope.user.login} - activities">

    <c:if test="${sessionScope.invalidAddActivity != null}">
        <script>
            $(window).on('load',function() {
                $('#addActivityModal').modal('show');
            });
        </script>
    </c:if>

    <c:if test="${sessionScope.invalidEditActivity != null}">
        <script>
            $(window).on('load',function() {
                $('#editActivityModal${sessionScope.invalidEditActivity.id}').modal('show');
            });
        </script>
    </c:if>

    <div class="managing sidenav">
        <div class="login-main-text">
            <c:if test="${sessionScope.user.role eq 'admin'}">

                <button type="button" class="btn btn-black my-3" data-toggle="modal" data-target="#addActivityModal">
                    <fmt:message key="activities_jsp.sidenav.create_new_activity"/>
                </button>

                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showCategoriesPage"/>
                    <button type="submit" class="btn btn-black"><fmt:message key="activities_jsp.sidenav.manage_categories"/></button>
                </form>
            </c:if>
            <div class="sorting-panel container">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showActivitiesPage"/>
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="pageSize" value="5">
                    <label><fmt:message key="activities_jsp.sidenav.sort_by.label"/>:</label><br>
                    <c:choose>
                        <c:when test="${'activityName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="activityName" id="sortByName" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="activityName" id="sortByName">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByName"><fmt:message key="activities_jsp.sidenav.sort_by.name"/></label><br>
                    <c:choose>
                        <c:when test="${'categoryName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByCategory"><fmt:message key="activities_jsp.sidenav.sort_by.category"/></label><br>
                    <c:choose>
                        <c:when test="${'usersCount'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="usersCount" id="sortByUserAmount" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="usersCount" id="sortByUserAmount">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByUserAmount"><fmt:message key="activities_jsp.sidenav.sort_by.user_amount"/></label><br>
                    <label><fmt:message key="activities_jsp.sidenav.filter_by.label"/>:</label><br>
                    <c:forEach var="category" items="${requestScope.categories}">
                        <c:set var="contains" scope="page" value="${false}"/>
                        <c:forEach var="catId" items="${requestScope.filterBy}">
                            <c:if test="${not contains}">
                                <c:set var="contains" value="${catId.equals(category.id.toString())}"/>
                            </c:if>
                        </c:forEach>
                        <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" ${contains ? 'checked' : ''}>
                        <label for="${category.id}">${category.name}</label><br>
                    </c:forEach>
                    <button type="submit" class="btn btn-black"><fmt:message key="activities_jsp.sidenav.ok"/></button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">

        <my:paginationNavigation command="showActivitiesPage"/>
    <table class="table">
        <thead>
        <tr>
            <th scope="col"><fmt:message key="tables.titles.activity_name"/></th>
            <th scope="col"><fmt:message key="tables.titles.category"/></th>
            <th scope="col"><fmt:message key="tables.titles.description"/></th>
            <th scope="col"><fmt:message key="tables.titles.amount_of_users"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="activity" items="${requestScope.activities}">
            <tr>
                <td>${activity.name}</td>
                <td>
                    ${activity.category.name}
                </td>
                <td>${activity.description}</td>
                <td>${activity.usersCount}</td>
                <td>
                    <c:choose>
                        <c:when test="${sessionScope.user.role eq 'user'}">
                            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#confirmActivityAddition">
                                <fmt:message key="tables.rows.modal.add_activity.title"/>
                            </button>
                            <div class="modal fade" id="confirmActivityAddition" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title"><fmt:message key="tables.rows.modal.add_activity.confirm_action"/></h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <fmt:message key="tables.rows.modal.add_activity.question"/>
                                            <h5>
                                                <fmt:message key="tables.rows.modal.add_activity.warn"/>
                                            </h5>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                            <form action="${pageContext.request.contextPath}/controller" method="post">
                                                <input type="hidden" name="command" value="requestActivityAddition"/>
                                                <input type="hidden" name="uId" value="${sessionScope.user.id}"/>
                                                <input type="hidden" name="aId" value="${activity.id}"/>
                                                <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.add_activity.title"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </c:when>
                        <c:otherwise>

                            <c:choose>
                                <c:when test="${activity.equals(sessionScope.invalidEditActivity)}">
                                    <my:activityEditModal activity="${sessionScope.invalidEditActivity}"/>
                                </c:when>
                                <c:otherwise>
                                    <my:activityEditModal activity="${activity}"/>
                                </c:otherwise>
                            </c:choose>

                            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#submitActivityDeletion${activity.id}">
                                <fmt:message key="tables.rows.modal.delete_activity.label"/>
                            </button>
                            <div class="modal fade" id="submitActivityDeletion${activity.id}" tabindex="-1" role="dialog" aria-labelledby="Submit deletion" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="tables.rows.modal.delete_activity.question"/>?</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <fmt:message key="tables.rows.modal.delete_activity.message"/>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                            <form action="${root}/controller" method="post">
                                                <input type="hidden" name="command" value="deleteActivity"/>
                                                <input type="hidden" name="id" value="${activity.id}"/>
                                                <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.delete_activity.label"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    </div>
    <div class="modal fade" id="addActivityModal" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <form action="${root}/controller" method="post">
                <input type="hidden" name="command" value="addActivity">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><fmt:message key="tables.rows.modal.create_activity.label"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label><fmt:message key="entities.fields.name"/></label>
                            <input type="text" name="name" class="form-control" required="required" value="${sessionScope.invalidAddActivity.name}" pattern="[A-ZА-ЯІЇЄЁa-zа-яіїєґё 0-9]{1,30}"/>
                        </div>
                        <div class="form-group">
                            <label><fmt:message key="entities.fields.description"/></label>
                            <textarea rows="5" cols="60" name="description" required>${sessionScope.invalidAddActivity.description}</textarea>
                        </div>
                        <div class="form-group">
                            <label><fmt:message key="entities.fields.category"/></label>
                            <select name="cId">
                                <c:forEach var="category" items="${requestScope.categories}">
                                    <option value="${category.id}" id="${category.id}" ${sessionScope.invalidAddActivity.category.equals(category) ? 'selected' : ''}>${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <c:if test="${not empty sessionScope.invalidFields}">
                            <fmt:message key="error_messages.invalid_input_fields"/>: ${sessionScope.invalidFields}
                            <c:remove var="invalidFields" scope="session"/>
                        </c:if>
                            <c:if test="${sessionScope.activityAddErrMsg != null}">
                                <fmt:message key="error_messages.name_is_taken"/>: ${sessionScope.invalidFields}
                                <c:remove var="activityAddErrMsg" scope="session"/>
                            </c:if>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                        <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.save"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</my:htmlCarcass>
<c:remove var="err_msg" scope="session"/>
<c:remove var="invalidAddActivity" scope="session"/>
<c:remove var="invalidEditActivity" scope="session"/>