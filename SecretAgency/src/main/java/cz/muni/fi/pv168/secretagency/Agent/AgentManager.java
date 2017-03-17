package cz.muni.fi.pv168.secretagency.Agent;

import java.util.List;

public interface AgentManager {

	/**
	 * 
	 * @param id Id to search for
	 */
	Agent findAgentById(Long id);

	/**
	 * 
	 * @param agent agent to be created
	 */
	void createAgent(Agent agent);

	/**
	 * 
	 * @param agent agent to be edited
	 */
	void updateAgent(Agent agent);

	/**
	 * 
	 * @param agent agent which will be deleted
	 */
	void deleteAgent(Agent agent);

	List<Agent> listAgents();



}