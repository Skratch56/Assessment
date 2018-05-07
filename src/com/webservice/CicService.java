package com.webservice;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.entity.CIC;
import com.qualifier.Resource;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Path("/cicservice")
public class CicService {

	@Inject
	private EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("status")
	public Response getStatus() {
		return Response.ok(
				"{\"status\":\"Service CIC Service is running...\"}").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cic")
	public Response getCICs
	() {
		String response = null;
		try {

			em = Resource.getEntityManager();
			Query query = em.createQuery("FROM com.entity.CIC");
			List<CIC> list = query.getResultList();
			em.close();
			response = toJSONString(list);
		} catch (Exception err) {
			response = "{\"status\":\"401\","
					+ "\"message\":\"No content found \""
					+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
			return Response.status(401).entity(response).build();
		}
		return Response.ok(response).build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cic/{cicid}")
	public Response getCIC(@PathParam("cicid") String cicID){
		String response = null;
		try {
			em = Resource.getEntityManager();
			CIC existingCIC = em.find(CIC.class, cicID);
			if(null ==existingCIC){
				response = "{\"status\":\"401\","
						+ "\"message\":\"No content found \""
						+ "\"developerMessage\":\"Body - "+ cicID +" Not Found in Library" + "}";
				return Response.status(401).entity(response).build();
			}
			em.close();
			response = toJSONString(existingCIC);
		} catch (Exception err) {
			response = "{\"status\":\"401\","
					+ "\"message\":\"No content found \""
					+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
			return Response.status(401).entity(response).build();
		}
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
		
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("CIC")
	public Response createNewBook(String payload){
	

		 
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Gson gson = gsonBuilder.create();

		
		CIC cic = gson.fromJson(payload, CIC.class);
		String returnCode = "200";
		em = Resource.getEntityManager();

		
		try {
			em.getTransaction().begin();
			em.persist(cic);
			em.flush();
			em.refresh(cic);
			em.getTransaction().commit();
			em.close();
			returnCode = "{"
					+ "\"href\":\"http:localhost:8080/CICWebService/rest/cicservice/cic/"+cic.getcic()+"\","
					+ "\"message\":\"New CIC successfully created.\""
					+ "}";
		} catch (Exception err) {
			err.printStackTrace();
			returnCode = "{\"status\":\"500\","+
					"\"message\":\"Resource not created.\""+
					"\"developerMessage\":\""+err.getMessage()+"\""+
					"}";
			return  Response.status(500).entity(returnCode).build(); 

		}
		return  Response.status(201).entity(returnCode).build(); 
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("CIC/{name}")
	public Response updateBook(@PathParam("name") String cicid,
			String payload) {

		System.out.println("payload - " + payload);

		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		Gson gson = gsonBuilder.create();

		
		
		CIC cic = gson.fromJson(payload, CIC.class);
		String returnCode = "200";

		System.out.println("ID - " + cic.getcic());
		System.out.println("Type - " + cic.getcictype());
		System.out.println("Subject - " + cic.getsubject());
		System.out.println("Body - " + cic.getBody());
		System.out.println("SourceSytem - " + cic.getSourcesystem());
		System.out.println("TimeStamp - " + cic.getcicTimeStamp());

		em = Resource.getEntityManager();


		try {
			em.getTransaction().begin();
			CIC existingcic = em.find(CIC.class, cicid);
			System.out
					.println("Existing CIC ID - " + existingcic.getcic());
			existingcic.setcictype(existingcic.getcictype());
			existingcic.setsubject(existingcic.getsubject());
			existingcic.setbody(existingcic.getBody());
			existingcic.setSourcesystem(existingcic.getSourcesystem());
			existingcic.setcicTimeStamp(existingcic.getcicTimeStamp());
			em.merge(existingcic);
			em.getTransaction().commit();
			em.close();
			returnCode = "{"
					+ "\"href\":\"http:localhost:8080/CICWebService/rest/CICservice/CIC/"+cic.getcic()+"\","
					+ "\"message\":\""+cic.getBody()+" was successfully updated.\""
					+ "}";
		} catch (Exception err) {
			err.printStackTrace();
			returnCode = "{\"status\":\"304\","+
					"\"message\":\"Resource not modified.\""+
					"\"developerMessage\":\""+err.getMessage()+"\""+
					"}";
			return  Response.status(304).entity(returnCode).build(); 
		}
		return  Response.status(200).entity(returnCode).build(); 
	}

//	@DELETE
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("cic/{cicid}")
//	public Response deleteBook(@PathParam("cicid") String cicid) {
//		em = Resource.getEntityManager();
//		String returnCode = "";
//		 
//		try {
//			em.getTransaction().begin();
//			CIC existingcic = em.find(CIC.class, cicid);
//			em.remove(existingcic);
//			em.getTransaction().commit();
//			em.close();
//			returnCode = "{"
//					+ "\"message\":\"Email succesfully deleted\""
//					+ "}";
//		} catch (Exception err) {
//			err.printStackTrace();
//			returnCode = "{\"status\":\"500\","+
//					"\"message\":\"Resource not deleted.\""+
//					"\"developerMessage\":\""+err.getMessage()+"\""+
//					"}";
//			return  Response.status(500).entity(returnCode).build(); 
//		}
//		return Response.ok(returnCode,MediaType.APPLICATION_JSON).build();
//	}

	public String toJSONString(Object object) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
	}
}
