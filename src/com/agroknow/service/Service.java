package com.agroknow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONObject;

import com.agroknow.utils.Annotation;

public abstract class Service 
{
	public ArrayList<Annotation> run(String input, String language) throws Exception{return null;} 

	public String name;
	public long started_on;
	public String base_uri;
	
	public void setStartedTime(){this.started_on=System.currentTimeMillis()/1000;}
	
	public void printStats()
	{
		System.out.println(this.name+" took:"+(System.currentTimeMillis()/1000 - this.started_on));
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	
	protected String getURL(String uri) throws Exception
	{

		StringBuilder result = new StringBuilder();
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
		conn.setRequestProperty("Content-Type", "application/json");
      
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		return result.toString();
	}
	
}
 