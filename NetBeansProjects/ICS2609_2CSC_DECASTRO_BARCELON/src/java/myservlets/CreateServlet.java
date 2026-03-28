package myservlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.EncryptionUtil;

public class CreateServlet extends HttpServlet {
    
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
        } catch(ClassNotFoundException e) {
            throw new ServletException("DB Driver not found.");
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) {
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
            session.setAttribute("flashMessage", "Error encrypting password: " + e.getMessage());
            response.sendRedirect("create.jsp");
            return;
        }

        String role = request.getParameter("role");
        
        try (Connection conn = DriverManager.getConnection(URL, dbUser, dbPass)) {

            String checkSql = "SELECT EMAIL FROM USERS WHERE EMAIL = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, email);
            ResultSet rs = checkPs.executeQuery();

            if(rs.next()) {
                response.getWriter().println("Email already exists.");
                return;
            }

            String sql = "INSERT INTO USERS (EMAIL, PASSWORD, USERROLE) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, encryptedPassword);
            ps.setString(3, role);

            ps.executeUpdate();
            
            session.setAttribute("flashMessage", "Account successfully created.");
            response.sendRedirect("success.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
