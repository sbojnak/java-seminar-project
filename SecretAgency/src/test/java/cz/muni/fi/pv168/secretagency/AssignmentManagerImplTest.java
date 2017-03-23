package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import cz.muni.fi.pv168.secretagency.Assignment.Assignment;
import cz.muni.fi.pv168.secretagency.Assignment.AssignmentManagerImpl;
import cz.muni.fi.pv168.secretagency.Mission.Mission;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.time.Month.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Tests for {@link AssignmentManagerImpl} class.
 *
 * Created by pistab on 10.3.2017.
 */
public class AssignmentManagerImplTest {

    private AssignmentManagerImpl assignmentManager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        assignmentManager = new AssignmentManagerImpl();
    }

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
                .mission(killTheTerroristMissionBuilder().build())
                .jobCompleted(false);
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
