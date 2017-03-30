package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import cz.muni.fi.pv168.secretagency.Assignment.Assignment;
import cz.muni.fi.pv168.secretagency.Assignment.AssignmentManagerImpl;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static java.time.Month.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AssignmentManagerImpl} class.
 *
 * Created by pistab on 10.3.2017.
 */
public class AssignmentManagerImplTest {

    private AssignmentManagerImpl assignmentManager;

    private DataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
       /* try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE NOT_CREATED_TEST_WILL_NOT_WORK(").executeUpdate();
        }*/
        assignmentManager = new AssignmentManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE ASSIGNMENT").executeUpdate();
        }
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:secretAgencyAssignment-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();



    //--------------------------------------------------------------------------
    // Preparing sample test data
    //--------------------------------------------------------------------------


    private AgentBuilder sampleJamesBondBuilder() {
        return new AgentBuilder()
                .name("James Bond")
                .birthDate(1983,OCTOBER,11)
                .securityLevel(2);
    }

    private MissionBuilder killTheTerroristMissionBuilder() {
        return new MissionBuilder().id(null)
                .description("Very difficult mission, be careful")
                .goal("Find and kill the terrorist")
                .name("Terrorist mission")
                .location("Khazad dum");
    }

    //TODO
    private AssignmentBuilder sampleFindAndKillAssigment(){
        return new AssignmentBuilder()
                .agent(sampleJamesBondBuilder().build())
                .mission(killTheTerroristMissionBuilder().build());
    }

    @Test
    public void createAssignment(){
        Assignment assignment = sampleFindAndKillAssigment().build();
        assignmentManager.createAssignment(assignment);
        Long assignmentId = assignment.getId();
        assertThat(assignmentId).isNotNull();
        assertThat(assignmentManager.findAssignmentById(assignment.getId()))
                .isNotSameAs(assignment)
                .isEqualToComparingFieldByField(assignment);
    }

}
