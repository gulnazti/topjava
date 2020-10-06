<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/functions" prefix="cg" %>
<html lang="ru">
<head>
    <title>Java Enterprise (Topjava) | Meals</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 2px solid black;
        }

        th, td {
            padding: 7px 10px;
        }

        .desc {
            width: 200px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="add">Add meal</a>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${meals}">
    <c:set var="color" value="color: ${meal.excess ? 'red' : 'green'}"/>
    <tr style="${color}">
        <td>${cg:formatDate(meal.dateTime)}</td>
        <td class="desc">${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="edit?id=${meal.id}">Edit</a></td>
        <td><a href="delete?id=${meal.id}">Delete</a></td>
    </tr>
    </c:forEach>
</table>
</body>
</html>