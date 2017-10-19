package com.ngdata.actionstore;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import redis.clients.jedis.Jedis;

public class ParamMapSaver {
	
    /**
     * The LOGGER.
     */
    private final static Logger LOGGER = Logger.getLogger(ParamMapSaver.class.getName());
	
	/**
	 * 
	 */
	private Long CURRENT_ID = 100l;
	
	/**
	 * 
	 */
	public static ParamMapSaver instance = new ParamMapSaver();
	
	/**
	 * 
	 */
    private Gson gson = new GsonBuilder().create();
	
	
	/**
	 * @param paramMapJSON
	 * @param jedis
	 * @return
	 */
	public Long saveParamMap(String paramMapJSON, Jedis jedis) {
    	synchronized(CURRENT_ID) {
    		Long nextId = ++this.CURRENT_ID;
	        try {
                if(presentInDB(nextId, jedis)) {
                	nextId = getMaxIdFromDB(nextId, jedis);
                }
                LOGGER.info("paramMapJSON = " + paramMapJSON);
                HashMap<String, String> paramMap = gson.fromJson(paramMapJSON, new HashMap<String, String>().getClass());
                paramMap.put("SHORTID", nextId.toString());
                paramMapJSON = gson.toJson(paramMap);
                jedis.set(nextId.toString(), paramMapJSON);
                this.CURRENT_ID = nextId;
                jedis.set("CURRENTID", nextId.toString());
	        } finally {
	            if (jedis != null) {
	                jedis.close();
	            }
	        }
	        return nextId;
    	}
	}
	
	private boolean presentInDB(Long nextId, Jedis jedis) {
		boolean retVal = false;
        String paramJSON = jedis.get(nextId.toString());
        if(null != paramJSON) {
        	retVal = true;
        }
        return retVal;
	}
	
	private Long getMaxIdFromDB(Long nextId, Jedis jedis) {
		boolean idFound = true;
		do {
			nextId++;
	        String paramJSON = jedis.get(nextId.toString());
	        if(null == paramJSON) {
	        	idFound = false;
	        }
		} while (idFound);
        return nextId;
	}
	
	

}
