package com.getusroi.paas.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.db.helper.DataBaseConnectionFactory;
import com.getusroi.paas.db.helper.DataBaseHelper;
import com.getusroi.paas.helper.PAASConstant;
import com.getusroi.paas.helper.PAASErrorCodeExceptionHelper;
import com.getusroi.paas.vo.ImageRegistry;
import com.mysql.jdbc.PreparedStatement;
import static com.getusroi.paas.helper.PAASConstant.*;

/**
 * This class  is used to get,add,delete and update image registry
 * @author bizruntime
 *
 */
public class ImageRegistryDAO {
	 static final Logger logger = LoggerFactory.getLogger(ImageRegistryDAO.class);
	 private final String INSERT_IMAGEREGISTRY_QUERY="insert into image_registry VALUES(?,?,?,?,?,?)";
	 private final String GET_ALL_IMAGEREGISTRY_QUERY="select * from image_registry";
	 private final String GET_IMAGE_REGISTRY_BY_NAME="select * from image_registry where name=?";
	 private final String DELETE_IMAGEREGISTRY_BY_IMAGENAME_AND_USERNAME_QUERY="delete from image_registry where name=? AND user_name=?";
	 /**
	  * This method is used to store imageRegistry in db
	  * @param imageRegistryVO : ImageRegistryVO 
	  * @throws DataBaseOperationFailedException : Unable to store image regsitry in db
	  */
	public void addImageRegistry(ImageRegistry imageRegistryVO) throws DataBaseOperationFailedException{
		logger.debug(".addImageRegistry method of ImageRegistryDAO");
		DataBaseConnectionFactory connectionFactory=new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt=null;
		 try {
			connection=connectionFactory.getConnection(MYSQL_DB);
			pstmt = (PreparedStatement) connection
					.prepareStatement(INSERT_IMAGEREGISTRY_QUERY);
			pstmt.setString(1, imageRegistryVO.getName());
			pstmt.setString(2, imageRegistryVO.getLocation());
			pstmt.setString(3, imageRegistryVO.getVersion());
			pstmt.setString(4, imageRegistryVO.getPrivate_cloud());
			pstmt.setString(5, imageRegistryVO.getUser_name());
			pstmt.setString(6, imageRegistryVO.getPassword());
			pstmt.executeUpdate();
			logger.debug("image registry data inserted successfully : "+imageRegistryVO);
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to store the image registry in db : "+imageRegistryVO.toString());
			throw new DataBaseOperationFailedException("Unable to store the image registry in db : "+imageRegistryVO.toString(),e);
		}catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to store the image registry in db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to store the image registry in db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to store the image registry in db : "+imageRegistryVO.toString(),e);
		} finally{
			DataBaseHelper.close(pstmt);
			DataBaseHelper.close(connection);
		}
	}//end of method addImageRegistry
	
	/**
	 * This method is used to get all image registry from db
	 * @return List<ImageRegistryVO> : List of all image registry from db
	 * @throws DataBaseOperationFailedException : unable to fetch image regsitry from db
	 */
	public List<ImageRegistry> getAllImageRegistry() throws DataBaseOperationFailedException{
		logger.debug(".getAllImageRegistry method of ImageRegistryDAO");
		Connection connection=null;
		Statement stmt=null;
		ResultSet result=null;
		List<ImageRegistry> imageRegistryList=new ArrayList<>();
		DataBaseConnectionFactory connectionFactory=new DataBaseConnectionFactory();
		try {
			connection=connectionFactory.getConnection(MYSQL_DB);
			stmt=connection.createStatement();
			result=stmt.executeQuery(GET_ALL_IMAGEREGISTRY_QUERY);
			if(result !=null){
				while(result.next()){
					String name=result.getString("name");
					String location=result.getString("location");
					String version=result.getString("version");
					String private_cloud=result.getString("private_cloud");
					String user_name=result.getString("user_name");
					String password=result.getString("password");
					logger.debug("name : "+name+", location : "+location+", version : "+version+", private cloud : "+private_cloud+", user name : "+user_name+", password : "+password);
					ImageRegistry imageRegistry=new ImageRegistry(name, location, version, private_cloud, user_name, password);
					imageRegistryList.add(imageRegistry);
				}//end of while
				logger.debug("element in image registry list are : "+imageRegistryList);
			}else{
				logger.debug("No data available in image_registry table");
			}
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to fetch the data from image_registry");
			throw new DataBaseOperationFailedException("Error in getting all data from image_registry ",e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to fetch the data from image_registry because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to fetch the data from image_registry db because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Error in getting all data from image_registry ",e);
		} finally{
			DataBaseHelper.dbCleanup(connection, stmt, result);
		}
		return imageRegistryList;
	}//end of method getAllImageRegistry
	
	
	/**
	 * This method is used to get  image registry from db by name
	 * @param imageRegistryName : image registry name in String
	 * @return ImageRegistry : image registry from db based on name
	 * @throws DataBaseOperationFailedException : unable to fetch image regsitry from db
	 */
	public ImageRegistry getImageRegistryByName(String imageRegistryName) throws DataBaseOperationFailedException{
		logger.debug(".getAllImageRegistry method of ImageRegistryDAO");
		
		Connection connection=null;
		java.sql.PreparedStatement pstmt=null;
		ResultSet result=null;
		ImageRegistry imageRegistry=null;
		DataBaseConnectionFactory connectionFactory=new DataBaseConnectionFactory();
		try {
			connection=connectionFactory.getConnection(MYSQL_DB);
			pstmt=connection.prepareStatement(GET_IMAGE_REGISTRY_BY_NAME);
			pstmt.setString(1,imageRegistryName);			
			result=pstmt.executeQuery();
			if(result !=null){
				while(result.next()){
					String name=result.getString("name");
					String location=result.getString("location");
					String version=result.getString("version");
					String private_cloud=result.getString("private_cloud");
					String user_name=result.getString("user_name");
					String password=result.getString("password");
					logger.debug("name : "+name+", location : "+location+", version : "+version+", private cloud : "+private_cloud+", user name : "+user_name+", password : "+password);
					 imageRegistry=new ImageRegistry(name, location, version, private_cloud, user_name, password);
					
				}//end of while
				logger.debug("element in image registry list are : "+imageRegistry);
			}else{
				logger.debug("No data available in image_registry table");
			}
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to fetch the data from image_registry");
			throw new DataBaseOperationFailedException("Error in getting all data from image_registry ",e);
		}catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Error in getting all data from image_registry: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Error in getting all data from image_registry because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Error in getting all data from image_registry ",e);
		} finally{
			DataBaseHelper.dbCleanup(connection, pstmt, result);
		}
		return imageRegistry;
	}//end of method getAllImageRegistry
	
	/**
	 * This method is used to delete image registry based on image name ans user_name
	 * @param imageName  : name of image in String
	 * @param userName : name of user in String
	 * @throws DataBaseOperationFailedException 
	 */
	public void deleteImageRegistryByNameAndUserName(String imageName,String userName) throws DataBaseOperationFailedException{
		logger.debug(".deleteImageRegistryByNameAndUserName method of ImageRegistryDAO");
		DataBaseConnectionFactory connectionFactory=new DataBaseConnectionFactory();
		Connection connection=null;
		PreparedStatement pstmt=null;
		try {
			connection=connectionFactory.getConnection(MYSQL_DB);
			pstmt=(PreparedStatement) connection.prepareStatement(DELETE_IMAGEREGISTRY_BY_IMAGENAME_AND_USERNAME_QUERY);
			pstmt.setString(1,imageName);
			pstmt.setString(2,userName);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Unable to delete image registry from db using image name : "+imageName+" and user name : "+userName);
			throw new DataBaseOperationFailedException("Unable to delete image registry from db using image name : "+imageName+" and user name : "+userName,e);
		} catch(SQLException e) {
			if(e.getErrorCode() == 1064) {
				String message = "Unable to delete image registry from db using image name because " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.ERROR_IN_SQL_SYNTAX);
				throw new DataBaseOperationFailedException(message, e);
			} else if(e.getErrorCode() == 1146) {
				String message = "Unable to delete image registry from db using image name because: " + PAASErrorCodeExceptionHelper.exceptionFormat(PAASConstant.TABLE_NOT_EXIST);
				throw new DataBaseOperationFailedException(message, e);
			} else
				throw new DataBaseOperationFailedException("Unable to delete image registry from db using image name : "+imageName+" and user name : "+userName,e);
		} finally{
			DataBaseHelper.dbCleanUp(connection, pstmt);			
		}
	}//end of method

}
