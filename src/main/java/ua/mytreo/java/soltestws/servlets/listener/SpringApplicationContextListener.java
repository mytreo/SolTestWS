package ua.mytreo.java.soltestws.servlets.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Слушатель, передающий сервдету ApplicationContext Spring'a
 *
 * @author mytreo  03.02.2016.
 * @version 1.0
 */
@WebListener
public class SpringApplicationContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"appConfig.xml"});
       sce.getServletContext().setAttribute("appContext",ctx);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
