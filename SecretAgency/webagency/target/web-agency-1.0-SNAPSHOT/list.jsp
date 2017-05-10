<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>name</th>
        <th>goal</th>
        <th>location</th>
        <th>description</th>
    </tr>
    </thead>
    <c:forEach items="${missions}" var="mission">
        <tr>
            <td><c:out value="${mission.id}"/></td>
            <td><c:out value="${mission.name}"/></td>
            <td><c:out value="${mission.goal}"/></td>
            <td><c:out value="${mission.location}"/></td>
            <td><c:out value="${mission.description}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/missions/delete?id=${mission.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Smazat"></form></td>
        </tr>
    </c:forEach>
</table>
<h2>Create mission</h2>
<form action="${pageContext.request.contextPath}/missions/add" method="post">
    <table>
        <tr>
            <th>Mission name:</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"/></td>
        </tr>
        <tr>
            <th>Mission goal:</th>
            <td><input type="text" name="goal" value="<c:out value='${param.goal}'/>"/></td>
        </tr>
        <tr>
            <th>Mission location:</th>
            <td><input type="text" name="location" value="<c:out value='${param.location}'/>"/></td>
        </tr>
        <tr>
            <th>Mission description:</th>
            <td><input type="text" name="description" value="<c:out value='${param.description}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Create" />
</form>
</body>
</html>
