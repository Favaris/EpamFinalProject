<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/taglibs.jspf" %>
<%@taglib tagdir="/WEB-INF/tags/html" prefix="my"%>
<%@taglib uri="http://com.prusan.finalproject.security" prefix="s"%>
<%@taglib uri="http://com.prusan.finalproject.util" prefix="ut" %>
<s:check role="${sessionScope.user.role}"  permission="admin"/>
<my:htmlCarcass title="${sessionScope.user.login} - manage categories">

    <c:if test="${sessionScope.invalidAddCategory != null}">
        <script>
            $(window).on('load',function() {
                $('#createNewCategoryAction').modal('show');
            });
        </script>
    </c:if>
    <c:if test="${sessionScope.invalidEditCategory != null}">
        <script>
            $(window).on('load',function() {
                $('#editCategoryModal${sessionScope.invalidEditCategory.id}').modal('show');
            });
        </script>
    </c:if>

    <div class="managing sidenav">
        <div class="login-main-text">
            <form>
            <button type="button" class="btn btn-black" data-toggle="modal" data-target="#createNewCategoryAction">
                <fmt:message key="categories_jsp.sidenav.add_new_category"/>
            </button>
            </form>

            <form action="${root}/controller">
                <input type="hidden" name="command" value="showActivitiesPage"/>
                <button type="submit" class="btn btn-black"><fmt:message key="categories_jsp.sidenav.manage_activities"/></button>
            </form>
        </div>
    </div>
    <div class="tables">

        <my:paginationNavigation command="showCategoriesPage"/>
        ${sessionScope.err_msg}
    <table class="table">
        <thead>
        <tr>
            <th scope="col"><fmt:message key="tables.titles.name"/> </th>
            <th scope="col"><fmt:message key="tables.titles.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="category" items="${sessionScope.categories}">
            <tr>
                <td>${category.name}</td>
                <td>
                    <c:if test="${sessionScope.invalidEditCategory.equals(category)}">
                        <c:set var="category" value="${sessionScope.invalidEditCategory}"/>
                    </c:if>
                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#editCategoryModal${category.id}">
                        <fmt:message key="tables.rows.modal.edit_activity.label"/>
                    </button>
                    <div class="modal show" id="editCategoryModal${category.id}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <form action="${root}/controller" method="post">
                                <input type="hidden" name="command" value="updateCategory">
                                <input type="hidden" name="id" value="${category.id}">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title"><fmt:message key="tables.rows.modal.edit_activity.label"/></h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <label><fmt:message key="entities.fields.name"/> </label>
                                    <input type="text" name="name" value="${category.name}" required>
                                </div>
                                <c:if test="${not empty sessionScope.invalidInputError}">
                                    <fmt:message key="error_messages.invalid_input_fields"/>: ${sessionScope.invalidInputError}
                                    <c:remove var="invalidInputError" scope="session"/>
                                </c:if>
                                 <c:if test="${not empty sessionScope.editCategoryErrMsg}">
                                     <fmt:message key="error_messages.name_is_taken"/>
                                 </c:if>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                    <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.save"/></button>
                                </div>
                            </div>
                            </form>
                        </div>
                    </div>

                    <button type="button" class="btn btn-black" data-toggle="modal" data-target="#confirmCategoryDeletion${category.id}">
                        <fmt:message key="tables.rows.modal.delete_activity.label"/>
                    </button>
                    <div class="modal fade" id="confirmCategoryDeletion${category.id}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title"><fmt:message key="tables.rows.modal.delete_activity.label"/></h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <fmt:message key="tables.rows.modal.delete_category.question"/>
                                    <h5>
                                        <fmt:message key="tables.rows.modal.delete_category.warn"/>
                                    </h5>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/></button>
                                    <form action="${root}/controller" method="post">
                                        <input type="hidden" name="command" value="deleteCategory"/>
                                        <input type="hidden" name="id" value="${category.id}"/>
                                        <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.delete_activity.label"/></button>
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
                        <h5 class="modal-title"><fmt:message key="tables.rows.modal.create_new_category.label"/> </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <label><fmt:message key="entities.fields.name"/>:</label>
                        <input type="text" name="name" value="${sessionScope.invalidAddCategory.name}" required/>
                    </div>
                    <c:if test="${not empty sessionScope.invalidInputError}">
                        <fmt:message key="error_messages.invalid_input_fields"/>: ${sessionScope.invalidInputError}
                        <c:remove var="invalidInputError" scope="session"/>
                    </c:if>
                    <c:if test="${not empty sessionScope.addCategoryErrMsg}">
                        <fmt:message key="error_messages.name_is_taken"/>
                    </c:if>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="tables.rows.modal.cancel"/> </button>
                        <button type="submit" class="btn btn-black"><fmt:message key="tables.rows.modal.add_activity.title"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</my:htmlCarcass>
<c:remove var="err_msg" scope="session"/>
<c:remove var="addCategoryErrMsg" scope="session"/>
<c:remove var="editCategoryErrMsg" scope="session"/>
<c:remove var="invalidAddCategory" scope="session"/>
<c:remove var="invalidEditCategory" scope="session"/>