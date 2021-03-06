package com.ngdata.actionstore;

/**
 * <ul>
 * <li>Title:MessageStoreService</li>
 * <li>Description: REST api for storing parameter map and retrieving the parameter map.</li>
 * <li>Created: Oct 12, 2017 by: Anand N</li>
 * <li>Edited By Anand N.</li>
 * </ul>
 */
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import redis.clients.jedis.Jedis;

@Path("/sc")
public class MessageStoreService {

    /**
     * The LOGGER.
     */
    private final static Logger LOGGER = Logger.getLogger(MessageStoreService.class.getName());
	
    /**
     * The httpHeaders.
     */
    @Context
    private HttpHeaders httpHeaders;

    /**
     * The uriInfo.
     */
    @Context
    private UriInfo uriInfo;

    /**
     * The servletContext.
     */
    @Context
    private ServletContext servletContext;

	/**
	 * 
	 */
    private Gson gson = new GsonBuilder().create();
	
	/**
	 * 
	 */
	private RedisConnectionHelper redisConnectionHelper = null;
	
    /**
     * @throws Exception
     */
    @PostConstruct
    public void initialize() throws Exception {
        initialize(httpHeaders, uriInfo, servletContext);
    }

    public void initialize(HttpHeaders httpHeaders, UriInfo uriInfo, ServletContext servletContext) {
    	ApplicationProperties.initialize();
        redisConnectionHelper = new RedisConnectionHelper();
    }
	
	
	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {
		return Response.status(200).entity("SUCCEEDED").build();
	}
	
	@PUT
	@Path("/message")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public String storeMessage(String parametersMapJSON) {

		//store the parametersMap in Redis
		HashMap<String, String> parametersMap = new HashMap<String, String>();
        LOGGER.info("parametersMapJSON = " + parametersMapJSON);
		Jedis jedis = redisConnectionHelper.getJedisConnection();
		Long shortId = ParamMapSaver.instance.saveParamMap(parametersMapJSON, jedis);
        
		
        HashMap<String, String> retValue = new HashMap<String, String>();
        //retValue.put("SHORTID", shortId.toString());
        retValue.put("SHORTID", Long.toHexString(shortId));
        retValue.put("CODE", "200");
        retValue.put("MESSAGE", "Parameters map stored successfully into Redis DB!");
        LOGGER.info("retValue = " + retValue);
        return gson.toJson(retValue);
	}
	
	@GET
	@Path("/message/{shortid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public String getMessage(@PathParam("shortid") String shortId) {

        LOGGER.info("shortId = " + shortId);
		//get the parametersMap from Redis
		RedisConnectionHelper redisConnectionHelper = new RedisConnectionHelper();
		redisConnectionHelper.getJedisConnection();
		Gson gson = new GsonBuilder().create();
				
        String parametersMapJSON = null;
		Jedis jedis = null;
        try {
            jedis = new RedisConnectionHelper().getJedisConnection();
            parametersMapJSON = jedis.get(shortId);
            
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        
        LOGGER.info("parametersMapJSON = " + parametersMapJSON);
        return gson.toJson(parametersMapJSON);
	}	
}


