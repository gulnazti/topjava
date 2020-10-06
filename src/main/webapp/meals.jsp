<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cg" uri="/WEB-INF/tld/functions" %>
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

        .red {
            color: red;
        }

        .green {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="?action=add">Add meal</a>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${meals}">
    <c:set var="color" value="${meal.excess ? 'red' : 'green'}"/>
    <tr class="${color}">
        <td>${cg:formatDate(meal.dateTime)}</td>
        <td class="desc">${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="?action=edit&id=${meal.id}">Edit</a></td>
        <td><a href="?action=delete&id=${meal.id}">Delete</a></td>
    </tr>
    </c:forEach>
</table>
</body>
</html>