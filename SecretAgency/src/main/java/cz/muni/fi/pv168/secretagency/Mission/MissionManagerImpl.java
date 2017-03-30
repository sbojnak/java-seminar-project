package cz.muni.fi.pv168.secretagency.Mission;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
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

public class MissionManagerImpl implements MissionManager {

    private final DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static List<Mission> executeQueryForMultipleBodies(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Mission> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowToMission(rs));
        }
        return result;
    }

	private static Mission executeForSingleMission(PreparedStatement preparedStatement) throws SQLException, ServiceFailureException {
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			Mission result = rowToMission(rs);
			if (rs.next()) {
				throw new ServiceFailureException(
						"Internal integrity error: more agents with the same id found!");
			}
			return result;
		} else {
			return null;
		}
	}

	private static Mission rowToMission(ResultSet rs) throws SQLException {
		Mission result = new Mission();
		result.setDescription(rs.getString("description"));
		result.setGoal(rs.getString("goal"));
		result.setLocation(rs.getString("location"));
		result.setId(rs.getLong("id"));
		result.setName(rs.getString("name"));
		return result;
	}

    /**
	 * 
	 * @param id mission id
	 */
	public Mission findMissionById(Long id) {
		if (dataSource == null) {
			throw new IllegalStateException("DataSource is not set");
		}
		if(id == null){
			throw new IllegalArgumentException("Argument id is null");
		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement("SELECT id, name, goal, location, description FROM mission WHERE id = ?");
			preparedStatement.setLong(1, id);
			return executeForSingleMission(preparedStatement);
		}
		catch(SQLException e){
			throw new ServiceFailureException("SQL Error was thrown while trying to get mission with id" + id, e);
		}
		finally {
			DBUtils.closeQuietly(connection, preparedStatement);
		}
	}

	/**
	 * 
	 * @param mission mission to create
	 */
	public void createMission(Mission mission) {
		if (dataSource == null) {
			throw new IllegalStateException("DataSource is not set");
		}
		if(mission == null){
			throw new IllegalArgumentException("Mission to be created is null");
		}
		if(mission.getName().length() > 40){
			throw new IllegalArgumentException("Mission name is too long");
		}
		if(mission.getGoal() == null){
			throw new IllegalArgumentException("Mission goal cannot be null");
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("INSERT INTO mission" +
					"(name, goal, location, description) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, mission.getName());
			preparedStatement.setString(2, mission.getGoal());
			preparedStatement.setString(3, mission.getLocation());
			preparedStatement.setString(4, mission.getDescription());
			int addedRows = preparedStatement.executeUpdate();
			DBUtils.checkUpdatesCount(addedRows, mission, true);
			Long id = DBUtils.getId(preparedStatement.getGeneratedKeys());
			mission.setId(id);
			connection.commit();
		} catch (SQLException e) {
			throw new ServiceFailureException("SQL Error was thrown while trying to create mission", e);
		}
		finally {
			DBUtils.doRollbackQuietly(connection);
			DBUtils.closeQuietly(connection, preparedStatement);
		}
	}

	/**
	 * 
	 * @param mission mission to edit
	 */
	public void editMission(Mission mission) {
		if (dataSource == null) {
			throw new IllegalStateException("DataSource is not set");
		}
		if(mission == null){
			throw new IllegalArgumentException("Mission to be edited is null");
		}

		if(mission.getId() == null){
		    throw new IllegalArgumentException("Mission id is null");
        }

		if(mission.getName().length() > 40){
			throw new IllegalArgumentException("Mission name is too long");
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("UPDATE mission " +
                    "SET name = ?, goal = ?, location = ?, description = ? WHERE id = ?");
            preparedStatement.setString(1, mission.getName());
            preparedStatement.setString(2, mission.getGoal());
            preparedStatement.setString(3, mission.getLocation());
            preparedStatement.setString(4, mission.getDescription());
            preparedStatement.setLong(5, mission.getId());
			int count = preparedStatement.executeUpdate();
			DBUtils.checkUpdatesCount(count, mission, false);
            connection.commit();
		} catch (SQLException e) {
			throw new ServiceFailureException("SQL Error was thrown while trying to edit mission", e);
		}
		finally {
			DBUtils.doRollbackQuietly(connection);
			DBUtils.closeQuietly(connection, preparedStatement);
		}
	}

	/**
	 * 
	 * @param mission mission which will be deleted
	 */
	public void deleteMission(Mission mission) {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
        if(mission == null){
            throw new IllegalArgumentException("Mission to be edited is null");
        }

        if(mission.getId() == null){
            throw new IllegalArgumentException("Mission id is null");
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("DELETE FROM mission WHERE id = ?");
            preparedStatement.setLong(1, mission.getId());
			preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new ServiceFailureException("SQL Error was thrown while trying to delete mission", e);
        }
        finally {
            DBUtils.doRollbackQuietly(connection);
            DBUtils.closeQuietly(connection, preparedStatement);
        }
	}

	public List<Mission> listMissions() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection
                    .prepareStatement("SELECT id, name, goal, location, description FROM mission");
            return executeQueryForMultipleBodies(preparedStatement);
        }
        catch(SQLException e){
            throw new ServiceFailureException("SQL Error was thrown while trying to get all missions", e);
        }
        finally {
            DBUtils.closeQuietly(connection, preparedStatement);
        }
    }
}