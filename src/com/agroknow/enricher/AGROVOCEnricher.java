package com.agroknow.enricher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.agroknow.service.AKSPARQL_AGROVOC;
import com.agroknow.service.FremeAGROVOC;
import com.agroknow.service.Geonames;
import com.agroknow.utils.Annotation;

public class AGROVOCEnricher extends Enricher {
 
	public AGROVOCEnricher() {
		// TODO Auto-generated constructor stub
		AKSPARQL_AGROVOC service = new AKSPARQL_AGROVOC();
		services.add(service);
		FremeAGROVOC service_f = new FremeAGROVOC();
		services.add(service_f); 
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
            	json_a=((JSONArray)((JSONObject)json.get("rdf:RDF")).get("bibo:Article"));
            }
            catch(java.lang.ClassCastException e)
            {
            	json_a.add(((JSONObject)json.get("rdf:RDF")).get("bibo:Article"));
            }
            
            
            
            for(int i=0;i<json_a.size();i++)
            {
            	//System.out.println(((JSONObject)json_a.get(i)).get("dct:identifier"));

            	int counter=annotations.size();
            	String arn=((JSONObject)json_a.get(i)).get("dct:identifier").toString();
            	
            	try
            	{
            		annotations.addAll(check((JSONObject)json_a.get(i), "dc:subject"));
            	}
            	catch(Exception e){}
            	
            	try
            	{
            		annotations.addAll(check((JSONObject)json_a.get(i), "dct:title"));
            	}
            	catch(Exception e){}
            	/*TODO:
            	 * 	check array case!!*/
            	try
            	{
            	annotations.addAll(check((JSONObject)
            			((JSONObject)(JSONObject)json_a.get(i)).get("dct:title"), "content"));
            	}
            	catch(Exception e)
            	{
            		//System.out.println(arn);
            		//e.printStackTrace();
            	}
            	try
            	{
            		annotations.addAll(check((JSONObject)json_a.get(i), "bibo:abstract"));
            	}
            	catch(Exception e){}
            	
            	try
            	{
            		annotations.addAll(check((JSONObject)
            			((JSONObject)(JSONObject)json_a.get(i)).get("bibo:abstract"), "content"));
            	}
        		catch(Exception e){}

            	for(int j=counter;j<annotations.size();j++)
            		annotations.get(j).arn=arn;
            }
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		
		return annotations;
	};

}
