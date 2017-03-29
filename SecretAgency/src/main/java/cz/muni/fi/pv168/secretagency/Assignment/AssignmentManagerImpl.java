package cz.muni.fi.pv168.secretagency.Assignment;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Mission.Mission;

import java.util.List;
import javax.sql.DataSource;

public class AssignmentManagerImpl implements AssignmentManager {

	private final DataSource dataSource;

	public AssignmentManagerImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 
	 * @param assignment assigment to be created
	 */
	public void createAssignment(Assignment assignment) {
		// TODO - implement AssignmentManagerImpl.createAssignment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param assignment assignment to be removed
	 */
	public void removeAssignment(Assignment assignment) {
		// TODO - implement AssignmentManagerImpl.removeAssignment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param mission mission from which we want agents
	 */
	public List<Agent> findAgentsOfMission(Mission mission) {
		// TODO - implement AssignmentManagerImpl.findAgentsOfMission
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param agent agent which missions we want
	 */
	public List<Mission> findMissionsOfAgent(Agent agent) {
		// TODO - implement AssignmentManagerImpl.findMissionsOfAgent
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param assignment assignment to be checked
	 */
	public Boolean isAssignmentDone(Assignment assignment) {
		// TODO - implement AssignmentManagerImpl.isAssignmentDone
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param assignment assignment to be updated
	 */
	public void updateAssignment(Assignment assignment) {
		// TODO - implement AssignmentManagerImpl.updateAssignment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param assignmentId id of assignment
	 */
	public Assignment findAssignmentById(Long assignmentId) {
		// TODO - implement AssignmentManagerImpl.findAssignment
		throw new UnsupportedOperationException();
	}

}