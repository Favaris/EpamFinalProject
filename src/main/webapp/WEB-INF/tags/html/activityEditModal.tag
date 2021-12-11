<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="activity" required="true" type="com.prusan.finalproject.db.entity.Activity"%>

<%@include file="/WEB-INF/jspf/taglibs.jspf" %>

<button type="button" class="btn btn-black" data-toggle="modal" data-target="#editActivityModal${activity.id}">
    Edit
</button>
<div class="modal fade" id="editActivityModal${activity.id}" tabindex="-1" role="dialog" aria-labelledby="Confirm addition" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <form action="${root}/controller" method="post">
            <input type="hidden" name="command" value="updateActivity">
            <input type="hidden" name="id" value="${activity.id}">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" class="form-control" required="required" value="${activity.name}"/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <textarea rows = "5" cols = "60" name = "description" required>${activity.description}</textarea>
                    </div>
                    <div class="form-group">
                        <label>Category</label>
                        <select name="cId">
                        <c:forEach var="category" items="${requestScope.categories}">
                            <option value="${category.id}" id="${category.id}" ${activity.category.equals(category) ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                        </select>
                    </div>
                    <c:if test="${not empty sessionScope.invalidFields}">
                        You have invalid fields: ${sessionScope.invalidFields}
                        <c:remove var="invalidFields" scope="session"/>
                    </c:if>
                    ${sessionScope.activityEditErrMsg}
                    <c:remove var="activityEditErrMsg" scope="session"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-black">Save</button>
                </div>
            </div>
        </form>
    </div>
</div>
