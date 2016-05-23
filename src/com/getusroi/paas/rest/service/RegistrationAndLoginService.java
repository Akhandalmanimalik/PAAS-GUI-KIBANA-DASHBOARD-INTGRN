package com.getusroi.paas.rest.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.dao.DataBaseOperationFailedException;
import com.getusroi.paas.dao.PaasUserRegisterAndLoginDAO;
import com.getusroi.paas.rest.service.exception.UserRegisterAndLoginServiceException;
import com.getusroi.paas.security.MD5PasswordEncryption;
import com.getusroi.paas.vo.PaasUserRegister;


@Path("/registerAndLoginService")
public class RegistrationAndLoginService {
	 static final Logger logger = LoggerFactory.getLogger(RegistrationAndLoginService.class);

	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	public void registerUser(String registrationData) throws DataBaseOperationFailedException, UserRegisterAndLoginServiceException{
		logger.debug(".registerUser method of RegistrationAndLoginService");
		PaasUserRegisterAndLoginDAO registerDAO=new PaasUserRegisterAndLoginDAO();
		ObjectMapper mapper = new ObjectMapper();
		PaasUserRegister paasUserRegister=null;
		try {
			if(registrationData !=null && !(registrationData.isEmpty()) && !(registrationData.equalsIgnoreCase(""))){
			paasUserRegister = mapper.readValue(registrationData,PaasUserRegister.class);
			//#TODO added new code to check user Exist with enter email
				boolean userExist=	registerDAO.checkEmailExist(paasUserRegister.getEmail());
				if(userExist)
					throw new UserRegisterAndLoginServiceException("User alredy exist with enter email :  "+paasUserRegister.getEmail());
			
			registerDAO.registerPaasUser(paasUserRegister);
			}else{
				//throw exception
				throw new UserRegisterAndLoginServiceException("registration data is not valid :  "+registrationData);
			}
		} catch (IOException e) {
			logger.error("Error in reading value from : "+registrationData+" using Object mapper in registerUser ");
			throw new UserRegisterAndLoginServiceException("Error in reading value from : "+registrationData+" using Object mapper in registerUser ");
		}
		
	}//end of method registerUser
	
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String checkForUniqueUser(String loginData,@Context HttpServletRequest req) throws DataBaseOperationFailedException, UserRegisterAndLoginServiceException {
		logger.debug(".checkForUniqueUser method of RegistrationAndLoginService");
		
		PaasUserRegister paasUserRegister=null;
		try {
			if (loginData != null && !(loginData.isEmpty())
					&& !(loginData.equalsIgnoreCase(""))) {

				JSONObject jsonObject = new JSONObject(loginData);

				PaasUserRegisterAndLoginDAO checkUniqueUser = new PaasUserRegisterAndLoginDAO();
				String mD5ncryptedPassword = new MD5PasswordEncryption().getMD5EncryptedPassword(jsonObject.getString("password"));
				paasUserRegister = checkUniqueUser.userWithEmailPasswordExist(
						jsonObject.getString("email"),
						mD5ncryptedPassword);

				if (paasUserRegister != null) {
					HttpSession session = req.getSession(true);
					session.setAttribute("id", paasUserRegister.getId());

					logger.debug("Login sucess full with Email ID: "
							+ paasUserRegister.getEmail());
					return paasUserRegister.getEmail();
				} else {
					return "failed";
				}

			}
		} catch ( JSONException e) {
				e.printStackTrace();
				logger.error("Error in reading value from : "+loginData+" using Object mapper in registerUser ");
				throw new UserRegisterAndLoginServiceException("Error in reading value from : "+loginData+" using Object mapper in loginuser ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return "Sucess";
	}//end of method checkForUniqueUser
	
	@GET
	@Path("{Param1}")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkEmailExist(@PathParam("Param1") String username) throws DataBaseOperationFailedException {
		logger.debug(".checkForUniqueUser method of RegistrationAndLoginService");
		PaasUserRegisterAndLoginDAO checkEmailExist=new PaasUserRegisterAndLoginDAO();		
		boolean uniqueFlag=checkEmailExist.checkEmailExist(username);
		if(uniqueFlag==true){
			return "email already exist : "+username;
		}else{
		return username;
		}
	}//end of method checkEmailExist
	@GET
	@Path("logout")
	@Produces(MediaType.TEXT_PLAIN)
	public void logout(@Context HttpServletRequest request) throws DataBaseOperationFailedException {
		logger.debug(".logout method of RegistrationAndLoginService");
		
		HttpSession session=request.getSession(false);
		session.invalidate();
		
		
		
	}
	
}
