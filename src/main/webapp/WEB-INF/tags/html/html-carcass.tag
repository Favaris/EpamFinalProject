<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="title" required="true" type="java.lang.String"%>

<%@include file="/WEB-INF/jspf/taglibs.jspf" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/main.css"/>?id=<%out.print(new java.util.Random().nextInt(9999));%>" rel="stylesheet">
</head>
<body>
    <c:if test="${not empty sessionScope.user}">
        <%@include file="/WEB-INF/jspf/navbar.jspf"%>
    </c:if>
    <jsp:doBody/>
</body>
</html>