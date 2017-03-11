package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.bind.ValidationException;

import static java.time.Month.*;
import static org.assertj.core.api.Assertions.*;
/**
 * Tests for {@link AgentManagerImpl} class.
 *
 * Created by pistab on 10.3.2017.
 */
public class AgentManagerImplTest {

    private AgentManagerImpl agentManager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        agentManager = new AgentManagerImpl();
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

    private AgentBuilder sampleMagnusGiriBuilder() {
        return new AgentBuilder()
                .name("Magnum")
                .birthDate(1999,SEPTEMBER,9)
                .securityLevel(1);
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

    @Test
    public void getAllAgents(){
        assertThat(agentManager.listAgents().isEmpty());

        Agent agentJames = sampleJamesBondBuilder().build();
        Agent agentMagnum = sampleMagnusGiriBuilder().build();

        agentManager.createAgent(agentJames);
        agentManager.createAgent(agentMagnum);

        assertThat(agentManager.listAgents())
                .usingFieldByFieldElementComparator()
                .containsOnly(agentJames,agentMagnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullAgent(){
        agentManager.createAgent(null);
    }

    @Test
    public void createAgentWithExistingId(){
        Agent agentMagnum = sampleMagnusGiriBuilder()
                        .id(1L)
                        .build();
        expectedException.expect(IllegalArgumentException.class);
        agentManager.createAgent(agentMagnum);
    }

    @Test
    public void createAgentWithNullName(){
        Agent agentJames = sampleJamesBondBuilder()
                        .name(null)
                        .build();
        assertThatThrownBy(() -> agentManager.createAgent(agentJames))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createAgentWithInvalidSecurityLevel(){
        Agent agentMagnum = sampleMagnusGiriBuilder()
                        .securityLevel(-1)
                        .build();
        Agent agentJames = sampleJamesBondBuilder()
                        .securityLevel(4)
                        .build();

        assertThatThrownBy(() -> agentManager.createAgent(agentJames))
                            .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> agentManager.createAgent(agentMagnum))
                            .isInstanceOf(ValidationException.class);
    }

}
