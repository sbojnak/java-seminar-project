package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import cz.muni.fi.pv168.secretagency.Assignment.AssignmentManagerImpl;
import cz.muni.fi.pv168.secretagency.Mission.Mission;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

    //TODO
    private AssignmentBuilder sampleFindAndKillAssigment(){
        return null;
    }
}
