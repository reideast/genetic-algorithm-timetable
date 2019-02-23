<%--
  Created by IntelliJ IDEA.
  User: deskase
  Date: 23/02/2019
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%
    String foo = System.getProperty("FOO");
    String baz = System.getProperty("BAZ");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>connect to AWS db</title>
</head>
<body>
<h2>Reading ENV</h2>
<p>Set in web console: FOO should be "BAR": "<%= foo %>"</p>
<p>From .ebextensions/options.config: BAZ should be "qux": "<%= baz %>"</p>
</body>
</html>
