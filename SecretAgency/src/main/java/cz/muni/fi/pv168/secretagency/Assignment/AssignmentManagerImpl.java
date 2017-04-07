package cz.muni.fi.pv168.secretagency.Assignment;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Agent.AgentManager;
import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import cz.muni.fi.pv168.secretagency.Mission.Mission;
import cz.muni.fi.pv168.secretagency.Mission.MissionManager;
import cz.muni.fi.pv168.secretagency.Mission.MissionManagerImpl;
import cz.muni.fi.pv168.secretagency.commons.DBUtils;
import cz.muni.fi.pv168.secretagency.commons.ServiceFailureException;
import org.apache.derby.client.am.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class AssignmentManagerImpl implements AssignmentManager {

	private final DataSource dataSource;

	public AssignmentManagerImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void createAssignment(Assignment assignment) {
		validateAssignment(assignment);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO ASSIGNMENT (agentID, missionID, jobCompleted) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, assignment.getAgent().getId());
			preparedStatement.setLong(2, assignment.getMission().getId());
			preparedStatement.setBoolean(3, assignment.isJobCompleted());
			int addedRows = preparedStatement.executeUpdate();
			if (addedRows != 1) {
				throw new ServiceFailureException("Number of added rows must be one, you add " + addedRows);
			}
			ResultSet keyResultSet = preparedStatement.getGeneratedKeys();
			assignment.setId(getKey(keyResultSet, assignment));

		} catch (SQLException ex) {
			throw new ServiceFailureException("Error while creating " + assignment, ex);
		} finally {
			DBUtils.closeQuietly(connection, preparedStatement);

		}
	}

	@Override
	public void removeAssignment(Assignment assignment) {
		if (validateId(assignment.getId())) {
			throw new IllegalArgumentException("ID must be greater than zero");
		}
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 "DELETE FROM ASSIGNMENT WHERE id=?", Statement.RETURN_GENERATED_KEYS)) {
			statement.setLong(1, assignment.getId());
			if (statement.executeUpdate() != 1) {
				throw new ServiceFailureException("Did not delete impl with id =" + assignment.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public List<Agent> findAgentsOfMission(Mission mission) {
		AgentManager agentManager = new AgentManagerImpl(dataSource);
		List<Agent> result = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 "SELECT id,agentID,missionID,jobCompleted FROM ASSIGNMENT WHERE missionID=?", Statement.RETURN_GENERATED_KEYS)) {
			statement.setLong(1, mission.getId());
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				result.add(agentManager.findAgentById(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Mission> findMissionsOfAgent(Agent agent) {
		MissionManagerImpl missionManager = new MissionManagerImpl(dataSource);
		List<Mission> result = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 "SELECT id,agentID,missionID FROM ASSIGNMENT WHERE agentID=?", Statement.RETURN_GENERATED_KEYS)) {
			statement.setLong(1, agent.getId());
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				result.add(missionManager.findMissionById(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param assignment assignment to be checked
	 */
	public Boolean isAssignmentDone(Assignment assignment) {
		if(assignment.getId() == null) {
			throw new IllegalArgumentException
					("Id of assignment is null, not possible to find data in table");
		}
		Assignment assignmentFromDB = findAssignmentById(assignment.getId());
		return assignmentFromDB.isJobCompleted();
	}

	@Override
	public void updateAssignment(Assignment assignment) {
		validateAssignment(assignment);

		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 "UPDATE ASSIGNMENT SET agentID=?,missionID=?,jobCompleted=? WHERE id=?", Statement.RETURN_GENERATED_KEYS)) {
			statement.setLong(1, assignment.getAgent().getId());
			statement.setLong(2, assignment.getMission().getId());
			statement.setBoolean(3,assignment.isJobCompleted());
			statement.setLong(4, assignment.getId());
			if (statement.executeUpdate() != 1) {
				throw new IllegalArgumentException("cannot update impl " + assignment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param assignmentId id of assignment
	 */
	public Assignment findAssignmentById(Long assignmentId) {
		if(dataSource == null) {
			throw new IllegalStateException("DataSource is not set");
		}

		if (assignmentId == null) {
			throw new IllegalArgumentException("id is null");
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(
					"SELECT id, agentID, missionID, jobCompleted FROM ASSIGNMENT WHERE id = ?");
			preparedStatement.setLong(1, assignmentId);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			return rowToAssignment(rs);
		} catch (SQLException ex) {
			throw new ServiceFailureException("Error when selecting assignment with id " + assignmentId, ex);
		} finally {
			DBUtils.closeQuietly(connection, preparedStatement);
		}
	}

	private Assignment rowToAssignment(ResultSet rs) throws SQLException {
		AgentManager agentManager = new AgentManagerImpl(dataSource);
		MissionManager missionManager = new MissionManagerImpl(dataSource);
		Assignment assignment = new Assignment();
		assignment.setAgent(agentManager.findAgentById(rs.getLong("agentId")));
		assignment.setMission(missionManager.findMissionById(rs.getLong("missionId")));
		assignment.setJobCompleted(rs.getBoolean("jobCompleted"));
		assignment.setId(rs.getLong("id"));
		return assignment;
	}

	private void validateAssignment(Assignment assignment) {
		if (assignment == null) {
			throw new IllegalArgumentException("Assignment can't be null.");
		} else if (validateId(assignment.getAgent().getId())) {
			throw new IllegalArgumentException("Agent's id must be greater or equals to 0");
		} else if (validateId(assignment.getMission().getId())) {
			throw new IllegalArgumentException("Mission's id must be greater or equals to 0");
		}
	}

	private boolean validateId(Long id) {
		return id == null || id < 0;
	}

	private Long getKey(ResultSet keyResultSet, Assignment assignment) throws SQLException, ServiceFailureException {
		if (keyResultSet.next()) {
			if (keyResultSet.getMetaData().getColumnCount() != 1) {
				throw new ServiceFailureException("Error when inserting into " + assignment
						+ " key fields count is wrong: " + keyResultSet.getMetaData().getColumnCount());
			}

			Long result = keyResultSet.getLong(1);
			if (keyResultSet.next()) {
				throw new ServiceFailureException("Error when inserting into " + assignment + " more keys founded");
			}
			return result;
		} else {
			throw new ServiceFailureException("Error when inserting into " + assignment + " no key found");
		}
	}
}