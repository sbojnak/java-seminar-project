package cz.muni.fi.pv168.secretagency.Assignment;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Mission.Mission;

import java.util.List;

public interface AssignmentManager {

	/**
	 * 
	 * @param assignment
	 */
	void createAssignment(Assignment assignment);

	/**
	 * 
	 * @param assignment
	 */
	void removeAssignment(Assignment assignment);

	/**
	 * 
	 * @param mission
	 */
	List<Agent> findAgentsOfMission(Mission mission);

	/**
	 * 
	 * @param agent
	 */
	List<Mission> findMissionsOfAgent(Agent agent);

	/**
	 * 
	 * @param assignment
	 */
	Boolean isAssignmentDone(Assignment assignment);

	/**
	 * 
	 * @param assignment
	 */
	void updateAssignment(Assignment assignment);

	/**
	 * 
	 * @param assignmentId
	 */
	Assignment findAssignmentById(Long assignmentId);

}