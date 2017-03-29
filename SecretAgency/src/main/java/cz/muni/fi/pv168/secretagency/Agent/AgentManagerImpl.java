package cz.muni.fi.pv168.secretagency.Agent;

import java.util.List;
import javax.sql.DataSource;

public class AgentManagerImpl implements AgentManager {

    private final DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * 
	 * @param id Id to search for
	 */
	public Agent findAgentById(Long id) {
		// TODO - implement AgentManagerImpl.findAgentById
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param agent agent to be created
	 */
	public void createAgent(Agent agent) {

	}

	/**
	 * 
	 * @param agent agent to be edited
	 */
	public void updateAgent(Agent agent) {
		// TODO - implement AgentManagerImpl.updateAgent
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param agent Agent which will be deleted
	 */
	public void deleteAgent(Agent agent) {
		// TODO - implement AgentManagerImpl.deleteAgent
		throw new UnsupportedOperationException();
	}

	public List<Agent> listAgents() {
		// TODO - implement AgentManagerImpl.listAgents
		throw new UnsupportedOperationException();
	}

}