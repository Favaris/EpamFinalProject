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
                    Create new activity
                </button>

                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showCategoriesPage"/>
                    <button type="submit" class="btn btn-black">Manage categories</button>
                </form>
            </c:if>
            <div class="sorting-panel container">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showActivitiesPage"/>
                    <input type="hidden" name="page" value="1">
                    <input type="hidden" name="pageSize" value="5">
                    <label>Sort by:</label><br>
                    <c:choose>
                        <c:when test="${'activityName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="activityName" id="sortByName" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="activityName" id="sortByName">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByName">name</label><br>
                    <c:choose>
                        <c:when test="${'categoryName'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="categoryName" id="sortByCategory">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByCategory">category</label><br>
                    <c:choose>
                        <c:when test="${'usersCount'.equals(requestScope.orderBy)}">
                            <input type="radio" name="orderBy" value="usersCount" id="sortByUserAmount" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="orderBy" value="usersCount" id="sortByUserAmount">
                        </c:otherwise>
                    </c:choose>
                    <label for="sortByUserAmount">user amount</label><br>
                    <label>Filter by:</label><br>
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
                    <button type="submit" class="btn btn-black">OK</button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">

        <my:paginationNavigation command="showActivitiesPage"/>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">name</th>
            <th scope="col">category</th>
            <th scope="col">description</th>
            <th scope="col">amount of users</th>
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
                                Add
                            </button>
                            <div class="modal fade" id="confirmActivityAddition" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Confirm action</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            Do you really want to add this activity?
                                            <h5>
                                                Warn: this action will only send a request to admin for adding this activity to your account.
                                                Only admin can decide what activity you should have.
                                            </h5>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                            <form action="${pageContext.request.contextPath}/controller" method="post">
                                                <input type="hidden" name="command" value="requestActivityAddition"/>
                                                <input type="hidden" name="uId" value="${sessionScope.user.id}"/>
                                                <input type="hidden" name="aId" value="${activity.id}"/>
                                                <button type="submit" class="btn btn-black">Add</button>
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
                                Delete
                            </button>
                            <div class="modal fade" id="submitActivityDeletion${activity.id}" tabindex="-1" role="dialog" aria-labelledby="Submit deletion" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel">Are you sure?</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            Are you sure you want to delete this activity? This will lead to deletion of this activity from all users.
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                            <form action="${root}/controller" method="post">
                                                <input type="hidden" name="command" value="deleteActivity"/>
                                                <input type="hidden" name="id" value="${activity.id}"/>
                                                <button type="submit" class="btn btn-black">Delete</button>
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
                        <h5 class="modal-title">Create new activity</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Name</label>
                            <input type="text" name="name" class="form-control" required="required" value="${sessionScope.invalidAddActivity.name}"/>
                        </div>
                        <div class="form-group">
                            <label>Description</label>
                            <textarea rows="5" cols="60" name="description" required>${sessionScope.invalidAddActivity.description}</textarea>
                        </div>
                        <div class="form-group">
                            <label>Category</label>
                            <select name="cId">
                                <c:forEach var="category" items="${requestScope.categories}">
                                    <option value="${category.id}" id="${category.id}" ${sessionScope.invalidAddActivity.category.equals(category) ? 'selected' : ''}>${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <c:if test="${not empty sessionScope.invalidFields}">
                            You have invalid fields: ${sessionScope.invalidFields}
                            <c:remove var="invalidFields" scope="session"/>
                        </c:if>
                        ${sessionScope.activityAddErrMsg}
                        <c:remove var="activityAddErrMsg" scope="session"/>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-black">Save</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</my:htmlCarcass>
<c:remove var="err_msg" scope="session"/>
<c:remove var="invalidAddActivity" scope="session"/>
<c:remove var="invalidEditActivity" scope="session"/>