package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import org.junit.Before;
import org.junit.Test;

import static java.time.Month.*;
import static org.assertj.core.api.Assertions.*;
/**
 * Tests for {@link AgentManagerImpl} class.
 *
 * Created by pistab on 10.3.2017.
 */
public class AgentManagerImplTest {

    private AgentManagerImpl agentManager;

    @Before
    public void setUp() {
        agentManager = new AgentManagerImpl();
    }

    //--------------------------------------------------------------------------
    // Preparing sample test data
    //--------------------------------------------------------------------------

    private AgentBuilder sampleJamesBondBuilder() {
        return new AgentBuilder()
                .id(null)
                .name("James Bond")
                .birthDate(1983,OCTOBER,11)
                .securityDegree(2);
    }

    private AgentBuilder sampleMagnusGiriBuilder() {
        return new AgentBuilder()
                .id(null)
                .name("Magnum")
                .birthDate(1999,SEPTEMBER,9);
    }


    //--------------------------------------------------------------------------
    // Actual tests
    //--------------------------------------------------------------------------


    @Test
    public void createAgent() {
        Agent agent = sampleJamesBondBuilder().build();
        agentManager.createAgent(agent);
        Long agentId = agent.getId();
        assertThat(agentId).isNotNull();

        assertThat(agentManager.findAgentById(agentId))
                .isNotSameAs(agent)
                .isEqualToComparingFieldByField(agent);
    }

}
