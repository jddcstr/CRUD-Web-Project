package myservlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import myexceptions.NullValueException;
import utils.EncryptionUtil;

/**
 *
 * @author dwayne
 */
public class LoginServlet extends HttpServlet {
    
    private String URL;
    private String dbUser;
    private String dbPass;
    private String dbDriver;
    private String cipherAlgorithm;
    private String secretKey;
    private String recaptchaSecretKey;
    private static Logger logger;
    
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        
        URL = config.getInitParameter("URL");
        dbUser = config.getInitParameter("dbUser");
        dbPass = config.getInitParameter("dbPass");
        dbDriver = config.getInitParameter("dbDriver");
        cipherAlgorithm = config.getInitParameter("cipherAlgorithm");
        secretKey = config.getInitParameter("secretKey");
        recaptchaSecretKey = config.getInitParameter("recaptchaSecretKey");
        String appRootPath = config.getServletContext().getRealPath("/");
        System.out.println("========== LOG FOLDER IS LOCATED AT: " + appRootPath + " ==========");
        logger = utils.SystemLogger.setupLogger(LoginServlet.class.getName(), appRootPath);
        
        try{
            Class.forName(dbDriver);
        }catch (ClassNotFoundException e) {
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
        
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        
        email = (email != null) ? email.trim() : "";
        password = (password != null) ? password.trim() : "";
        
        logger.info("Event: Start of login ——— Description: User ' " + email + "' initialized login.");
        
        try {
            if(email.isEmpty() && password.isEmpty()) {
                throw new NullValueException("Both username and password are blank.");
            }
            // CAPTCHA CHECK
                HttpSession session = request.getSession(true);
                Integer captchaAttempts = (Integer) session.getAttribute("captchaAttempts");
                if(captchaAttempts == null) {
                    captchaAttempts = 0;
                }
                
                if(captchaAttempts >= 3) {
                    session.setAttribute("flashMessage", "Error: Maximum CAPTCHA attempts reached. Access denied.");
                    response.sendRedirect("maxAttempt.jsp");
                    return;
                }
                
                String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                
                boolean isCaptchaValid = utils.CaptchaUtil.verify(gRecaptchaResponse, recaptchaSecretKey);
                
                if(!isCaptchaValid) {
                    logger.warning("Event: CAPTCHA validation result ——— Description: Failed for user '" + email + "'.");
                    captchaAttempts++;
                    session.setAttribute("captchaAttempts", captchaAttempts);
                    
                    if(captchaAttempts >= 3) {
                        session.setAttribute("flashMessage", "Error: Maximum CAPTCHA attempts reached. Access denied.");
                        response.sendRedirect("index.jsp");
                        return;
                    }
                    
                    int attemptsLeft = 3 - captchaAttempts;
                    session.setAttribute("flashMessage", "Error: CAPTCHA validation failed. " + attemptsLeft + " attempt(s) remaining.");
                    response.sendRedirect("index.jsp");
                    return;
                }

                logger.info("Event: CAPTCHA validation result ——— Description: Passed for user '" + email + "'.");
                session.removeAttribute("captchaAttempts");
                
            // break line for separating different checking method ————————————————————————    
            
            // DATABASE CHECK
            try (Connection conn = DriverManager.getConnection(URL, dbUser, dbPass)) {
                String sql = "SELECT PASSWORD, USERROLE FROM USERS WHERE EMAIL = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                
                ResultSet rs = ps.executeQuery();
                boolean usernameExists = rs.next();
                
                if(!usernameExists) {
                    logger.warning("Event: Username lookup ——— Description: Email '" + email + "' not found.");
                    logger.warning("Event: Failed login ——— Description: Invalid username for '" + email + "'.");
                    
                    if(password.isEmpty()) {
                        request.getRequestDispatcher("error_1.jsp")
                                .forward(request,response);
                    }else {
                        request.getRequestDispatcher("error_3.jsp")
                                .forward(request,response);
                    }
                    return;
                }
                
                logger.info("Event: Username lookup ——— Description: Email '" + email + "' found.");
                        
                String dbPassword = rs.getString("PASSWORD");
                String decryptedPassword = null;
                try {
                    decryptedPassword = EncryptionUtil.decrypt(dbPassword, secretKey, cipherAlgorithm);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "Event: Exception or system error ——— Description: Password decryption failed for user '" + email + "'.", e);
                    
                    session.setAttribute("flashMessage", "System Error: Decryption failed. Check logs.");
                    response.sendRedirect("index.jsp");
                    return;
                }
                String role = rs.getString("USERROLE");
                
                if(decryptedPassword == null || !decryptedPassword.equals(password)) {
                    logger.warning("Event: Password validation result ——— Description: Password mismatch for '" + email + "'.");
                    logger.warning("Event: Failed Login ——— Description: Incorrect password entered for '" + email + "'.");
                    request.getRequestDispatcher("error_2.jsp")
                            .forward(request,response);
                    return;
                }
                
                logger.info("Event: Password validation result ——— Description: Password verified successfully for '" + email + "'.");
                logger.info("Event: Successful login ——— Description: Session created and authenticated for '" + email + "'.");
                
                session.setAttribute("username", email);
                session.setAttribute("role", role);
                
                response.sendRedirect("success.jsp");
            }
            
        } catch (NullValueException e) {
            logger.warning("Event: Failed login ——— Description: Blank credentials provided.");
            request.getRequestDispatcher("noLoginCredentials.jsp")
                    .forward(request,response);
        } catch (IOException | SQLException | ServletException e){
            logger.log(Level.SEVERE, "Event: Exception or system error ——— Description: System crashed during login for '" + email + "'.", e);
            throw new ServletException(e);
        } finally {
            logger.info("Event: End of login process ——— Description: Login process terminated for '" + email + "'.\n");
        }
    }
}
