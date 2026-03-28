Solution Specification
Machine Problem #3 – Secure Web-Based Login System
1. Introduction
This document contains the implementation of our Machine Problem#3. In this project, we added encryption and decryption for security, CAPTCHA to filter out bots, and logging to record activities. The features were added while maintaining the core functionality developed in MP2
2. System Overview
The system is a Java EE web application deployed on a local Glassfish server. It provides a login page where users authenticate using their respective credentials and a captcha verification.

Main components of the system:
• index.jsp – login interface for users
• LoginServlet – processes login authentication
• CaptchaUtil – generates captcha verification
• EncryptionUtil – handles password encryption and decryption
• LoginDB(Database) – stores user credentials
•SystemLogger – records activities(login, error, exception, admin action)
• UpdateServlet– updates user information in the database
• DeleteServlet – deletes user from the database
• CreateServlet – adds new user to the database


3. System Architecture

User
 ↓
index.jsp
 ↓
CaptchaUtil (g-recaptcha)
 ↓
LoginServlet
 ↓
Password Encryption/Decryption
 ↓
Database Verification
 ↓
success.jsp (if authenticated)

For admin actions:

Success.jsp
 ↓
CreateServlet, UpdateServlet, DeleteServlet
 ↓
(Creates user or updates user  or deletes user)

4. Database Design
Table: users
Column
Type
Description
EMAIL
Varchar
User Login Name
PASSWORD
Varchar
Encrypted Password
USERROLE
Varchar
User Role (admin or guest)


5. Password Encryption Implementation
Passwords are encrypted before being stored in the database to improve security. The system retrieves the secret key and cipher algorithm from the web.xml which is used for encryption and decryption of the password.

Example concept used in the implementation:
 Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

The database stores the password encrypted instead of plain text.





6. Captcha Implementation
Captcha verification is used to prevent automated login attempts and brute-force attacks. While the system supports basic implementation, this project utilizes Google reCAPTCHA v2 for enhanced security.
Implementation details:
The system uses reCAPTCHA v2 “I’m not a robot” checkbox on the login page.
Validation is performed by CaptchaUtil.java which sends the user’s response token to the Google API for verification
As per the requirements, users are allowed up to three attempts for CAPTCHA verification. If all attempts are exhausted, the user is redirected to maxAttempt.jsp and access is denied.
Flow:
index.jsp (user checks “I’m not a robot” box)
 ↓
LoginServlet (receives the g-recaptcha-response token)
 ↓
CaptchaUtil (validates the token with Google’s servers)
 ↓
LoginServlet (receives boolean result (if valid, system proceeds to password decryption —  if invalid, the attempt counter is incremented))

7. Session Management
Session management is implemented to ensure secure authentication.

Security measures implemented:
• Session timeout set to 5 minutes
• Session fixation prevention by invalidating old sessions
• Only authenticated users can access success.jsp

Example concept used:
To prevent Session Fixation attacks, LoginServlet clears any existing session data before establishing a new authenticated identity:
HttpSession session = request.getSession(false);
if (session != null) {
	session.invalidate();
}
session = request.getSession(true);
session.setAttribute(“username”, email);




8. Logging Implementation
Login activities are recorded using java.util.logging.Logger and FileHandler.
Log Configuration:
File name: Logs are stored in log/ directory within the application using the format Log_[YYYYMMDD].log
Format: Each entry includes Timestamp, Log Level (INFO, WARNING, SEVERE), Event Checkpoint, and Description
Event Checkpoints:
Start of login attempt
CAPTCHA validation result (Success/fail)
Username lookup result (Found/Not found)
Password validation result (Match/Mismatch)
Successful Login
Failed Login
Exceptions or system errors (e.g., Decryption failure)
End of login process




9. Screenshots 
• Login page
• Captcha generation
• Successful login
• Admin view
• Guest view
• Failed login attempt
• Encrypted password stored in database
• Log file entries


10. Conclusion
The MP3 system successfully integrates different layers of security. By combining AES password encryption, reCAPTCHA bot protection, and comprehensive logging, the application is protected against automated bot attacks and unauthorized credential access while maintaining the core functionality of the original system. These added functionalities ensure that the system meets the security standards of modern web-based authentication.
