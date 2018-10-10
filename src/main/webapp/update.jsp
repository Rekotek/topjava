<%--
  User: taras
  Date: 2018-10-10
  Time: 01:30
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/update.css" />" rel="stylesheet">
    <title>Обновить запись</title>
</head>
<body>
    <form method="post">
        <fieldset>
            <legend>Обновление записи №${meal.id}</legend>
            <input type="hidden" name="id" value="${meal.id}" >
            <input type="hidden" name="action" value="update" >
            <p><label class="field" for="date_time">Дата и время:</label>
               <input type="datetime-local" name="date_time" value="${meal.dateTime}">
            </p>
            <p><label class="field" for="desc">Описание:</label> <input class="textbox-200" type="text" name="desc" value="${meal.description}" ></p>
            <p><label class="field" for="calories">Калории:</label> <input class="textbox-200" type="number" name="calories" value="${meal.calories}" ></p>
            <p class="btn-group">
                <input type="submit" class="btn" value="Обновить"/>
                <input type="reset" class="btn" value="Сброс"/>
                <input type="button" class="btn" value="Назад" onclick="history.back()"/>
            </p>
        </fieldset>
    </form>
</body>
</html>
