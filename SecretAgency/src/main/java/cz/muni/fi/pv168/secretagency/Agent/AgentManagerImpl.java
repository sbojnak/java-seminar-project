package cz.muni.fi.pv168.secretagency.Agent;

import cz.muni.fi.pv168.secretagency.commons.DBUtils;
import cz.muni.fi.pv168.secretagency.commons.ServiceFailureException;
import org.apache.derby.client.am.Statement;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.st;

public class AgentManagerImpl implements AgentManager {

    private final DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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

    private static Agent rowToAgent(ResultSet rs) throws SQLException {
        Agent result = new Agent();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("name"));
        result.setBirthDate(toLocalDate(rs.getDate("birthDate")));
        result.setSecurityLevel(rs.getInt("securityLevel"));
        return result;
    }

    /**
	 * 
	 * @param id Id to search for
	 */
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

	/**
	 * 
	 * @param agent agent to be created
	 */
	public void createAgent(Agent agent) throws ServiceFailureException{
	    checkDataSource();
        validateAgent(agent);

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