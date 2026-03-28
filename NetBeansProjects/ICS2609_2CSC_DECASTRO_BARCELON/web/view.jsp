<%@page import="java.sql.*"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    
    if(session.getAttribute("username") == null){
        response.sendRedirect("error_session.jsp");
        return;
    }

    String URL = "jdbc:derby://localhost:1527/LoginDB";
    String dbUser = "app";
    String dbPass = "app";
    String dbDriver = "org.apache.derby.jdbc.ClientDriver";

    Class.forName(dbDriver);
    Connection conn = DriverManager.getConnection(URL, dbUser, dbPass);

    String currentUser = (String) session.getAttribute("username");
    String currentRole = (String) session.getAttribute("role");
    
    PreparedStatement ps;
    // If Guest, filter the SQL query. If Admin, show all.
    if (currentRole.equalsIgnoreCase("guest")) {
        String sql = "SELECT EMAIL, PASSWORD, USERROLE FROM USERS WHERE EMAIL = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, currentUser);
    } else {
        String sql = "SELECT EMAIL, PASSWORD, USERROLE FROM USERS ORDER BY EMAIL ASC";
        ps = conn.prepareStatement(sql);
    }

    ResultSet rs = ps.executeQuery();
//    String sql = "SELECT EMAIL, PASSWORD, USERROLE FROM USERS ORDER BY EMAIL ASC";
//    PreparedStatement ps = conn.prepareStatement(sql);
//    ResultSet rs = ps.executeQuery();
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>View Users</title>
        <link rel="stylesheet" href="css/success.css">
    </head>
    <body>
    <header>
        <h1>View Accounts</h1>
        <div class="header-right">
            <a href="success.jsp" class="logout-btn">Back</a>
            <a href="<%=request.getContextPath()%>/LogoutServlet" class="logout-btn">Logout</a>
        </div>
    </header>

    <div class="dashboard-container">
        <div class="dashboard-card">
            <h2>Registered Users</h2>

            <table class="styled-table">
                <tr>
                    <th>Email</th>
                    <th>Password</th>
                    <th>Role</th>
                </tr>

                <%
                    while(rs.next()){
                %>
                    <tr>
                        <td><%= rs.getString("EMAIL") %></td>
                        <td><%= rs.getString("PASSWORD") %></td>
                        <td><%= rs.getString("USERROLE") %></td>
                    </tr>
                <%
                    }
                    conn.close();
                %>
            </table>

        </div>
    </div>

    </body>
</html>
