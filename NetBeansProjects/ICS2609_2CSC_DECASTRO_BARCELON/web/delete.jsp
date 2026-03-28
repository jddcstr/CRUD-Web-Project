<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    
    if(session.getAttribute("username") == null){
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
        <title>Delete Page</title>
        <link rel="stylesheet" href="css/success.css">
    </head>
    <body>
        <header>
            <h1>Delete Account</h1>
            <div class="header-right">
                <a href="success.jsp" class="logout-btn">Back</a>
                <a href="<%=request.getContextPath()%>/LogoutServlet" class="logout-btn">Logout</a>
            </div>
        </header>
        <div class="dashboard-container">
            <div class="dashboard-card">
                <form action="<%=request.getContextPath()%>/DeleteServlet" method="post" class="form-group">
                    <input type="text" name="email" placeholder="Email to Delete" class="form-input" required>
                    <button type="submit" class="form-btn">Delete</button>
                </form>
            </div>
        </div>
    </body>
</html>
