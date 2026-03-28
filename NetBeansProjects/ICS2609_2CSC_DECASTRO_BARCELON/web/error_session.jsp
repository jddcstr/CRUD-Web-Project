<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Session Error</title>
        <link rel="stylesheet" href="css/success.css">
    </head>
    <body>
        <header>
            <h1>Machine Problem 2 CRUD System</h1>
        </header>

    <div class="dashboard-container">
        <div class="error-card">

            <h2 class="error-title">Unauthorized Access</h2>

            <p class="error-message">
                Your session has expired or you attempted to access a restricted page.<br>
                Please login to continue.
            </p>

            <a href="index.jsp" class="error-btn">Go to Login</a>

        </div>
    </div>

        <footer>
            <p>© De Castro - Barcelon CRUD System</p>
        </footer>
    </body>
</html>
