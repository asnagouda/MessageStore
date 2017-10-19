package com.ngdata.actionstore;

import java.util.UUID;

import redis.clients.jedis.Jedis;

public class UUIDTest {
	public static void main(String[] args) {
		
		UUID uuid1 = UUID.randomUUID();
	    System.out.println( uuid1.toString() );
	    System.out.println( Double.MAX_VALUE );
	    System.out.println( Double.MIN_VALUE );
		
	}
}
