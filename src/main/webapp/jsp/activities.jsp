<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}" permission="logged"/>
<my:html-carcass title="${sessionScope.user.login} - activities">
    <c:if test="${empty sessionScope.activities}">
        <jsp:forward page="${root}/controller?command=downloadActivities"/>
    </c:if>
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
        </div>
    </div>
    <div class="tables">
    <table class="table">
        <thead>
        <tr>
            <th scope="col">name</th>
            <th scope="col">categories</th>
            <th scope="col">description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="activity" items="${sessionScope.activities}">
            <tr>
                <td>${activity.name}</td>
                <td>
                    <c:forEach var="cat" items="${activity.categories}">
                        ${cat.name},
                    </c:forEach>
                </td>
                <td>${activity.description}</td>
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
