package myservlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.EncryptionUtil;

public class UpdateServlet extends HttpServlet {

    private String URL;
    private String dbUser;
    private String dbPass;
    private String dbDriver;
    private String cipherAlgorithm;
    private String secretKey;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        URL = config.getInitParameter("URL");
        dbUser = config.getInitParameter("dbUser");
        dbPass = config.getInitParameter("dbPass");
        dbDriver = config.getInitParameter("dbDriver");
        cipherAlgorithm = config.getInitParameter("cipherAlgorithm");
        secretKey = config.getInitParameter("secretKey");

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

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String encryptedPassword;
        try {
            encryptedPassword = EncryptionUtil.encrypt(password, secretKey, cipherAlgorithm);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flashMessage", "Update failed: Encryption error.");
            response.sendRedirect("update.jsp");
            return;
        }
        String role = request.getParameter("role");

        try (Connection conn = DriverManager.getConnection(URL, dbUser, dbPass)) {

            String sql = "UPDATE USERS SET PASSWORD = ?, USERROLE = ? WHERE EMAIL = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, encryptedPassword);
            ps.setString(2, role);
            ps.setString(3, email);

            int rows = ps.executeUpdate();

            if(rows == 0) {
                response.getWriter().println("User not found.");
                return;
            }
            
            session.setAttribute("flashMessage", "Account successfully updated.");
            response.sendRedirect("success.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
