<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>

<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:html-carcass title="${sessionScope.user.login} - manage categories">
    <div class="managing sidenav">
        <div class="login-main-text">
            <form>
            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#createNewCategoryAction">
                Add new category
            </button>
            </form>

            <form action="${root}/controller">
                <input type="hidden" name="command" value="showActivitiesPage"/>
                <button type="submit" class="btn btn-black">Manage activities</button>
            </form>
        </div>
    </div>
    <div class="tables">
    ${sessionScope.err_msg}
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showCategoriesPage">
            <input type="hidden" name="page" value="${requestScope.page - 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${requestScope.page - 1 > 0}">
                    <button type="submit" class="btn btn-black">Prev</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Prev</button>
                </c:otherwise>
            </c:choose>
        </form>
        <form action="${root}/controller">
            <input type="hidden" name="command" value="showCategoriesPage">
            <input type="hidden" name="page" value="${requestScope.page + 1}">
            <input type="hidden" name="pageSize" value="5">
            <c:choose>
                <c:when test="${requestScope.page < requestScope.pageCount}">
                    <button type="submit" class="btn btn-black">Next</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-black" disabled>Next</button>
                </c:otherwise>
            </c:choose>
        </form>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">name</th>
            <th scope="col">options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="category" items="${sessionScope.categories}">
            <tr>
                <td>${category.name}</td>
                <td>
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#${'editCategoryModal'.concat(category.id)}">
                        Edit
                    </button>
                    <div class="modal fade" id="${'editCategoryModal'.concat(category.id)}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="updateCategory">
                                <input type="hidden" name="id" value="${category.id}">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Edit</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <label>Name</label>
                                    <input type="text" name="name" value="${category.name}" required>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                    <button type="submit" class="btn btn-black">Save</button>
                                </div>
                            </div>
                            </form>
                        </div>
                    </div>

                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#confirmCategoryDeletion">
                        Delete
                    </button>
                    <div class="modal fade" id="confirmCategoryDeletion" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Confirm action</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    Do you really want to delete this category?
                                    <h5>
                                        Warn: Category can be deleted only if there are no activities with it.
                                        You must make sure that there are no activities of that category.
                                    </h5>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                    <form action="${root}/controller" method="post">
                                        <input type="hidden" name="command" value="deleteCategory"/>
                                        <input type="hidden" name="id" value="${category.id}"/>
                                        <button type="submit" class="btn btn-black">Delete</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>

    <div class="modal fade" id="createNewCategoryAction" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form action="${root}/controller" method="post">
                    <input type="hidden" name="command" value="addCategory"/>
                    <div class="modal-header">
                        <h5 class="modal-title">Add new category</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <label>Set a name for new category:</label>
                        <input type="text" name="name" required/>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-black">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</my:html-carcass>
<c:remove var="err_msg" scope="session"/>