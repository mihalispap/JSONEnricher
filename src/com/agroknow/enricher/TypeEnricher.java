package com.agroknow.enricher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.agroknow.service.AKSPARQL_VIVO;
import com.agroknow.service.FremeGeonames;
import com.agroknow.service.Geonames;
import com.agroknow.utils.Annotation;

public class TypeEnricher extends Enricher {

	public TypeEnricher() {
		// TODO Auto-generated constructor stub 
		AKSPARQL_VIVO service = new AKSPARQL_VIVO();
		services.add(service);
	}


	protected ArrayList<Annotation> check(String input)
	{
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();

		try
    	{
    		String value=input;

    		value=value.replace("[", "");
    		value=value.replace("]", "");
    		value=value.replace("\"", "");
			
    		identify_lang(value);
    		
    		annotations.addAll(run_services(value, annotations));    
    		
    		String[] values=value.split(",");
    		
    		for(int j=0;j<values.length;j++)
    		{
    			
    			annotations.addAll(run_services(values[j], annotations));
    		}
    	}
    	catch(Exception e)
    	{
    		return annotations;
    	}
		
		
		return annotations;
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
	            	if(((JSONObject)json_a.get(i)).get("dct:type").getClass()
	            			.equals(org.json.simple.JSONArray.class))
					{
	            		JSONArray json_array = (JSONArray)((JSONObject)json_a.get(i)).get("dct:type");
	            		
						for(int j=0;j<json_array.size();j++)
						{
							annotations.addAll(check(((JSONObject)json_array.get(j))
									.toString()));
						}
					}
					else
					{
						annotations.addAll(check(((JSONObject)json_a.get(i)).get("dct:type").toString()));
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
			annotations.get(i).jsonid="type";
		
		return annotations;
	};

}
