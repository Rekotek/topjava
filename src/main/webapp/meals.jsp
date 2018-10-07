<%--
  User: taras
  Date: 2018-10-08
  Time: 00:07
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <title>Список съеденного</title>
</head>
<body>
<h2>Список съеденного</h2>
<form>
    <label for="calories">Граничное значение калорий:</label>
    <input id="calories" name="calories" type="number" value="${caloriesPerDay}" autofocus>
    <button type="submit">Показать</button>
</form>
<table width="60%">
    <col width="30%"/>
    <col width="50%"/>
    <thead>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>
    <c:forEach var="meal" items="${mealList}"  varStatus="loop">

        <tr style="color: ${meal.exceed ? "red" : "black"}"
            class="${loop.index % 2 == 0 ? 'even' : 'odd'}">
            <td>${meal.formattedDateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>

<hr>
Вернуться <a href="index.html">назад.</a>
</body>
</html>
