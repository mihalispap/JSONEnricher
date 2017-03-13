package com.agroknow.enricher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.agroknow.service.AKSPARQL_FAOGeo;
import com.agroknow.service.FremeGeonames;
import com.agroknow.service.Geonames;
import com.agroknow.service.Lexvo;
import com.agroknow.utils.Annotation;

public class LanguageEnricher extends Enricher {

	public LanguageEnricher() {
		// TODO Auto-generated constructor stub 
		Lexvo service = new Lexvo();
		services.add(service);
	}

	public ArrayList<Annotation> enrich(String jsonfile)
	{

		ArrayList<Annotation> annotations=new ArrayList<Annotation>();

		try 
        {
        	String contents=readFile(jsonfile,StandardCharsets.UTF_8);
            
        	//System.out.println("I will parse:"+jsonfile);
            
            JSONObject json = (JSONObject) new JSONParser().parse(contents);
            
            //JSONArray json_a=((JSONArray)((JSONObject)json.get("rdf:RDF")).get("bibo:Article"));
            
            JSONArray json_a = new JSONArray();
            
            try
            {
            	//json_a=((JSONArray)((JSONObject)json.get("rdf:RDF")).get("bibo:Article"));
            	json_a=((JSONArray)json.get("resource"));
            }
            catch(java.lang.ClassCastException e)
            {
            	json_a.add((JSONObject)json.get("resource"));
            }
            
            
            
            for(int i=0;i<json_a.size();i++)
            {
            	//System.out.println(((JSONObject)json_a.get(i)).get("dct:identifier"));

            	int counter=annotations.size();
            	String arn=((JSONObject)json_a.get(i)).get("dct:identifier").toString();

            	try
            	{
	            	if(((JSONObject)json_a.get(i)).get("dct:title").getClass()
	            			.equals(org.json.simple.JSONArray.class))
					{
	            		JSONArray json_array = (JSONArray)((JSONObject)json_a.get(i)).get("dct:title");
	            		
						for(int j=0;j<json_array.size();j++)
						{
							annotations.addAll(check(((JSONObject)json_array.get(j))
									.get("value").toString()));
						}
					}
					else if(((JSONObject)json_a.get(i)).get("dct:title").getClass()
	            			.equals(org.json.simple.JSONObject.class))
					{
						annotations.addAll(check(((JSONObject)((JSONObject)json_a.get(i)).get("dct:title"))
								.get("value").toString()));			
					}
					else
					{
						annotations.addAll(check(((JSONObject)json_a.get(i)).get("dct:title").toString()));
					}
            	}
            	catch(Exception e) {
            	}

            	try
            	{
	            	if(((JSONObject)json_a.get(i)).get("bibo:abstract").getClass()
	            			.equals(org.json.simple.JSONArray.class))
					{
	            		JSONArray json_array = (JSONArray)((JSONObject)json_a.get(i)).get("bibo:abstract");
	            		
						for(int j=0;j<json_array.size();j++)
						{
							annotations.addAll(check(((JSONObject)json_array.get(j))
									.get("value").toString()));
						}
					}
					else if(((JSONObject)json_a.get(i)).get("bibo:abstract").getClass()
	            			.equals(org.json.simple.JSONObject.class))
					{
						annotations.addAll(check(((JSONObject)((JSONObject)json_a.get(i)).get("bibo:abstract"))
								.get("value").toString()));			
					}
					else
					{
						annotations.addAll(check(((JSONObject)json_a.get(i)).get("bibo:abstract").toString()));
					}
            	}
            	catch(Exception e) {
            	}
            	
            	
            	
            	for(int j=counter;j<annotations.size();j++)
            		annotations.get(j).arn=arn;
            }
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }

		for(int i=0;i<annotations.size();i++)
			annotations.get(i).jsonid="language";
		
		return annotations;
	};
}
