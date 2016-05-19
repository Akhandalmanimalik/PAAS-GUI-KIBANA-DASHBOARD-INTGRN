package com.getusroi.paas.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.db.helper.DataBaseConnectionFactory;
import com.getusroi.paas.db.helper.DataBaseHelper;
import com.getusroi.paas.helper.PAASConstant;
import com.getusroi.paas.helper.PAASErrorCodeExceptionHelper;
import com.getusroi.paas.vo.PaasUserRegister;
import com.mysql.jdbc.PreparedStatement;

import static com.getusroi.paas.helper.PAASConstant.*;


/**
 * This is DAO Class to register paas user,get detail of Paas user, update and delete the paas user
 * @author bizruntime
 *
 */
public class PaasUserRegisterAndLoginDAO {
	 static final Logger logger = LoggerFactory.getLogger(PaasUserRegisterAndLoginDAO.class);
	 private static final String REGISTER_USER_QUERY="insert into tenant(tenant_name,tenant_email,company_name,company_address,createdDTM) values(?,?,?,?,NOW())";
	 private static final String ALL_REGISTERED_USER_QUERY="select * from tenant";
	 private static final String DELETE_RESGISTERED_USER_BY_EMAIL_COMPANYNAME="delete from tenant where tenant_email=? AND company_name=?";
	 private static final String LOGIN_QUERY="select * from tenant where tenant_email=? AND password=?";
	 private static final String CHECKEMAIL_EXIST_QUERY="select tenant_email from tenant where tenant_email=?";
	 private static final String CHECKEMAIl_AND_PASSWORD_EXIST_QUERY="select * from tenant where tenant_email=? and password=?";
	 
	 public static void main(String[] args) throws DataBaseOperationFailedException {
		 PaasUserRegister registerPaasUser = new PaasUserRegister();
		 registerPaasUser.setTenant_name("tenant1");
		 registerPaasUser.setCompany_name("Bizruntime");
		 registerPaasUser.setCompany_address("Sarjapur");
		 registerPaasUser.setEmail("Venkatesh.m@bizruntime.com");
		 registerPaasUser.setPassword("Bizruntime@123");
		 new PaasUserRegisterAndLoginDAO().registerPaasUser(registerPaasUser);
	}
	/**
	 * This method is used to  register user to paas
	 * @param paasUserRegister : PaasUserRegisterVO Object type contain details require to register the user
	 * @throws DataBaseOperationFailedException 
	 */
	public void registerPaasUser(PaasUserRegister paasUserRegister) throws DataBaseOperationFailedException{
		logger.debug(".registerPaasUser method of PaasUserRegisterDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt = null;
		try {
			connection = connectionFactory.getConnection(MYSQL_DB);			
			pstmt = (PreparedStatement) connection.prepareStatement(REGISTER_USER_QUERY);
			
			pstmt.setString(1, paasUserRegister.getTenant_name());
			pstmt.setString(2, paasUserRegister.getEmail());
			pstmt.setString(3, paasUserRegister.getCompany_name());
			pstmt.setString(4, paasUserRegister.getCompany_address());
			 
			pstmt.executeUpdate();
			logger.debug("Data Inserted");
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to register user to paas");
			throw new DataBaseOperationFailedException("Unable to register user to paas with detail : "+paasUserRegister.toString(),e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to register user to paas because " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to register user to paas because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to register user to paas with detail : "+paasUserRegister.toString(),e);
		} finally{
			DataBaseHelper.dbCleanUp(connection, pstmt);			
		}		

	}// end of method registerPaasUser
	
	/**
	 * This method is used to  get all user registered to paas
	 * @return List<PaasUserRegisterVO> : List of all Pass User Register
	 * @throws DataBaseOperationFailedException 
	 */
	public List<PaasUserRegister> getAllPaasUser() throws DataBaseOperationFailedException{
		logger.debug(".getAllPaasUser method of PaasUserRegisterDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		List<PaasUserRegister> paasUserList=new ArrayList<>();
		Connection connection=null;
		Statement stmt = null;
		ResultSet result=null;
		try {
			 connection = connectionFactory.getConnection(MYSQL_DB);
			 stmt= connection.createStatement();
			 result=stmt.executeQuery(ALL_REGISTERED_USER_QUERY);
			 if(result!=null){
				 while(result.next()){
					 String tenant_name = result.getString("tenant_name");
					 String company_name = result.getString("company_name");
					 String company_address = result.getString("company_address");
					 String email = result.getString("tenant_email");
					 String password = result.getString("password");
					 int id = result.getInt("id");
					 PaasUserRegister paasUser=new PaasUserRegister(company_name, company_address, email, password,id,tenant_name);
					 paasUserList.add(paasUser);
				 }
			 }else{
				 logger.debug("No data available in tenant table");
			 }
			 logger.debug("Paas user List : "+paasUserList);				 
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to get All users registered to paas ");
			throw new DataBaseOperationFailedException("Unable to get All users registered to paas",e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to get All users registered to paas because " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to get All users registered to paas because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to get All users registered to paas",e);
		} finally{
			DataBaseHelper.dbCleanup(connection, stmt, result);			
		}
		return paasUserList;

	}// end of method registerPaasUser
	
	/**
	 * This method is used to  delete registered user based
	 * @param paasUserRegister : PaasUserRegisterVO Object type contain details require to register the user
	 * @throws DataBaseOperationFailedException 
	 */
	public void deleteRegisteredUser(PaasUserRegister paasUserRegister) throws DataBaseOperationFailedException{
		logger.debug(".deleteRegisteredUser method of PaasUserRegisterDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt = null;
		try {
			 connection = connectionFactory.getConnection(MYSQL_DB);
			 pstmt = (PreparedStatement) connection.prepareStatement(DELETE_RESGISTERED_USER_BY_EMAIL_COMPANYNAME);
			 pstmt.setString(1,paasUserRegister.getEmail());
			 pstmt.setString(2,paasUserRegister.getCompany_name());
			 pstmt.executeUpdate();			 
			 logger.debug("registered user with email : "+paasUserRegister.getEmail()+"and companyName+"+paasUserRegister.getCompany_name()+" deleted successfully");				 
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to delete regsitered user with email : "+paasUserRegister.getEmail()+"and companyName+"+paasUserRegister.getCompany_name());
			throw new DataBaseOperationFailedException("Unable to delete regsitered user with email : "+paasUserRegister.getEmail()+"and companyName+"+paasUserRegister.getCompany_name(),e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to delete regsitered user because " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to delete regsitered user because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to delete regsitered user with email : "+paasUserRegister.getEmail()+"and companyName+"+paasUserRegister.getCompany_name(),e);
		} finally{
			DataBaseHelper.dbCleanUp(connection, pstmt);			
		}

	}// end of method registerPaasUser
	
	/**
	 * This method is used to login to pass 
	 * @param email : String used to login
	 * @param password : String used to login
	 * @return PaasUserRegister : PaasUserRegister
	 * @throws DataBaseOperationFailedException
	 */
	public PaasUserRegister loginToPaas(String email,String password) throws DataBaseOperationFailedException{
		logger.debug(".loginToPaas method of PaasUserRegisterDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt = null;
		 ResultSet result=null;
		  PaasUserRegister paasUser=null;

		try {
			 connection = connectionFactory.getConnection(MYSQL_DB);
			 pstmt = (PreparedStatement) connection.prepareStatement(LOGIN_QUERY);
			 pstmt.setString(1,email);
			 pstmt.setString(2,password);			 
			  result=pstmt.executeQuery();	
			 if(result.next()){
				 String tenant_name = result.getString("tenant_name");
				 String company_name=result.getString("company_name");
				 String company_address=result.getString("company_address");
				 email=result.getString("tenant_email");
				 password=result.getString("password");
				 int id=result.getInt("id");
				 paasUser=new PaasUserRegister(company_name, company_address, email, password,id,tenant_name);
				 logger.debug("Logged in   with email : "+email+"and password+"+password+" is  successfull");	
			 }
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to loggedIn  with email : "+email+"and password+"+password);
			throw new DataBaseOperationFailedException("Unable to loggedIn  with email : "+email+"and password+"+password,e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to loggedIn because " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to loggedIn because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to loggedIn  with email : "+email+"and password+"+password,e);
		} finally{
			DataBaseHelper.dbCleanUp(connection, pstmt);			
		}		
		return paasUser;
	}//end of method loginToPaas
	
	/**
	 * This method is used to check if email already exist in tenant table or not
	 * @param email : String , check if email already exist
	 * @return boolean : true if exist and false if not
	 * @throws DataBaseOperationFailedException 
	 */
	public boolean checkEmailExist(String email) throws DataBaseOperationFailedException{
		logger.debug(".checkEmailExist method of PaasUserRegisterDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt = null;
		 ResultSet result=null;
		boolean emailFlag=false;
		try {
			 connection = connectionFactory.getConnection(MYSQL_DB);
			 pstmt = (PreparedStatement) connection.prepareStatement(CHECKEMAIL_EXIST_QUERY);
			 pstmt.setString(1,email);			 			 
			  result=pstmt.executeQuery();	
			 if(result.next()){
				 emailFlag=true;
				 logger.debug("Email already exist with value : "+email);	
			 }
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to fetch email from tenant table with value : "+email);
			throw new DataBaseOperationFailedException("Unable to fetch email from regsiter table with value : "+email,e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to fetch email from tenant table " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to fetch email from regsiter table because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to fetch email from regsiter table with value : "+email,e);
		} finally{
			DataBaseHelper.dbCleanUp(connection, pstmt);			
		}		
		return emailFlag;
	}//end of method checkEmailExist
	
	/**
	 * This method is used to check if email and password already exist in tenant table or not
	 * @param email : String , check if email already exist
	 * @param password : String, check if password exist
	 * @return boolean : true if exist and false if not
	 * @throws DataBaseOperationFailedException 
	 */
	public PaasUserRegister userWithEmailPasswordExist(String email,String password) throws DataBaseOperationFailedException{
		
		logger.debug(".userWithEmailPasswordExist method of PaasUserRegisterDAO");
		DataBaseConnectionFactory connectionFactory = new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt = null;
		ResultSet result=null;

		PaasUserRegister paasUser=null;
		try {
			connection = connectionFactory.getConnection(MYSQL_DB);
			
			pstmt = (PreparedStatement) connection.prepareStatement(CHECKEMAIl_AND_PASSWORD_EXIST_QUERY);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			result = pstmt.executeQuery();
			if (result.next()) {
				String company_name = result.getString("company_name");
				String company_address = result.getString("company_address");
				email = result.getString("tenant_email");
				password = result.getString("password");
				int id = result.getInt("id");
				paasUser = new PaasUserRegister();
				paasUser.setCompany_address(company_address);
				paasUser.setCompany_name(company_name);
				paasUser.setEmail(email);
				paasUser.setId(id);
				logger.debug("Email and password already exist with value : "
						+ email + " and " + password);
			}
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to fetch data from regsiter table with value : "+email);
			throw new DataBaseOperationFailedException("Unable to fetch data from regsiter table with value : "+email+" and pass : "+password,e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to fetch data from regsiter table because " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to fetch data from regsiter table because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to fetch data from regsiter table with value : "+email+" and pass : "+password,e);
		} finally{
			DataBaseHelper.dbCleanUp(connection, pstmt);			
		}		
		return paasUser;
	}//end of method userWithEmailPasswordExist

	
}
