package com.getusroi.paas.rest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.dao.ApplicationDAO;
import com.getusroi.paas.dao.DataBaseOperationFailedException;
import com.getusroi.paas.dao.ImageRegistryDAO;
import com.getusroi.paas.helper.PAASConstant;
import com.getusroi.paas.helper.ScriptService;
import com.getusroi.paas.marathon.service.IMarathonService;
import com.getusroi.paas.marathon.service.MarathonServiceException;
import com.getusroi.paas.marathon.service.impl.MarathonService;
import com.getusroi.paas.rest.service.exception.ApplicationServiceException;
import com.getusroi.paas.vo.AddService;
import com.getusroi.paas.vo.ApplicantSummary;
import com.getusroi.paas.vo.ImageRegistry;
import com.getusroi.paas.vo.MessosTaskInfo;
import com.google.gson.Gson;



@Path("/applicationService")
public class ApplicationService {
	 static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
	 static final String TENANT="tenant";

	@POST
	@Path("/addApplicantSummary")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addApplicationSummary(String appSummaryData) throws ApplicationServiceException, DataBaseOperationFailedException{
		logger.debug(".addApplicationSummary method of ApplicationService ");
		ObjectMapper mapper = new ObjectMapper();
		ApplicationDAO applicationDAO=new ApplicationDAO();
		try {
			ApplicantSummary applicantSummary=mapper.readValue(appSummaryData,ApplicantSummary.class);
			applicationDAO.insertApplicationSummary(applicantSummary);
		} catch (IOException e) {
			logger.error("Error in reading data : "+appSummaryData+" using object mapper in addApplicationSummary");
			throw new ApplicationServiceException("Error in reading data : "+appSummaryData+" using object mapper in addApplicationSummary");
		}
	}//end of addApplicationSummary
	
	@POST
	@Path("/getApplicationSummary")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApplicationSummary(String repositoryName) throws DataBaseOperationFailedException, ApplicationServiceException{
		logger.debug(".getApplicationSummary method of ApplicationService ");
		JSONObject jsonObject =new JSONObject(repositoryName);
		ImageRegistryDAO imageRegistryDAO=new ImageRegistryDAO();
		ImageRegistry imageRegistry=imageRegistryDAO.getImageRegistryByName(jsonObject.getString("imageRegistry"));
		String response=null;
		if(imageRegistry != null){
			String baseURL = PAASConstant.HTTPS_PROTOCOL_KEY+imageRegistry.getLocation() +PAASConstant.ALL_REPOSTORY_KEY+imageRegistry.getName()+PAASConstant.ALL_TAGS_KEY;
			String authentication=imageRegistry.getUser_name() + ":" + imageRegistry.getPassword();
			try {
				 response=getHttpResponse(baseURL, authentication, "GET");

				logger.debug("http response  : "+response);				
			} catch (IOException e) {
				logger.error("Unable to get the http response using base url :"+baseURL+", authentication : "+authentication);
				throw new ApplicationServiceException("Unable to get the http response using base url :"+baseURL+", authentication : "+authentication);
			}
		}else{
			logger.debug("No image repository availabel with name : "+repositoryName);
		}		
		return response;
	}//end of method getApplicationSummary
	
	@POST
	@Path("/addService")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addService(String applicationServiceData,@Context HttpServletRequest request  ) throws DataBaseOperationFailedException, MarathonServiceException, InterruptedException, ApplicationServiceException{
		logger.debug(".addService method of ApplicationService ");
		ObjectMapper mapper = new ObjectMapper();
		ApplicationDAO applicationDAO=new ApplicationDAO();
		IMarathonService marathonService=new MarathonService();
		try {
			
			HttpSession session=request.getSession(false);
			int userId=(int)session.getAttribute("id");
			
			AddService addService=mapper.readValue(applicationServiceData,AddService.class);
			addService.setUserId(userId);
			applicationDAO.addService(addService);			
			//create instance in marathon using service object
		String appID=	marathonService.postRequestToMarathon(addService);
		
		logger.debug("----------Before  ContianerScript  script  called------------------------");			
			Thread.sleep(60000);
		List<MessosTaskInfo>  listOfMessosTask=	 ScriptService.runSCriptGetMessosTaskId(appID);
		if(listOfMessosTask.isEmpty()){
			Thread.sleep(60000);
			listOfMessosTask=ScriptService.runSCriptGetMessosTaskId(appID);
			
		}
		for (Iterator iterator = listOfMessosTask.iterator(); iterator
				.hasNext();) {
			MessosTaskInfo messosTaskInfo = (MessosTaskInfo) iterator.next();
			new ScriptService().updateSubnetNetworkInMessos(messosTaskInfo, addService.getSubnet_name());
		}
			logger.debug("----------Network  script  called------------------------");
		} catch (IOException e) {
			logger.error("Error in reading data "+applicationServiceData+" using object mapper in addService");
			throw new ApplicationServiceException("Error in reading data "+applicationServiceData+" using object mapper in addService");
		}
	}//end of method addService
	
	@GET
	@Path("/getAllApplicationService")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllApplicationService(@Context HttpServletRequest request) throws DataBaseOperationFailedException{
		logger.debug(".getAllApplicationService method of ApplicationService ");
		
		HttpSession session=request.getSession(false);
		int userId=(int)session.getAttribute("id");
	
		ApplicationDAO applicationDAO=new ApplicationDAO();
		List<AddService> addServiceList=applicationDAO.getAllServiceByUserId(userId);
		Gson gson = new Gson();
		String addServiceInJsonString=gson.toJson(addServiceList);
		return addServiceInJsonString;
		
	}//end of method getAllApplicationService
	
	
	@GET
	@Path("/getAllApplicationSummary")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllApplicationSummary() throws DataBaseOperationFailedException{
		logger.debug(".getAllApplicationSummary method of ApplicationService ");
		ApplicationDAO applicationDAO=new ApplicationDAO();
		List<ApplicantSummary> applicantSummaryList=applicationDAO.getAllApplicantSummary();
		Gson gson = new Gson();
		String applicantSummaryInJsonString=gson.toJson(applicantSummaryList);
		return applicantSummaryInJsonString;
	}//end of method getAllApplicationSummary
	
	@GET
	@Path("/deleteServiceByName/{serviceName}/{envirnoment}")
	public void deleteServiceByName(@PathParam("serviceName") String serviceName,@PathParam("envirnoment") String envirnoment,@Context HttpServletRequest request) throws DataBaseOperationFailedException, MarathonServiceException{
		logger.debug(".deleteServiceByName method of ApplicationService ");
		logger.debug("ServiceNAme : "+serviceName  +" environement : "+envirnoment);
		ApplicationDAO applicationDAO=new ApplicationDAO();
		HttpSession session=request.getSession(false);
		int user_id=(int)session.getAttribute("id");
		String appid=TENANT+user_id+"-"+envirnoment;
		//new MarathonService().deletInstanceformMarathan(appid);
		
		applicationDAO.deleteServiceByServiceName(serviceName,user_id);
	}//end of method deleteServiceByName
	
	@PUT
	@Path("/updateMarathonInstace")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateMarathonInstace(String data) throws MarathonServiceException{
		logger.debug(".updateMarathonInstace method of ApplicationService ");
		IMarathonService marathonService=new MarathonService();
		marathonService.updateMarathonInsance(data);
		return data;
	}//end of method updateMarathonInstace
	
	
	/**
	 * This method is used to get response from http client
	 * @param baseURL : base url in String
	 * @param authentication : authentication in String
	 * @param httpRequestMethod : http request method
	 * @return String : response data in String
	 * @throws IOException : Unable to connect to url using http client
	 */
	private String getHttpResponse(String baseURL,String authentication,String httpRequestMethod) throws IOException{
		logger.debug(".getHttpResponse method of ApplicationService");	
		StringBuffer result = new StringBuffer();
		URL url = new URL(baseURL);       
		//String authStr = summary.getUser_name() + ":" + summary.getPassword();
        String encodedAuthStr = Base64.encodeBase64String(authentication.getBytes());
		// Create Http connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if(connection!=null){
        // Set connection properties
        connection.setRequestMethod(httpRequestMethod);
        connection.setRequestProperty("Authorization", "Basic "
                + encodedAuthStr);
        connection.setRequestProperty("Accept", "application/json");        
        logger.debug("Response  Code"+connection.getResponseCode());        
        InputStream content = (InputStream) connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                content));
        String line = "";
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        }
        return result.toString();
	}//end of method getHttpResponse
	
	

}
