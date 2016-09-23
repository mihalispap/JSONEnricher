package com.agroknow.enricher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.agroknow.service.FremeGeonames;
import com.agroknow.service.Geonames;
import com.agroknow.utils.Annotation;

public class LocationEnricher extends Enricher {
 
	public LocationEnricher() {
		// TODO Auto-generated constructor stub 
		Geonames service = new Geonames();
		services.add(service);
		FremeGeonames service_f = new FremeGeonames();
		services.add(service_f);
	}

	public ArrayList<Annotation> enrich(String jsonfile)
	{

		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
		String arn="";
		
		try 
        {
        	String contents=readFile(jsonfile,StandardCharsets.UTF_8);
        	
            System.out.println("I will parse:"+jsonfile);
            
            JSONObject json =(JSONObject) ( (JSONObject) new JSONParser().parse(contents)).get("resource");
            
            Set<String> elements = json.keySet();
        	
            System.out.println(elements);
            
        	java.util.Iterator<String> it=elements.iterator();
        	while (it.hasNext()) 
        	{
        		String key=it.next();
        		
        		
        	    //System.out.println(key+"|"+objectInArray.get(key).getClass());
        		System.out.println(key);
        		
        	}
            
            if(true) return null;
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
            	arn=((JSONObject)json_a.get(i)).get("dct:identifier").toString();
            	
            	int counter=annotations.size();
            	
            	//System.out.println(((JSONObject)json_a.get(i)).get("dct:identifier"));
            	try
            	{
            		annotations.addAll(check((JSONObject)json_a.get(i), "dc:subject"));
            	}
            	catch(Exception e){
            		e.printStackTrace();
            	}
            	try
            	{
            		annotations.addAll(check((JSONObject)json_a.get(i), "dct:title"));
            	}
            	catch(Exception e){}
            	
            	try
            	{
            		annotations.addAll(check((JSONObject)json_a.get(i), "bibo:abstract"));
            	}
            	catch(Exception e){}
            	
            	for(int j=counter;j<annotations.size();j++)
            		annotations.get(j).arn=arn;
            	//break;
            }
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		
		for(int i=0;i<annotations.size();i++)
			annotations.get(i).jsonid="location";
		
		return annotations;
	};
}
