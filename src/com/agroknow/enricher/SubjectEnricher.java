package com.agroknow.enricher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.agroknow.service.AKSPARQL_AGROVOC;
import com.agroknow.service.AKSPARQL_GACS;
import com.agroknow.service.AKSPARQL_GrapeVarieties;
import com.agroknow.service.AgroPortalSubject;
import com.agroknow.service.FremeAGROVOC;
import com.agroknow.service.Geonames;
import com.agroknow.utils.Annotation;
import com.agroknow.utils.Utilities;

public class SubjectEnricher extends Enricher {
 
	public SubjectEnricher() {
		// TODO Auto-generated constructor stub
		AKSPARQL_AGROVOC service = new AKSPARQL_AGROVOC();
		services.add(service);
		FremeAGROVOC service_f = new FremeAGROVOC();
		services.add(service_f); 
		AgroPortalSubject service_ap = new AgroPortalSubject();
		services.add(service_ap); 
		AKSPARQL_GACS service_gc = new AKSPARQL_GACS();
		services.add(service_gc); 
		/*TODO:
		 * 		add only afetr cleansing is performed!*/
		AKSPARQL_GrapeVarieties service_gv = new AKSPARQL_GrapeVarieties();
		services.add(service_gv); 
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
            	String arn=((JSONObject)json_a.get(i)).get("arn").toString();

            	try
            	{
	            	if(((JSONObject)json_a.get(i)).get("subject").getClass()
	            			.equals(org.json.simple.JSONArray.class))
					{
	            		JSONArray json_array = (JSONArray)((JSONObject)json_a.get(i)).get("subject");
	            		
						for(int j=0;j<json_array.size();j++)
						{
							annotations.addAll(check(((JSONObject)json_array.get(j))
									.get("value").toString()));
						}
					}
					else if(((JSONObject)json_a.get(i)).get("subject").getClass()
	            			.equals(org.json.simple.JSONObject.class))
					{
						annotations.addAll(check(((JSONObject)((JSONObject)json_a.get(i)).get("subject"))
								.get("value").toString()));			
					}
					else
					{
						annotations.addAll(check(((JSONObject)json_a.get(i)).get("subject").toString()));
					}
            	}
            	catch(Exception e) {
            	}

            	try
            	{
	            	if(((JSONObject)json_a.get(i)).get("title").getClass()
	            			.equals(org.json.simple.JSONArray.class))
					{
	            		JSONArray json_array = (JSONArray)((JSONObject)json_a.get(i)).get("title");
	            		
						for(int j=0;j<json_array.size();j++)
						{
							annotations.addAll(check(((JSONObject)json_array.get(j))
									.get("value").toString()));
						}
					}
					else if(((JSONObject)json_a.get(i)).get("title").getClass()
	            			.equals(org.json.simple.JSONObject.class))
					{
						annotations.addAll(check(((JSONObject)((JSONObject)json_a.get(i)).get("title"))
								.get("value").toString()));			
					}
					else
					{
						annotations.addAll(check(((JSONObject)json_a.get(i)).get("title").toString()));
					}
            	}
            	catch(Exception e) {
            	}

            	try
            	{
	            	if(((JSONObject)json_a.get(i)).get("abstract").getClass()
	            			.equals(org.json.simple.JSONArray.class))
					{
	            		JSONArray json_array = (JSONArray)((JSONObject)json_a.get(i)).get("abstract");
	            		
						for(int j=0;j<json_array.size();j++)
						{
							annotations.addAll(check(((JSONObject)json_array.get(j))
									.get("value").toString()));
						}
					}
					else if(((JSONObject)json_a.get(i)).get("abstract").getClass()
	            			.equals(org.json.simple.JSONObject.class))
					{
						annotations.addAll(check(((JSONObject)((JSONObject)json_a.get(i)).get("abstract"))
								.get("value").toString()));			
					}
					else
					{
						annotations.addAll(check(((JSONObject)json_a.get(i)).get("abstract").toString()));
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
			annotations.get(i).jsonid="subject";
		
		return annotations;
	};

}
