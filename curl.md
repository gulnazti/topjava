## CURL for users

#### getAll
<pre>curl http://localhost:8080/topjava/rest/admin/users</pre>

#### get
<pre>curl http://localhost:8080/topjava/rest/admin/users/100001</pre>

#### create
<pre>curl -X POST -d '{"name":"New","email":"new@gmail.com","password":"newPass","roles":["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava/rest/admin/users</pre>

#### update
<pre>curl -X PUT -d '{"name":"UpdatedName","email":"update@gmail.com","password":"newPass","roles":["ADMIN"],"caloriesPerDay":330}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/admin/users/100001</pre>

## CURL for meals

#### getAll
<pre>curl http://localhost:8080/topjava/rest/profile/meals</pre>

#### getFiltered
<pre>curl http://localhost:8080/topjava/rest/profile/meals/filter?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-30&endTime=21:00:00</pre>

#### get
<pre>curl http://localhost:8080/topjava/rest/profile/meals/100002</pre>

#### create
<pre>curl -X POST -d '{"dateTime":"2020-02-01T18:00","description":"Созданный ужин","calories":300}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava/rest/profile/meals</pre>

#### update
<pre>curl -X PUT -d '{"dateTime":"2020-01-30T10:02","description":"Обновленный завтрак","calories":200}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/profile/meals/100002</pre>

#### delete
<pre>curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/100008</pre>
