<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/topjava.common.js" defer></script>
<script type="text/javascript" src="resources/js/topjava.meals.js" defer></script>
<script type="text/javascript" src="resources/js/topjava.isodate.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3><spring:message code="meal.title"/></h3>

        <div class="row col-md-6">
            <form id="filterForm"
                  class="border border-secondary p-md-2 mb-md-2"
                  <%--method="post"--%>
                  <%--action="meals/filter"--%>
            >
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="startDate"><spring:message code="meal.startDate"/>:</label>
                        <input type="date"
                               class="form-control"
                               id="startDate"
                               name="startDate"
                               value="${param.startDate}"/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="endDate"><spring:message code="meal.endDate"/>:</label>
                        <input type="date"
                               class="form-control"
                               id="endDate"
                               name="endDate"
                               value="${param.endDate}"/>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="startTime"><spring:message code="meal.startTime"/>:</label>
                        <input type="time"
                               class="form-control"
                               id="startTime"
                               name="startTime"
                               value="${param.startTime}">
                    </div>
                    <div class="form-group col-md-6">
                        <label for="endTime"><spring:message code="meal.endTime"/>:</label>
                        <input type="time"
                               class="form-control"
                               id="endTime"
                               name="endTime"
                               value="${param.endTime}">
                    </div>
                </div>
                <button type="button"
                        class="btn btn-secondary"
                        onclick="updateTable()">
                    <spring:message code="meal.filter"/>
                </button>
                <button type="button"
                        id="resetBtn"
                        class="btn btn-outline-secondary float-right"
                        onclick="resetFilter()">
                    <spring:message code="meal.filter-reset"/>
                </button>
            </form>
        </div>

        <div class="row col">
            <button class="btn-primary btn-xs" onclick="addMeal()">
                <span class="fa fa-plus"></span>
                <spring:message code="meal.add"/>
            </button>
        </div>

        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>
                <tr data-mealExcess="${meal.excess}" data-id="${meal.id}">
                    <td>
                            <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                            <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                            <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                            ${fn:formatDateTime(meal.dateTime)}
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals/update?id=${meal.id}"><span class="fa fa-pencil"></span></a></td>
                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<!--- ADD NEW MEAL MODAL DIALOGUE -->
<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3><spring:message code="meal.add"/></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">
                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="datetime-local"
                               class="form-control"
                               id="dateTime" name="dateTime"
                               placeholder="<spring:message code="meal.dateTime"/>"
                               required />
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message code="meal.description"/></label>
                        <input type="text"
                               size=40
                               class="form-control"
                               id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>"
                               required />
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number"
                               <%--value="${meal.calories}"--%>
                               class="form-control"
                               id="calories" name="calories"
                               required />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>

<!-- END MODAL -->

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>