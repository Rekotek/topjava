<%@ page import="ru.javawebinar.topjava.util.DateTimeUtil" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
    <title><spring:message code="meal.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<section>
    <h3><a href="${pageContext.request.contextPath}"><spring:message code="app.home"/></a></h3>
    <h2><spring:message code="meal.${meal.id == null ? 'create': 'edit'}"/></h2>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form:form method="post"
               action="${pageContext.request.contextPath}/meals/${meal.id == null ? 'create': 'update'}/"
               modelAttribute="meal">
        <form:input path="id" type="hidden" name="id" value="${meal.id}"/>
        <dl>
            <dt><spring:message code="meal.datetime"/>:</dt>
            <dd><form:input path="dateTime" type="datetime-local" value="${meal.dateTime}" name="dateTime" required="required"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><form:input path="description" type="text" value="${meal.description}" size="40" name="description" required="required"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><form:input path="calories" type="number" value="${meal.calories}" name="calories" required="required"/></dd>
        </dl>
        <button type="submit"><spring:message code="meal.submit"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form:form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
