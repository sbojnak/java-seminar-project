package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Mission.MissionManager;
import cz.muni.fi.pv168.secretagency.Mission.MissionManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        try(Connection con = ds.getConnection()) {
            for (String line : Files.readAllLines(Paths.get("src", "main", "resources", "data.sql"))) {
                if(line.trim().isEmpty()) continue;
                if(line.endsWith(";")) line=line.substring(0,line.length()-1);
                try (PreparedStatement st1 = con.prepareStatement(line)) {
                    st1.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
