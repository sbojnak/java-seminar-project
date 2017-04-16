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
        </tr>
    </c:forEach>
</table>
</body>
</html>
