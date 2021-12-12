<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="activity" required="true" type="com.prusan.finalproject.db.entity.Activity"%>

<%@include file="/WEB-INF/jspf/taglibs.jspf" %>

<button type="button" class="btn btn-black" data-toggle="modal" data-target="#editActivityModal${activity.id}">
    <fmt:message key="tables.rows.modal.edit_activity.label"/>
</button>
<div class="modal fade" id="editActivityModal${activity.id}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <form action="${root}/controller" method="post">
            <input type="hidden" name="command" value="updateActivity">
            <input type="hidden" name="id" value="${activity.id}">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><fmt:message key="tables.rows.modal.edit_activity.label"/></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label><fmt:message key="entities.fields.name"/></label>
                        <input type="text" name="name" class="form-control" required="required" value="${activity.name}"/>
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="entities.fields.description"/></label>
                        <textarea rows = "5" cols = "60" name = "description" required>${activity.description}</textarea>
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="entities.fields.category"/></label>
                        <select name="cId">
                        <c:forEach var="category" items="${requestScope.categories}">
                            <option value="${category.id}" id="${category.id}" ${activity.category.equals(category) ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                        </select>
                    </div>
                    <c:if test="${not empty sessionScope.invalidFields}">
                        <fmt:message key="error_messages.invalid_input_fields"/>: ${sessionScope.invalidFields}
                        <c:remove var="invalidFields" scope="session"/>
                    </c:if>
                    <c:if test="${sessionScope.activityEditErrMsg != null}">
                        <fmt:message key="error_messages.name_is_taken"/>: ${sessionScope.invalidFields}
                        <c:remove var="activityEditErrMsg" scope="session"/>
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
