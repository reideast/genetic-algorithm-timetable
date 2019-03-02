<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<jsp:useBean id="scheduleBean" class="net.andreweast.bean.ScheduleBean"/>
<p>${scheduleBean.schedule}</p>

</body>
</html>
