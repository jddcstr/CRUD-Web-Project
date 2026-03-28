<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    
    if(session.getAttribute("username") == null){
        response.sendRedirect("error_session.jsp");
        return;
    }
    
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>System Dashboard</title>
        <link rel="stylesheet" href="css/success.css">
    </head>
    <body>
        <header>
            <h1>Machine Problem 2 CRUD System</h1>

            <div class="header-right">
                <span>Welcome, <strong><%=username %></strong> (<%= role %>)</span>
                <a href="<%=request.getContextPath()%>/LogoutServlet" class="logout-btn">Logout</a>
            </div>
        </header>
                
        <div class="dashboard-container">
            <div class="dashboard-card">
                <%
                String flashMessage = (String) session.getAttribute("flashMessage");
                if(flashMessage != null){
                    String alertClass = "success-message";
                    if(flashMessage.toLowerCase().contains("error") || flashMessage.toLowerCase().contains("failed")){
                        alertClass = "fail-message";
                    }
                %>
                <div class="<%= alertClass %>">
                    <%= flashMessage %>
                </div>
                <%
                session.removeAttribute("flashMessage");}
                %>
                <h2>Dashboard</h2>
                
                <div class="button-group">
                    
                    <% if(role.equalsIgnoreCase("Admin")) { %>
                    
                    <a href="create.jsp" class="dashboard-btn">Create Account</a>
                    <a href="update.jsp" class="dashboard-btn">Update Account</a>
                    <a href="delete.jsp" class="dashboard-btn">Delete Account</a>
                    <a href="view.jsp" class="dashboard-btn">View Accounts</a>
                    
                    <% } else if(role.equalsIgnoreCase("Guest")) { %>
                    
                    <a href="view.jsp" class="dashboard-btn">View Accounts</a>
                    
                    <% } %>
                </div>
            </div>
        </div>
                
        <footer>
            <p> © De Castro - Barcelon CRUD System
        </footer>
    </body>
</html>
