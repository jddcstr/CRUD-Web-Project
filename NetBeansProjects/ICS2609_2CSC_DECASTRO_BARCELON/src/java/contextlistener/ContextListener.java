package contextlistener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String header = "<header><h1>ICS 2609 / 2CSC / DE CASTRO, Jonathan — BARCELON, D'artagnan</h1></header>";
        String footer = "<footer><p>Current Date and Time - " + new java.util.Date() + " </p> <p>MP2</p></footer>";
        
        context.setAttribute("header", header);
        context.setAttribute("footer", footer);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
}
