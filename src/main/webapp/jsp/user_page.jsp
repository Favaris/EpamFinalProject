<%--
  Created by IntelliJ IDEA.
  User: Favaris
  Date: 11/12/2021
  Time: 1:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>user page</title>
</head>
<body>
login: ${sessionScope.user.login}
<br/>
pass: ${sessionScope.user.password}
<br/>
name: ${sessionScope.user.name}
<br/>
surname: ${sessionScope.user.surname}
<br/>
role: ${sessionScope.user.role}
<br/>
</body>
</html>
