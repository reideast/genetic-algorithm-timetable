<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.sql.*" %>

<%
    StringBuilder results = new StringBuilder();

    // Read RDS connection information from the environment
    String dbProtocol = "postgresql";
    String dbName = System.getProperty("RDS_DB_NAME");
    String dbUserName = System.getProperty("RDS_USERNAME");
    String dbPassword = System.getProperty("RDS_PASSWORD");
    String dbHostname = System.getProperty("RDS_HOSTNAME");
    String dbPort = System.getProperty("RDS_PORT");
    String jdbcUrl = "jdbc:" + dbProtocol + "://" + dbHostname + ":" +
            dbPort + "/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword;
    System.out.println("JDBC URL='" + jdbcUrl + "'");

    // Load the JDBC driver
    try {
        System.out.println("Loading driver...");
        Class.forName("org.postgresql.Driver");
        System.out.println("Driver loaded!");
    } catch (ClassNotFoundException e) {
        throw new RuntimeException("Cannot find the driver in the classpath!", e);
    }

    // Get info from tables
    try (Connection conn = DriverManager.getConnection(jdbcUrl)) {
        try (Statement readStatement = conn.createStatement()) {
            try (ResultSet resultSet = readStatement.executeQuery("SELECT name, number FROM testtable;")) {
                while (resultSet.next()) {
                    results.append(resultSet.getString("name"));
                    results.append(": ").append(resultSet.getString("number")).append("<br>");
                }
            }
        }
    } catch (SQLException ex) {
        // Handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        ex.printStackTrace();
    }
%>



<!DOCTYPE html>
<html>
<head>
    <title>connect to AWS db</title>
</head>
<body>
<h2>Established connection to RDS.</h2>
<p>Read first two rows: <%= results.toString() %></p>
</body>
</html>
