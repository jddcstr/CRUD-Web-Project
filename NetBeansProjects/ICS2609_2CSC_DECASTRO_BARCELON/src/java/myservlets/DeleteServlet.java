package myservlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class DeleteServlet extends HttpServlet {

    private String URL;
    private String dbUser;
    private String dbPass;
    private String dbDriver;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        URL = config.getInitParameter("URL");
        dbUser = config.getInitParameter("dbUser");
        dbPass = config.getInitParameter("dbPass");
        dbDriver = config.getInitParameter("dbDriver");

        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new ServletException("DB Driver not found.");
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("role") == null ||
           !session.getAttribute("role").equals("admin")) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        String targetEmail = request.getParameter("email");
        String loggedInAdmin = (String) session.getAttribute("username");
        
        if(targetEmail != null && targetEmail.equalsIgnoreCase(loggedInAdmin)) {
            session.setAttribute("flashMessage", "Error: You cannot delete your own admin account." );
            response.sendRedirect("success.jsp");
            return;
        }

//        String email = request.getParameter("email");

        try (Connection conn = DriverManager.getConnection(URL, dbUser, dbPass)) {

            String sql = "DELETE FROM USERS WHERE EMAIL = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, targetEmail);

            int rows = ps.executeUpdate();

            if(rows == 0) {
                response.getWriter().println("User not found.");
                return;
            }

            session.setAttribute("flashMessage", "Account successfully deleted.");
            response.sendRedirect("success.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
