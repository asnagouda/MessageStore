package com.ngdata.actionstore;

import java.util.HashMap;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONTest {
	public static void main(String[] args) {
		
		HashMap<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put("SHORTID", "34534");
		parametersMap.put("URL", "http://localhost:8080/sc");
		Gson gson = new GsonBuilder().create();
		String parametersMapJSON = gson.toJson(parametersMap);
		System.out.println("parametersMapJSON = " + parametersMapJSON);
		
	}
}
