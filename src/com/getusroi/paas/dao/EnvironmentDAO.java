package com.getusroi.paas.dao;

import static com.getusroi.paas.helper.PAASConstant.MYSQL_DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.db.helper.DataBaseConnectionFactory;
import com.getusroi.paas.db.helper.DataBaseHelper;
import com.getusroi.paas.helper.PAASConstant;
import com.getusroi.paas.helper.PAASErrorCodeExceptionHelper;
import com.getusroi.paas.vo.ApplicantSummary;
import com.getusroi.paas.vo.EnvironmentType;
import com.getusroi.paas.vo.Environments;
import com.mysql.jdbc.PreparedStatement;
import com.paas_gui.vpc.MarathonRest;

/**
 * this class contains all DAO operation of environment page
 * @author bizruntime
 *
 */
public class EnvironmentDAO {

	private static final Logger LOG = LoggerFactory.getLogger(EnvironmentDAO.class);

	private static final String INSERT_ENVIRONMENT_SERVICE_QUERY = "INSERT INTO environment_types VALUES(?,?,?,?,?,?,?)";
	private static final String GET_ALL_ENVIRONMENT_QUERY = "SELECT * FROM environment_types";
	private static final String DELETE_ENVIRONMENT_BY_NAME = "DELETE FROM environment_types WHERE name = ?";
	private static final String SELECT_ALL_ENVIRONMENTS_LIST = "SELECT * FROM envirnament";
	private static final String SELECT_APPLICATION_SUMMARY_BY_APPLICATION_NAME = "SELECT applicantName FROM appsummary";
	private static final String SELECT_IMAGE_REPOSITORY_FROM_APPLICANT_NAME = "SELECT imageRepository FROM appsummary WHERE applicantName=?";
	private static final String INSERT_ENVIRONMENTS_QUERY = "INSERT INTO envirnament VALUES(?,?,?,?,?,?)";
	private static final String SELECT_ADD_SERVICE_QUERY = "SELECT * FROM addService";
	private static final String READ_ENVIRONMENT_VARIABLE = "SELECT * FROM environment_variable WHERE serviceName =?";
	private static final String READ_ROUTE = "SELECT * FROM route WHERE serviceName =?";


	/**
	 * this method is used to insert the environment type data
	 * 
	 * @param environmentTypes
	 *            : EnvironmentTypes object contains data which is going to
	 *            insert
	 * @throws DataBaseOperationFailedException
	 *             : Unable to insert EnvironmentTypes
	 */
	public void insertEnvironmentType(EnvironmentType environmentTypes) throws DataBaseOperationFailedException {
		LOG.debug(".insertEnvironmentType method of insertEnvironmentType");
		DataBaseConnectionFactory dataBaseConnectionFactory = new DataBaseConnectionFactory();
		Connection connection = null;
		PreparedStatement pStatement = null;
		try {
			connection = dataBaseConnectionFactory.getConnection(MYSQL_DB);
			pStatement = (PreparedStatement) connection.prepareStatement(INSERT_ENVIRONMENT_SERVICE_QUERY);
			pStatement.setString(1, environmentTypes.getName());
			pStatement.setString(2, environmentTypes.getDescription());
			pStatement.setString(3, environmentTypes.getAcceptTag());
			pStatement.setString(4, environmentTypes.getPromoteTag());
			pStatement.setString(5, environmentTypes.getAction());
			pStatement.setInt(6, environmentTypes.getRestartInterval());
			pStatement.setInt(7, environmentTypes.getQuietPeriod());

			pStatement.executeUpdate();
			LOG.debug("EnvironmentType Data is Updated");

		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to insert environment type into db with data: " + environmentTypes);
			throw new DataBaseOperationFailedException(
					"Unable to insert environment type into db with data : " + environmentTypes, e);
		
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to insert data into environmenttype because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to insert data into environmenttype because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException(
						"Unable to insert environment type into db with data : " + environmentTypes, e);
		} finally {
			DataBaseHelper.dbCleanUp(connection, pStatement);
		}

	} // end of insertEnvironmentType method

	/**
	 * this method is used to get all environment type data from db
	 * 
	 * @throws DataBaseOperationFailedException
	 *             : Unable to fetch EnvironmentTypes
	 */

	public List<EnvironmentType> getAllEnvironmentType() throws DataBaseOperationFailedException {

		LOG.debug(".getAllEnvironmentType of EnvironmentDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		List<EnvironmentType> environmentTypesList = new LinkedList<EnvironmentType>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(GET_ALL_ENVIRONMENT_QUERY);
			if (resultSet != null) {
				while (resultSet.next()) {
					EnvironmentType environmentType = new EnvironmentType();
					environmentType.setName(resultSet.getString("name"));
					environmentType.setAction(resultSet.getString("description"));
					environmentType.setAcceptTag(resultSet.getString("accept_tag"));
					environmentType.setDescription(resultSet.getString("promote_tag"));
					environmentType.setPromoteTag(resultSet.getString("action"));
					environmentType.setQuietPeriod(resultSet.getInt("restart_interval"));
					environmentType.setRestartInterval(resultSet.getInt("quiet_period"));

					environmentTypesList.add(environmentType);

				}
			} else {
				LOG.debug("No data is available on environment");
			}

		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to fetch environment into db ");
			throw new DataBaseOperationFailedException("Unable to environment summary ", e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to fetch environment into db: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to fetch environment into db: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to environment summary ", e);
		} finally {
			DataBaseHelper.dbCleanUp(connection, statement);
		}
		return environmentTypesList;
	}

	/**
	 * this is used to delete environment data by name
	 * 
	 * @param name
	 *            is used to delete data from database
	 * @throws DataBaseOperationFailedException
	 *             : Unable to delete EnvironmentTypes
	 */

	public void deleteEnvironmentTypeByName(String name) throws DataBaseOperationFailedException {
		LOG.debug(".deleteEnvironmentTypeByName of EnvironmentDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			preparedStatement = (PreparedStatement) connection.prepareStatement(DELETE_ENVIRONMENT_BY_NAME);
			preparedStatement.setString(1, name);
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to delete environment from db ");
			throw new DataBaseOperationFailedException("Unable to delete environment from db ", e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to delete environment from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to delete environment from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to delete environment from db ", e);
		} finally {
			DataBaseHelper.dbCleanUp(connection, preparedStatement);
		}

	} // end of deleteEnvironmentTypeByName

	/**
	 * this method used to get all environments data from db
	 * 
	 * @return : return the list of environments data
	 * @throws DataBaseOperationFailedException
	 *             : Unable to select environments
	 */

	public List<Environments> getAllEnvironmentsList() throws DataBaseOperationFailedException {

		LOG.debug(".deleteEnvironmentTypeByName of EnvironmentDAO");
		List<Environments> environmentsList = new ArrayList<Environments>();
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			preparedStatement = (PreparedStatement) connection.prepareStatement(SELECT_ALL_ENVIRONMENTS_LIST);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Environments environments = new Environments();
				environments.setContainerName(resultSet.getString("containername"));
				environments.setService(resultSet.getString("service"));
				environments.setTag(resultSet.getString("tag"));
				environments.setHost("192.168.1.219");
				String ipadde = MarathonRest.getgatewayRoute();
				environments.setIpadress(ipadde);

				environments.setState(resultSet.getString("state"));

				environmentsList.add(environments);
			}

		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to get environmentlist from db ");
			throw new DataBaseOperationFailedException("Unable to get environmentlist from db ", e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to get environmentlist from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to get environmentlist from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to get environmentlist from db ", e);
		} finally {
			DataBaseHelper.dbCleanup(connection, preparedStatement, resultSet);
		}

		return environmentsList;

	} // end of method

	/**
	 * this method is used to get application summary by name
	 * 
	 * @return : it return the application summary list
	 * @throws DataBaseOperationFailedException
	 *             : Unable to get application summary
	 */

	public List<ApplicantSummary> getApplicationSummaryByApplicationName() throws DataBaseOperationFailedException {
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		List<ApplicantSummary> applicantSummaryList = new LinkedList<ApplicantSummary>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			preparedStatement = (PreparedStatement) connection
					.prepareStatement(SELECT_APPLICATION_SUMMARY_BY_APPLICATION_NAME);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ApplicantSummary applicantSummary = new ApplicantSummary();
				applicantSummary.setApplicantionName(resultSet.getString("applicantName"));
				applicantSummaryList.add(applicantSummary);
			}
		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to get application summary from db ");
			throw new DataBaseOperationFailedException("Unable to get application summary from db  ", e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to get application summary from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to get application summary from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to get application summary from db  ", e);
		} finally {
			DataBaseHelper.dbCleanup(connection, preparedStatement, resultSet);
		}
		return applicantSummaryList;
	} // end of getApplicationSummaryByApplicationName method

	/**
	 * this method is used to select image repository from repository name
	 * 
	 * @param repoName
	 *            : repository name
	 * @return : return image repository
	 * @throws DataBaseOperationFailedException
	 *             : Unable to select image repository
	 */

	public ApplicantSummary selectImageRepositoryFromRepositoryName(String repoName)
			throws DataBaseOperationFailedException {

		LOG.debug(".selectImageRepositoryFromRepositoryName of EnvironmentDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ApplicantSummary applicantSummary = null;

		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			preparedStatement = (PreparedStatement) connection
					.prepareStatement(SELECT_IMAGE_REPOSITORY_FROM_APPLICANT_NAME);
			preparedStatement.setString(1, repoName);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				applicantSummary = new ApplicantSummary();
				applicantSummary.setImageRepository(resultSet.getString("imageRepository"));
			}
		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to get select image regesitory from summary from db ");
			throw new DataBaseOperationFailedException("Unable to get select image regesitory from summary from db  ",
					e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to get select image regesitory from summary from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to get select image regesitory from summary from db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to get select image regesitory from summary from db  ",
						e);
		} finally {
			DataBaseHelper.dbCleanup(connection, preparedStatement, resultSet);
		}
		return applicantSummary;
	} // end of selectImageRegesitoryFromSummary method

	/**
	 * this method is used to insert all environments values into db
	 * 
	 * @param environments
	 *            : environments object contains values of environment
	 * @return : return true if successfully insert data
	 * @throws DataBaseOperationFailedException
	 *             : Unable to insert environments into db
	 */

	public boolean insertAllEnvironmentsData(Environments environments) throws DataBaseOperationFailedException {

		LOG.debug(".insertAllEnvironmentsData of EnvironmentDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			preparedStatement = (PreparedStatement) connection.prepareStatement(INSERT_ENVIRONMENTS_QUERY);
			preparedStatement.setString(1, environments.getContainerName());
			preparedStatement.setString(2, environments.getService());
			preparedStatement.setString(3, environments.getTag());
			preparedStatement.setString(4, environments.getHost());
			preparedStatement.setString(5, environments.getIpadress());
			preparedStatement.setString(6, environments.getState());
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException | IOException e) {
			LOG.error("Unable to insert environment data into db: " + environments);
			throw new DataBaseOperationFailedException("Unable to insert environment data into db  " + environments, e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to insert environment data into db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to insert environment data into db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			}
		} finally {
			DataBaseHelper.dbCleanUp(connection, preparedStatement);
		}

		return true;
	} // end of insertAllEnvironmentsData method
	
}