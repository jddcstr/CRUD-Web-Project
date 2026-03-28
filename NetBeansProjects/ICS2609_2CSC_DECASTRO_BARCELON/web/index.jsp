<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login Page</title>
        <link rel="stylesheet" href="css/index.css">
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    </head>
    <body>
        <%= application.getAttribute("header") %>
        <div class="login-container">
            
            <h1>Sign in to your<br>CRUD Website Account</h1>
            <%
                String flashMessage = (String) session.getAttribute("flashMessage");
                if(flashMessage != null) {
                    String alertClass = "success-message"; 
                    if(flashMessage.toLowerCase().contains("error") || flashMessage.toLowerCase().contains("failed")) {
                        alertClass = "fail-message"; 
                    }
            %>
                <div class="<%= alertClass %>">
                    <%= flashMessage %>
                </div>
            <%
                    session.removeAttribute("flashMessage");
                }
            %>
            <form action="LoginServlet" method="POST">
                <input type="text" placeholder="Username" name="username">
                <input type="password" placeholder="Password" name="password">
                <div class="g-recaptcha" data-sitekey="6LdmmZgsAAAAAHrOdR7KOxtP7aiAsZ7GCKSY8shJ" style="margin-bottom: 15px;"></div>
                <input type="submit" value="Sign In">
            </form>
            
        </div>
        <%= application.getAttribute("footer") %>
    </body>
</html>
