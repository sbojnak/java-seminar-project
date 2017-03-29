package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Mission.MissionManager;
import cz.muni.fi.pv168.secretagency.Mission.MissionManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Jakub Bartolomej Kosuth on 29.3.2017.
 * Main class of project
 */
public class Main {

    public static void main(String[] args) throws IOException{
        Properties myconf = new Properties();
        myconf.load(Main.class.getResourceAsStream("/myconf.properties"));

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(myconf.getProperty("jdbc.url"));
        ds.setUsername(myconf.getProperty("jdbc.user"));
        ds.setPassword(myconf.getProperty("jdbc.password"));

        MissionManager mission = new MissionManagerImpl(ds);
    }
}
