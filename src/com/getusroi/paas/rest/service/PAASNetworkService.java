package com.getusroi.paas.rest.service;

import java.io.IOException;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.paas.dao.DataBaseOperationFailedException;
import com.getusroi.paas.dao.NetworkDAO;
import com.getusroi.paas.helper.PAASConstant;
import com.getusroi.paas.helper.PAASGenericHelper;
import com.getusroi.paas.helper.ScriptService;
import com.getusroi.paas.rest.service.exception.PAASNetworkServiceException;
import com.getusroi.paas.sdn.service.SDNInterface;
import com.getusroi.paas.sdn.service.impl.SDNServiceImplException;
import com.getusroi.paas.sdn.service.impl.SDNServiceWrapperImpl;
import com.getusroi.paas.vo.ACL;
import com.getusroi.paas.vo.Subnet;
import com.getusroi.paas.vo.VPC;
import com.google.gson.Gson;


@Path("/networkservice")
public class PAASNetworkService {
	 static final Logger logger = LoggerFactory.getLogger(PAASNetworkService.class);

	 static final String TENANT="tenant";
	 @POST
	 @Path("/addVPC")
	 @Consumes(MediaType.APPLICATION_JSON)
	public void addVPC(String vpcData) throws DataBaseOperationFailedException, PAASNetworkServiceException{
		logger.debug(".addVPC method of PAASNetworkService");
		ObjectMapper mapper = new ObjectMapper();
		NetworkDAO networkDAO=new NetworkDAO();
		try {
			VPC vpc=mapper.readValue(vpcData,VPC.class);
			vpc.setVpcId(PAASGenericHelper.getCustomUUID(PAASConstant.VPC_PREFIX));
			networkDAO.registerVPC(vpc);
		} catch (IOException e) {
			logger.error("Error in reading data : "+vpcData+" using object mapper in addVPC");
			throw new PAASNetworkServiceException("Error in reading data : "+vpcData+" using object mapper in addVPC");
		}
		
	}//end of method 
	 
	 @GET
	 @Path("/getAllVPC")
	 @Produces(MediaType.APPLICATION_JSON)
	 public String getAllVPC() throws DataBaseOperationFailedException{
		 logger.debug(".getAllVPC method of PAASNetworkService");
		 NetworkDAO networkDAO=new NetworkDAO();
		 List<VPC> vpcList=networkDAO.getAllVPC();
		 Gson gson=new Gson();
		 String vpcInJsonString=gson.toJson(vpcList);
		 return vpcInJsonString;
	 }//end of method getAllVPC
	 
	 @GET
	 @Path("/getAllACL")
	 @Produces(MediaType.APPLICATION_JSON)
	 public String getAllACL() throws DataBaseOperationFailedException{
		 logger.debug(".getAllACL method of PAASNetworkService");
		 NetworkDAO networkDAO=new NetworkDAO();
		 List<ACL> aclList=networkDAO.getAllACL();
		 Gson gson=new Gson();
		 String aclInJsonString=gson.toJson(aclList);
		 return aclInJsonString;
	 }//end of method getAllACL
	 
	 @GET
	 @Path("/deleteVPCByName/{vpcName}")
	 @Produces(MediaType.TEXT_PLAIN)
	public String deleteVPCByName(@PathParam("vpcName") String vpcName) throws DataBaseOperationFailedException {
		 logger.debug(".deleteVPCByName method of PAASNetworkService");
		 NetworkDAO networkDAO=new NetworkDAO();
		 networkDAO.deleteVPCByName(vpcName);
		 return "vpc with name : "+vpcName+" is delete successfully";
	 }//end of method deleteVPCByName
	 
	 @POST
	 @Path("/updateVPC")
	 @Consumes(MediaType.APPLICATION_JSON)
	public void updateVPC(String vpcData) throws DataBaseOperationFailedException, PAASNetworkServiceException{
		logger.debug(".updateVPC method of PAASNetworkService");
		ObjectMapper mapper = new ObjectMapper();
		NetworkDAO networkDAO=new NetworkDAO();
		try {
			VPC vpc=mapper.readValue(vpcData,VPC.class);			
			networkDAO.updateVPCByNameAndVPCId(vpc);
		} catch (IOException e) {
			logger.error("Error in reading data : "+vpcData+" using object mapper in updateVPC");
			throw new PAASNetworkServiceException("Error in reading data : "+vpcData+" using object mapper in updateVPC");
		}		
	}//end of method updateVPC
	 
	 @POST
	 @Path("/addSubnet")
	 @Consumes(MediaType.APPLICATION_JSON)
	 public void addSubnet(String subnetData,@Context HttpServletRequest request) throws DataBaseOperationFailedException, SDNServiceImplException, PAASNetworkServiceException{
		 logger.debug(".updateVPC method of PAASNetworkService");
		 ObjectMapper mapper = new ObjectMapper();
		 NetworkDAO networkDAO=new NetworkDAO();
		 
		 try {
			 
			 //subnet pojo is chnaged 
			Subnet subnet=mapper.readValue(subnetData,Subnet.class);
			HttpSession session=request.getSession(false);
			int id=(int)session.getAttribute("id");
			 String subnetName= TENANT+id+"-"+subnet.getEnvironment();

			 subnet.setUser_id(id);
			String vpcid=networkDAO.getVPCIdByVPCNames(subnet.getVpc_name());
			if(vpcid !=null && !(vpcid.isEmpty()) && !(vpcid.equalsIgnoreCase(""))){
				subnet.setVpcId(vpcid);
				subnet.setSubnet_name(subnetName);
				subnet.setSubnetId(PAASGenericHelper.getCustomUUID(PAASConstant.SUBNET_PREFIX));
				networkDAO.addSubnet(subnet);
					new ScriptService().createSubnetNetwork(subnet.getCidr(),subnetName);
				
				//comment OpenDayLight Rest api
			/*	boolean createVBridge1=sdnService.createVirtualBridge(subnet.getVpc_name()+PAASConstant.VB_KEY, subnet.getVpc_name(),"controllerone");
				if(createVBridge1){
					boolean createInterface1=sdnService.createInterfaces(subnet.getVpc_name()+PAASConstant.INTERFACE1_KEY, subnet.getVpc_name(), subnet.getVpc_name()+PAASConstant.VB_KEY);
					boolean createInterface2=sdnService.createInterfaces(subnet.getVpc_name()+PAASConstant.INTERFACE2_KEY, subnet.getVpc_name(), subnet.getVpc_name()+PAASConstant.VB_KEY);

					if(createInterface1 && createInterface2){
						boolean createMapping1=sdnService.createMapping(subnet.getVpc_name()+PAASConstant.INTERFACE1_KEY,subnet.getVpc_name(), subnet.getVpc_name()+PAASConstant.VB_KEY, PAASConstant.BRIDGE1_KEY,"11");
						boolean createMapping2=sdnService.createMapping(subnet.getVpc_name()+PAASConstant.INTERFACE2_KEY,subnet.getVpc_name(), subnet.getVpc_name()+PAASConstant.VB_KEY, PAASConstant.BRIDGE2_KEY,"11");
						if(createMapping1 && createMapping2){
							sdnService.createFlowList(subnet.getVpc_name());
							sdnService.virtualBridgeInterfacesFlowFilter(subnet.getVpc_name(),subnet.getVpc_name()+PAASConstant.VB_KEY, subnet.getVpc_name()+PAASConstant.INTERFACE1_KEY);
						}//end of if(createMapping1 && createMapping2)
					}//end of if(createInterface1 && createInterface2)
				}//end of if(createVBridge1)
*/			}else{
				//throw exception
				throw new PAASNetworkServiceException("no VPC exist with the vpc name "+subnet.getVpc_name());
			}
		} catch (IOException |InterruptedException e) {
			logger.debug("Error in reading data : "+subnetData+" using object mapper in addSubnet");
			throw new PAASNetworkServiceException("Error in reading data : "+subnetData+" using object mapper in addSubnet");
		} 
		

	 }//end of method addSubnet
	 
	 @GET
	 @Path("/getAllSubnet")
	 @Produces(MediaType.APPLICATION_JSON)
	 public String getAllSubnet(@Context HttpServletRequest request) throws DataBaseOperationFailedException{
		 
		 logger.debug(".getAllSubnet method of PAASNetworkService");
		 
		 HttpSession session=request.getSession(false);
			int id=(int)session.getAttribute("id");
		
		 NetworkDAO networkDAO=new NetworkDAO();
		 List<Subnet> subnetList=networkDAO.getAllSubnetByUserId(id);
		 Gson gson=new Gson();
		 String subnetInJsonString=gson.toJson(subnetList);
		 return subnetInJsonString;
	 }//end of method getAllSubnet
	 
	 
	 @GET
	 @Path("/deleteSubnetByName/{subnetName}")
	 @Produces(MediaType.TEXT_PLAIN)
	public String deleteSubnetByName(@PathParam("subnetName") String subnetName) throws DataBaseOperationFailedException {
		 logger.debug(".deleteVPCByName method of PAASNetworkService");
		 NetworkDAO networkDAO=new NetworkDAO();
		 networkDAO.deleteSubnetBySubnetName(subnetName);
		 return "subnet with name : "+subnetName+" is delete successfully";
	 }//end of method deleteSubnetByName
	 
	 @POST
	 @Path("/updateSubnet")
	 @Consumes(MediaType.APPLICATION_JSON)
	public void updateSubnet(String subnetData) throws DataBaseOperationFailedException, PAASNetworkServiceException{
		logger.debug(".updateSubnet method of PAASNetworkService");
		ObjectMapper mapper = new ObjectMapper();
		NetworkDAO networkDAO=new NetworkDAO();
		try {
			Subnet subnet=mapper.readValue(subnetData,Subnet.class);			
			networkDAO.updateSubnetBySubnetIDAndSubnetName(subnet);
		} catch (IOException e) {
			logger.error("Error in reading data : "+subnetData+" using object mapper in updateSubnet");
			throw new PAASNetworkServiceException("Error in reading data : "+subnetData+" using object mapper in updateSubnet");
		}		
	}//end of method updateSubnet
	 
	 @POST
	 @Path("/addACLRule")
	 @Consumes(MediaType.APPLICATION_JSON)
	 public void addACLRule(String aclData) throws SDNServiceImplException, DataBaseOperationFailedException, PAASNetworkServiceException{
		 logger.debug(".addACLRule method of PAASNetworkService");
		 ObjectMapper mapper = new ObjectMapper();
		 NetworkDAO networkDAO=new NetworkDAO();
		 SDNInterface sdnService=new SDNServiceWrapperImpl();
		 boolean flowFlag=false;
		 try {
			ACL acl=mapper.readValue(aclData, ACL.class);
			if(acl.getAction().equalsIgnoreCase("pass")){
			flowFlag=sdnService.installFlow(acl.getAclName(), acl.getSrcIp(), acl.getDestIP(),PAASConstant.ACL_PASS_ACTION_KEY);
			}else{
				flowFlag=sdnService.installFlow(acl.getAclName(), acl.getSrcIp(), acl.getDestIP(),PAASConstant.ACL_OTHER_ACTION_KEY);
			}			
			if(flowFlag){
				networkDAO.insertACL(acl);
			}
		} catch (IOException e) {
			logger.error("Error in reading data : "+aclData+" using object mapper in addACLRule");
			throw new PAASNetworkServiceException("Error in reading data : "+aclData+" using object mapper in addACLRule");
		}
	 }//end of method addACLRule
	 
	 @GET
	 @Path("/getAllACLNames")
	 @Produces(MediaType.APPLICATION_JSON)
	 public String getAllACLNames() throws DataBaseOperationFailedException{
		 logger.debug(".getAllACL method of PAASNetworkService");
		 NetworkDAO networkDAO=new NetworkDAO();
		 List<String> aclList=networkDAO.getAllACLNames();
		 Gson gson=new Gson();
		 String aclInJsonString=gson.toJson(aclList);
		 return aclInJsonString;
	 }//end of method getAllACLNames
	 
}
