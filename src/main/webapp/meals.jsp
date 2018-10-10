<%--
  User: taras
  Date: 2018-10-08
  Time: 00:07
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <title>Список съеденного</title>
</head>
<body>
<c:if test="${error != null}">
    <p class="error">${error}</p>
</c:if>
<h2>Список съеденного</h2>
<p><em>Всего записей: ${countMeals}</em></p>
<%--<form>--%>
    <%--<label for="calories">Граничное значение калорий:</label>--%>
    <%--<input id="calories" class="calories" name="calories" type="number" value="${caloriesPerDay}" autofocus>--%>
    <%--<button type="submit">Показать</button>--%>
<%--</form>--%>
<table width="60%">
    <col width="20%"/>
    <col width="40%"/>
    <col width="13%"/>
    <thead>
    <tr>
        <th height="50px">Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan="2"></th>
    </tr>
    </thead>
    <c:forEach var="meal" items="${mealList}" varStatus="loop">

        <tr style="color: ${meal.exceed ? "red" : "black"}"
            class="${loop.index % 2 == 0 ? 'even' : 'odd'}"
            valign="middle">
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm" />
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <form method="get">
                    <input type="hidden" name="id" value="${meal.id}">
                    <input type="hidden" name="action" value="edit">
                    <input type="submit" value="Редактировать"/>
                </form>
            </td>
            <td>
                <form method="post">
                    <input type="hidden" name="id" value="${meal.id}">
                    <input type="hidden" name="action" value="delete">
                    <input type="submit" name="delete" value="Удалить"/>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<hr>
Вернуться <a href="index.html">назад.</a>
</body>
</html>
