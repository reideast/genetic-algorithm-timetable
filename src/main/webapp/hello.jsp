<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page import="net.andreweast.bean.Hello" %>--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hello Gradle Web App</title>
</head>
<body>

<jsp:useBean id="greeting" class="net.andreweast.bean.Hello"/>
<h3>${greeting.hello}</h3>

<p>Modified, and deployed over <pre>eb cli</pre> newer version!</p>

</body>
</html>
