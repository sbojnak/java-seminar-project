package cz.muni.fi.pv168.secretagency.Agent;

import java.util.List;

public interface AgentManager {

	/**
	 * 
	 * @param id
	 */
	Agent findAgentById(Long id);

	/**
	 * 
	 * @param agent
	 */
	void createAgent(Agent agent);

	/**
	 * 
	 * @param agent
	 */
	void editAgent(Agent agent);

	/**
	 * 
	 * @param id
	 */
	void deleteAgent(Long id);

	List<Agent> listAgents();

}