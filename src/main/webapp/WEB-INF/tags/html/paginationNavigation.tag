<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="command" required="true" type="java.lang.String"%>

<%@include file="/WEB-INF/jspf/taglibs.jspf" %>

<c:set var="hasOrderBy" value="${not empty requestScope.orderBy}"/>
<c:set var="hasFilterBy" value="${not empty requestScope.filterBy}"/>
<c:set var="hascountLessThan" value="${not empty requestScope.countLessThan}"/>
<c:set var="hascountBiggerThan" value="${not empty requestScope.countBiggerThan}"/>
<c:set var="hasSearchBy" value="${not empty requestScope.searchBy}"/>
<c:set var="hasUId" value="${not empty requestScope.uId}"/>

<div class="row">
    <div class="col-4"></div>
<div class="btn-group mx-5 col-4" role="group" >
    <form action="${root}/controller">
        <input type="hidden" name="command" value="${command}">

        <input type="hidden" name="page" value="${requestScope.page - 1}">
        <input type="hidden" name="pageSize" value="${requestScope.pageSize}">

        <%@ include file="/WEB-INF/jspf/paginationInputTags.jspf"%>

        <button type="submit" class="btn btn-dark" ${requestScope.page - 1 > 0 ? '' : 'disabled'}>Prev</button>
    </form>
    <c:forEach var="pageNum" begin="1" end="${requestScope.pageCount}" step="1">
        <form action="${root}/controller">
            <input type="hidden" name="command" value="${command}">

            <input type="hidden" name="page" value="${pageNum}">
            <input type="hidden" name="pageSize" value="${requestScope.pageSize}">

            <%@ include file="/WEB-INF/jspf/paginationInputTags.jspf"%>

            <button type="submit" class="btn btn-dark ${requestScope.page == pageNum ? 'active' : ''}">${pageNum}</button>
        </form>
    </c:forEach>
    <form action="${root}/controller">
        <input type="hidden" name="command" value="${command}">

        <input type="hidden" name="page" value="${requestScope.page + 1}">
        <input type="hidden" name="pageSize" value="${requestScope.pageSize}">

        <%@ include file="/WEB-INF/jspf/paginationInputTags.jspf"%>

        <button type="submit" class="btn btn-dark" ${requestScope.page < requestScope.pageCount ? '' : 'disabled'}>Next</button>
    </form>
</div>
    <div class="col-4"></div>
</div>