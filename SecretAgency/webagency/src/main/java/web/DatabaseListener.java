package web;

import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import cz.muni.fi.pv168.secretagency.Main;
import cz.muni.fi.pv168.secretagency.Mission.MissionManagerImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by pistab on 15.4.2017.
 */
@WebListener
public class DatabaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        try {
            DataSource dataSource = Main.createDatabase();
            servletContext.setAttribute("missionManager", new MissionManagerImpl(dataSource));
            //servletContext.setAttribute("agentManager", new AgentManagerImpl(dataSource));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
