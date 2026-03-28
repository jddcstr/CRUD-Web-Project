<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if(session == null || session.getAttribute("role") == null){
        response.sendRedirect("error_session.jsp");
        return;
    }
    
    String role = (String) session.getAttribute("role");
    if(!role.equalsIgnoreCase("admin")){
        response.sendRedirect("success.jsp");
        return;
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Page</title>
        <link rel="stylesheet" href="css/success.css">
    </head>
    <body>
        <header>
            <h1>Create Account</h1>
            <div class="header-right">
                <a href="success.jsp" class="logout-btn">Back</a>
                <a href="LogoutServlet" class="logout-btn">Logout</a>
            </div>
        </header>
        <div class="dashboard-container">
            <div class="dashboard-card">
                <h2>Create New Account</h2>
                <form action="<%=request.getContextPath()%>/CreateServlet" method="POST" class="form-group">
                    <input type="text" name="email" placeholder="Email" class="form-input" required>
                    <input type="password" name="password" placeholder="Password" class="form-input" required>
                    <select name="role" class="form-select">
                        <option value="guest">guest</option>
                        <option value="admin">admin</option>
                    </select>
                    <button type="submit" class="form-btn">Create</button>
                </form>
            </div>
        </div>
    </body>
</html>
