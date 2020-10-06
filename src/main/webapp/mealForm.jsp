<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cg" uri="/WEB-INF/tld/functions" %>
<html lang="ru">
<head>
    <title>Java Enterprise (Topjava) | Meal Form</title>
    <style type="text/css">
        p {
            background: #f5f5f5;
            margin: 10px 0;
        }

        label {
            float: left;
            width: 150px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<c:set var="title" value="${meal.id == 0 ? 'Add meal' : 'Edit meal'}" />
<h2>${title}</h2>
<form action="?action=save" method="post">
    <input type="text" name="id" value="${meal.id}" hidden />
    <p>
        <label for="datetime">DateTime:</label>
        <input type="datetime-local" name="datetime" id="datetime" value="${meal.dateTime}" required="required" />
    </p>
    <p>
        <label for="description">Description:</label>
        <input type="text" name="description" id="description" value="${meal.description}" size="30" required="required"
               pattern="\S+.*" />
    </p>
    <p>
        <label for="calories">Calories:</label>
        <input type="number" name="calories" id="calories"  value="${meal.calories}" required="required"
               min="0" />
    </p>
    <input type="submit" value="Save" />
    <input type="reset" value="Cancel" onclick="window.history.back()" />
</form>
</body>
</html>