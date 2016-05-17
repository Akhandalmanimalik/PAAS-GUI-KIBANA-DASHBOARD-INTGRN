package com.getusroi.paas.rest.service;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.dao.DataBaseOperationFailedException;
import com.getusroi.paas.dao.ImageRegistryDAO;
import com.getusroi.paas.rest.service.exception.ImageRegistryServiceException;
import com.getusroi.paas.sdn.service.SDNInterface;
import com.getusroi.paas.sdn.service.impl.SDNServiceImplException;
import com.getusroi.paas.sdn.service.impl.SDNServiceWrapperImpl;
import com.getusroi.paas.vo.ImageRegistry;
import com.google.gson.Gson;


@Path("/imageRegistry")
public class ImageRegistryService {
	 static final Logger logger = LoggerFactory.getLogger(ImageRegistryService.class);

	
	@POST
	@Path("/addImageRegistry")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addImageRegistry(String imageRegistryData) throws DataBaseOperationFailedException, SDNServiceImplException, ImageRegistryServiceException{
		logger.debug(".addImageRegistry method of ImageRegistryService");
		ImageRegistryDAO imageRegistryDAO=new ImageRegistryDAO();
		ObjectMapper mapper = new ObjectMapper();
		SDNInterface sdnService=new SDNServiceWrapperImpl();
		String responseMessage=null;
		try {
			ImageRegistry imageRegistry=mapper.readValue(imageRegistryData, ImageRegistry.class);
			imageRegistryDAO.addImageRegistry(imageRegistry);
			String username = imageRegistry.getUser_name();
			String pass = imageRegistry.getPassword();
			String url = imageRegistry.getLocation();
			logger.debug("username : "+username+ " pass: "+pass+ "url : "+url);			
			boolean response=sdnService.getUserDetailsRegistry(imageRegistry);
			if(response)
				responseMessage= "add Image Registry is successful in sdn";
			else
				responseMessage="Unable to add Image Registry in sdn";
		} catch (IOException e) {
			logger.error("Error in reading value from image registry  : "+imageRegistryData+" using object mapper in addImageRegistry");
			throw new ImageRegistryServiceException("Error in reading value from image registry  : "+imageRegistryData+" using object mapper in addImageRegistry");
		}
		return responseMessage;
	}//end of method addImageRegistry

	@GET
	@Path("/getAllImageRegistry")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllImageRegistry() throws DataBaseOperationFailedException{
		logger.debug(".selectImageRegistry method of ImageRegistryService");
		ImageRegistryDAO imageRegistryDAO=new ImageRegistryDAO();
		List<ImageRegistry> imageRegistryList=imageRegistryDAO.getAllImageRegistry();
		Gson gson = new Gson();
		String imageRegistryListInJSON=gson.toJson(imageRegistryList);
		return imageRegistryListInJSON;
	}//end of method getAllImageRegistry
	
	@GET
	@Path("/deleteImageRegistry/{imageName}/{userName}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteImageRegistry(@PathParam("imageName") String imageName,@PathParam("userName") String userName) throws DataBaseOperationFailedException{
		logger.debug(".deleteImageRegistry method of ImageRegistryService");
		ImageRegistryDAO imageRegistryDAO=new ImageRegistryDAO();
		imageRegistryDAO.deleteImageRegistryByNameAndUserName(imageName, userName);	
		return "delete successful for image registry with image name : "+imageName+" and user name : "+userName;
	}//end of method deleteImageRegistry
}
