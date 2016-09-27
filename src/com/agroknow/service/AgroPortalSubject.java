package com.agroknow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agroknow.utils.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AgroPortalSubject extends Service {

	private static String apikey = "095bdee0-2d8d-4e49-b954-e91136289feb";
	
	private String base = "http://services.agroportal.lirmm.fr/";
	
	public AgroPortalSubject() {
		// TODO Auto-generated constructor stub
	}

	private static String get(String urlToGet) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + apikey);
            conn.setRequestProperty("Accept", "application/json");
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	
	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
	
		//apikey = "8e5bcb27-f2ef-46e7-b686-13764ed1a964";
		//input="melanoma";
		
		
		
		String uri = base+"annotator?text="
				+ ""+URLEncoder.encode(input,"UTF-8")+""
						+ "&longest_only=false&exclude_numbers=false&whole_word_only=true"
						+ "&exclude_synonyms=false&expand_mappings=false&score=cvalueh"
						+ "&apikey="+apikey;	
		
		//System.out.println("Input:"+input);
		//System.exit(1);
		
		try
		{
			String result = getURL(uri);
	      
	      JSONArray json = new JSONArray(result.toString());
	      
	      //System.out.println("Annotated:"+input+" with score:"+json);
	      
	      for(int i=0;i<json.length();i++)
	      {
	    	  	JSONObject objectInArray = (JSONObject)json.get(i);        	
	        		    	  	
	    	  	Annotation annotation = new Annotation();
	    	  	annotation.score=(float)objectInArray.getDouble("score")/13;
	    	  	annotation.uri=((JSONObject)objectInArray.get("annotatedClass")).get("@id").toString();
	    	  	
	    	  	JSONObject links = 
	    	  			((JSONObject)((JSONObject)objectInArray.get("annotatedClass")).get("links"));
	    	  	try
	    	  	{
	    	  		String o_url = links.getString("ontology");
	    	  		
	    	  		o_url+="?apikey="+apikey;
	    	  		    	  		
	    	  		JSONObject o_json = new JSONObject(getURL(o_url));	    	  		
	    	  		annotation.vocabulary=o_json.get("name").toString();
	    	  	}
	    	  	catch(Exception e)
	    	  	{
	    	  		//e.printStackTrace();
	    	  	}
	    	  	
	    	  	String annotation_value = ((JSONObject)((JSONArray)objectInArray
	    	  				.get("annotations")).get(0)).get("text").toString();
	    	  	
	    	  	annotation.value=annotation_value.toLowerCase();
	    	  	
	    	  	annotations.add(annotation);
	      }
	      
	      //System.out.println(result);
		}
		catch(Exception e) {e.printStackTrace();}
	      return annotations;
	}
	
}
