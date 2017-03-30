package cz.muni.fi.pv168.secretagency.Agent;

import cz.muni.fi.pv168.secretagency.commons.DBUtils;
import cz.muni.fi.pv168.secretagency.commons.IllegalEntityException;
import cz.muni.fi.pv168.secretagency.commons.ServiceFailureException;
import org.apache.derby.client.am.Statement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class AgentManagerImpl implements AgentManager {

    private final DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
	public Agent findAgentById(Long id) {
        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT id, name, birthDate, securityLevel FROM agent WHERE id = ?");
            preparedStatement.setLong(1, id);
            return executeQueryForSingleAgent(preparedStatement);
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when selecting agent with id " + id, ex);
        } finally {
            DBUtils.closeQuietly(connection, preparedStatement);
        }
	}

	@Override
	public void createAgent(Agent agent) throws ServiceFailureException{
	    checkDataSource();
        validateAgent(agent);
        if (agent.getId() != null) {
            throw new IllegalArgumentException("agent id is already set");
        }
        if(agent.getSecurityLevel() > 3 || agent.getSecurityLevel() < 0){
            throw new IllegalArgumentException("invalid securityLevel can't be set");
        }
        if(agent.getName() == null){
            throw new IllegalArgumentException("null name can't be set");
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(
                     "INSERT INTO AGENT (name, birthDate, securityLevel) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, agent.getName());
            preparedStatement.setDate(2, toSqlDate(agent.getBirthDate()));
            preparedStatement.setInt(3,agent.getSecurityLevel());
            int addedRows = preparedStatement.executeUpdate();
            DBUtils.checkUpdatesCount(addedRows, agent, true);
            Long id = DBUtils.getId(preparedStatement.getGeneratedKeys());
            agent.setId(id);
            connection.commit();
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting into Agent " + agent, ex);
        } finally {
            DBUtils.doRollbackQuietly(connection);
            DBUtils.closeQuietly(connection, preparedStatement);
        }
	}

	@Override
	public void updateAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        if (agent.getId() == null) {
            throw new IllegalArgumentException("agent id is null");
        }
        if(agent.getSecurityLevel() > 3 || agent.getSecurityLevel() < 0){
            throw new IllegalArgumentException("invalid securityLevel");
        }
        if(agent.getName() == null){
            throw new IllegalArgumentException("null name is set");
        }
        if(agent.getBirthDate() == null){
            throw new IllegalArgumentException("null birthdate cannot be added to the database");
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(
                    "UPDATE AGENT SET name = ?, birthDate = ?, securityLevel = ? WHERE id = ?");
            preparedStatement.setString(1, agent.getName());
            preparedStatement.setDate(2, toSqlDate(agent.getBirthDate()));
            preparedStatement.setInt(3, agent.getSecurityLevel());
            preparedStatement.setLong(4, agent.getId());

            int count = preparedStatement.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            connection.commit();
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Can not update agent " + agent);
        } finally {
            DBUtils.doRollbackQuietly(connection);
            DBUtils.closeQuietly(connection, preparedStatement);
        }
	}

	@Override
	public void deleteAgent(Agent agent) {
        checkDataSource();
        validateAgent(agent);
        if (agent.getId() == null) {
            throw new IllegalEntityException("grave id is null");
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM AGENT WHERE id = ?");
            preparedStatement.setLong(1, agent.getId());

            int count = preparedStatement.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.doRollbackQuietly(connection);
            DBUtils.closeQuietly(connection, preparedStatement);
        }
	}

	@Override
	public List<Agent> listAgents() {
        checkDataSource();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT id, name, birthDate, securityLevel FROM AGENT");
            return executeQueryForMultipleAgents(preparedStatement);
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error while selecting all agents", ex);
        }
        finally {
            DBUtils.closeQuietly(connection, preparedStatement);
        }
	}

    private void validateAgent(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("Agent can't be null.");
        }
    }

    private static Date toSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    private static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    static Agent executeQueryForSingleAgent(PreparedStatement preparedStatement) throws SQLException, ServiceFailureException {
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Agent result = rowToAgent(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more agents with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    static List<Agent> executeQueryForMultipleAgents(PreparedStatement preparedStatement) throws SQLException {
        ResultSet rs = preparedStatement.executeQuery();
        List<Agent> result = new ArrayList<Agent>();
        while (rs.next()) {
            result.add(rowToAgent(rs));
        }
        return result;
    }

    private static Agent rowToAgent(ResultSet rs) throws SQLException {
        Agent result = new Agent();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("name"));
        result.setBirthDate(toLocalDate(rs.getDate("birthDate")));
        result.setSecurityLevel(rs.getInt("securityLevel"));
        return result;
    }

}