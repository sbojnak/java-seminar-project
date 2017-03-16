package cz.muni.fi.pv168.secretagency;

import a.i.A;
import a.j.V;
import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.bind.ValidationException;

import java.time.LocalDate;

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
    // test for creating agent
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
        expectedException.expect(ValidationException.class);
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
        Agent agentJames = sampleJamesBondBuilder()
                        .securityLevel(4)
                        .build();

        assertThatThrownBy(() -> agentManager.createAgent(agentJames))
                            .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createAgentWithNegativeSecurityLevel(){
        Agent agentMagnum = sampleMagnusGiriBuilder()
                .securityLevel(-1)
                .build();

        assertThatThrownBy(() -> agentManager.createAgent(agentMagnum))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createAgentWithNullBirthDate(){
        Agent agentMagnum = sampleMagnusGiriBuilder()
                        .birthDate(null)
                        .build();
        agentManager.createAgent(agentMagnum);

        assertThat(agentManager.findAgentById(agentMagnum.getId()))
                        .isNotNull()
                        .isEqualToComparingFieldByField(agentMagnum);
    }

    //--------------------------------------------------------------------------
    // Tests for editing information about Agent (AgentManagerImpl.updateAgent(Agent agent)
    //--------------------------------------------------------------------------

    @FunctionalInterface
    private interface Operation<T> {
        void callOn(T subjectOfOperation);
    }

    private void testUpdateAgent(Operation<Agent> updateOperation) {
        Agent agentForUpdate = sampleMagnusGiriBuilder().build();
        Agent anotherAgent = sampleJamesBondBuilder().build();
        agentManager.createAgent(agentForUpdate);
        agentManager.createAgent(anotherAgent);

        updateOperation.callOn(agentForUpdate);

        agentManager.updateAgent(agentForUpdate);
        assertThat(agentManager.findAgentById(agentForUpdate.getId()))
                .isEqualToComparingFieldByField(agentForUpdate);
        // Check if updates didn't affected other records
        assertThat(agentManager.findAgentById(anotherAgent.getId()))
                .isEqualToComparingFieldByField(anotherAgent);
    }

    @Test
    public void updateAgentName(){
            testUpdateAgent((agent) -> agent.setName("Syrdan amudan"));
    }

    @Test void updateAgentBirthDate(){
        testUpdateAgent((agent) -> agent.setBirthDate(LocalDate.of(1995,SEPTEMBER,2)));
    }

    @Test
    public void updateAgentSecurityLevel(){
        testUpdateAgent((agent) -> agent.setSecurityLevel(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullAgent() {
        agentManager.updateAgent(null);
    }

    @Test
    public void updateAgentWithNullId() {
        Agent agent = sampleMagnusGiriBuilder().id(null).build();
        expectedException.expect(IllegalArgumentException.class);
        agentManager.updateAgent(agent);
    }

    @Test
    public void updateNonExistingAgent() {
        Agent agent = sampleMagnusGiriBuilder().id(1L).build();
        expectedException.expect(ValidationException.class);
        agentManager.updateAgent(agent);
    }

    @Test
    public void updateAgentWithNullName(){
        Agent agent = sampleJamesBondBuilder().build();
        agentManager.createAgent(agent);

        agent.setName(null);

        expectedException.expect(ValidationException.class);
        agentManager.updateAgent(agent);
    }

    @Test
    public void updateAgentWithNullBirthDate(){
        Agent agent = sampleMagnusGiriBuilder().build();
        agentManager.createAgent(agent);

        agent.setBirthDate(null);

        expectedException.expect(ValidationException.class);
        agentManager.updateAgent(agent);
    }

    @Test
    public void updateAgentWithInvalidSecurityLevel(){
        Agent agent = sampleMagnusGiriBuilder().build();
        agentManager.createAgent(agent);

        agent.setSecurityLevel(5);

        expectedException.expect(ValidationException.class);
        agentManager.updateAgent(agent);
    }

    @Test
    public void updateAgentWithNegativeSecurityLevel(){
        Agent agent = sampleMagnusGiriBuilder().build();
        agentManager.createAgent(agent);

        agent.setSecurityLevel(-2);

        expectedException.expect(ValidationException.class);
        agentManager.updateAgent(agent);
    }

    //--------------------------------------------------------------------------
    // Tests for deleting Agent (AgentManagerImpl.deleteAgent(Long id)
    //--------------------------------------------------------------------------

    @Test
    public void deleteAgent(){
        Agent james = sampleJamesBondBuilder().build();
        Agent magnus = sampleMagnusGiriBuilder().build();
        agentManager.createAgent(james);
        agentManager.createAgent(magnus);

        assertThat(agentManager.findAgentById(james.getId())).isNotNull();
        assertThat(agentManager.findAgentById(magnus.getId())).isNotNull();

        agentManager.deleteAgent(james);

        assertThat(agentManager.findAgentById(james.getId())).isNull();
        assertThat(agentManager.findAgentById(magnus.getId())).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullAgent() {
        agentManager.deleteAgent(null);
    }

    @Test
    public void deleteAgentWithNullId(){
        Agent agent = sampleMagnusGiriBuilder().id(null).build();
        expectedException.expect(IllegalArgumentException.class);
        agentManager.deleteAgent(agent);
    }

    @Test
    public void deleteNonExistingAgent(){
        Agent agent = sampleMagnusGiriBuilder().id(1L).build();
        expectedException.expect(IllegalArgumentException.class);
        agentManager.deleteAgent(agent);
    }

    //--------------------------------------------------------------------------
    // Tests for listing all agents (AgentManagerImpl.listAgents()
    //--------------------------------------------------------------------------

    @Test
    public void listAllAgents(){
        Agent magnus = sampleMagnusGiriBuilder().build();
        Agent james = sampleJamesBondBuilder().build();

        agentManager.createAgent(magnus);
        agentManager.createAgent(james);

        assertThat(agentManager.listAgents()).hasSize(2)
                        .containsOnly(magnus,james);
    }


}
