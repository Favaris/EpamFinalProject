<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}" permission="logged"/>
<my:html-carcass title="${sessionScope.user.login} - activities">
    <div class="managing sidenav">
        <div class="login-main-text">
            <c:if test="${sessionScope.user.role eq 'admin'}">
                <form action="${root}/controller">
                    <input type="hidden" name="command" value="showActivityAddPage"/>
                    <button type="submit" class="btn btn-black">Add new activity</button>
                </form>
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
                    <c:choose>
                        <c:when test="${requestScope.filterBy != null}">
                            <c:forEach var="category" items="${requestScope.categories}">

                                    <c:set var="contains" value="${false}"/>
                                    <c:forEach var="catId" items="${requestScope.filterBy}">
                                        <c:if test="${not contains}">
                                            <c:set var="contains" value="${catId.equals(category.id.toString())}"/>
                                        </c:if>
                                    </c:forEach>

                                    <c:choose>
                                        <c:when test="${contains}">
                                            <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" checked>
                                            <label for="${category.id}">${category.name}</label><br>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}">
                                            <label for="${category.id}">${category.name}</label><br>
                                        </c:otherwise>
                                    </c:choose>

                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="category" items="${requestScope.categories}">
                                <input type="checkbox" name="filterBy" value="${category.id}" id="${category.id}" checked>
                                <label for="${category.id}">${category.name}</label><br>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    <button type="submit" class="btn btn-black">OK</button>
                </form>
            </div>
        </div>
    </div>
    <div class="tables">
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showActivitiesPage">
            <input type="hidden" name="page" value="${requestScope.page - 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${empty requestScope.orderBy}">
                    <input type="hidden" name="orderBy" value="activityName">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${requestScope.page - 1 > 0}">
                    <button type="submit" class="btn btn-black">Prev</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Prev</button>
                </c:otherwise>
            </c:choose>
            <c:forEach var="catId" items="${requestScope.filterBy}">
                <input type="hidden" name="filterBy" value="${catId}">
            </c:forEach>
        </form>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showActivitiesPage">
            <input type="hidden" name="page" value="${requestScope.page + 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${empty requestScope.orderBy}">
                    <input type="hidden" name="orderBy" value="activityName">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="orderBy" value="${requestScope.orderBy}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${requestScope.page < requestScope.pageCount}">
                    <button type="submit" class="btn btn-black">Next</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Next</button>
                </c:otherwise>
            </c:choose>
            <c:forEach var="catId" items="${requestScope.filterBy}">
                <input type="hidden" name="filterBy" value="${catId}">
            </c:forEach>
        </form>
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
                            <form action="${root}/controller" method="get">
                                <input type="hidden" name="command" value="showActivityEditPage"/>
                                <input type="hidden" name="id" value="${activity.id}"/>
                                <button type="submit" class="btn btn-black">Edit</button>
                            </form>
                            <c:set var="modal_id" value="${'submitActivityDeletion'.concat(activity.id)}"/>
                            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#${pageScope.modal_id}">
                                Delete
                            </button>
                            <div class="modal fade" id="${pageScope.modal_id}" tabindex="-1" role="dialog" aria-labelledby="Submit deletion" aria-hidden="true">
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
</my:html-carcass>
