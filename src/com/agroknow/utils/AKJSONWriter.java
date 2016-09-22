package com.agroknow.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public class AKJSONWriter 
{
	private String to_write="";
	
	public void writeAnnotations(ArrayList<Annotation> annotations, String output_folder) throws ParseException, FileNotFoundException, UnsupportedEncodingException
	{
		
		if(annotations.size()==0) 
			return;
		
    	PrintWriter writer = new PrintWriter(output_folder+File.separator+
				annotations.get(0).arn+".annotation.json", "UTF-8");
 
		to_write="{\"enriched\":{\n\t\"arn\":\""+annotations.get(0).arn+"\",\n\t";
		for(int i=0;i<annotations.size();i++)
		{            		
            			String jsonid=annotations.get(i).jsonid;
            			String uris=annotations.get(i).uri;
            			String values=annotations.get(i).value;
            			String scores=String.valueOf(annotations.get(i).score);
            			
            			String initid=jsonid;
            			
            			to_write+="\""+jsonid+"\":[{\"value\":"
            					+ "\""+annotations.get(i).value+"\", "
            							+ "\"uri\":\""+annotations.get(i).uri+"\","
            							+ "\"score\":"+annotations.get(i).score+","
            							+ "\"vocabulary\":\""+annotations.get(i).vocabulary+"\"}";
            					
            			
            			if(i!=annotations.size()-1)
            			{
            				i++;
            				while(annotations.get(i).jsonid==initid)
            				{
            					to_write+=",\n\t{\"value\":"
                    					+ "\""+annotations.get(i).value+"\", "
                    							+ "\"uri\":\""+annotations.get(i).uri+"\","
                    							+ "\"score\":"+annotations.get(i).score+","
                    							+ "\"vocabulary\":\""+annotations.get(i).vocabulary+"\"}";
            					
            					i++;
            					if(i==annotations.size())
            						break;
            				}
            			}
            		to_write+="],";
        } 
		to_write+="}}";
		to_write=to_write.replace("],}}","]}}");
		
		writer.println(to_write);        		
		writer.close();
		
		
	}
	public void writeCore(String output_folder, String jsonfile) throws ParseException, FileNotFoundException, UnsupportedEncodingException
	{
		String contents="";
		try 
        {
        	contents=readFile(jsonfile,StandardCharsets.UTF_8);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return;
        }
		
		JSONObject json = (JSONObject) new JSONParser().parse(contents);
        
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
        	to_write="";
        	
        	
		     String id=((JSONObject)json_a.get(i)).get("dct:identifier").toString();
            	//System.out.println(((JSONObject)json_a.get(i)).get("dct:identifier"));
            	PrintWriter writer = new PrintWriter(output_folder+File.separator+
        				id+".resource.json", "UTF-8");
        		
            	to_write+="{\"resource\": {\n\t";
            	
            		to_write+="\"arn\": \""+id+"\",\n\t";
            		
            		fetch_plain("rdf:about", "uri",(JSONObject)json_a.get(i));
            	    fetch_inner("dct:source", "rdf:resource","center",(JSONObject)json_a.get(i));
            	    
            	    fetch_plain("dct:issued", "publication_date",(JSONObject)json_a.get(i));
            	    fetch_plain("dct:dateSubmitted", "submission_date",(JSONObject)json_a.get(i));
            	    fetch_plain("dct:language", "language",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:uri", "url",(JSONObject)json_a.get(i));
            	    fetch_plain("dct:type", "type",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:isbn", "isbn",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:issue", "issue",(JSONObject)json_a.get(i));
            	    fetch_plain("dct:extent", "extent",(JSONObject)json_a.get(i));
            	    
            	    fetch_multivalue("dc:subject","subject",(JSONObject)json_a.get(i));
            	    fetch_multivalue_inner
    	    			("dc:subject", "content","subject",(JSONObject)json_a.get(i));
            	    
            	    fetch_multivalue("dct:title","title",(JSONObject)json_a.get(i));
            	    fetch_multivalue_inner
        	    		("dct:title","content","title",(JSONObject)json_a.get(i));
            	    
            	    fetch_multivalue("bibo:abstract","abstract",(JSONObject)json_a.get(i));
            	    fetch_multivalue_inner
        	    		("bibo:abstract", "content","abstract",(JSONObject)json_a.get(i));
            	    
            	    fetch_multivalue("dct:description","abstract",(JSONObject)json_a.get(i));
            	    fetch_multivalue_inner
        	    		("dct:description", "content","abstract",(JSONObject)json_a.get(i));
            	    
            	    fetch_multivalue_inner_inner
            	    	("dct:creator","foaf:Person", "foaf:name","author",(JSONObject)json_a.get(i));
            	    fetch_multivalue_inner_inner
        	    		("dct:creator","foaf:Organization", "foaf:name",
        	    				"corporate_author",(JSONObject)json_a.get(i));
            	    fetch_multivalue_inner_inner
        	    		("dct:publisher","foaf:Organization", "foaf:name","publisher",(JSONObject)json_a.get(i));
            	    
            	    fetch_plain_inner_inner_combo
            	    	("dct:isPartOf","bibo:Journal", "dct:title","bibo:ISSN",
            	    			"partof","title","issn",(JSONObject)json_a.get(i));
            	    
            	    fetch_plain("bibo:pageStart", "page_start",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:pageEnd", "page_end",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:volume", "volume",(JSONObject)json_a.get(i));
            	    
        		to_write+="}}";
        		
        		to_write=to_write.replace(",\n}}", "\n}}");
        		to_write=to_write.replace(",\n\t}", "\n\t}");
        		
        		writer.println(to_write);        		
        		writer.close();
        } 
		
		
		
	}

	private void fetch_plain_inner_inner_combo(String json_id, String lvl1_id, String lvl2_1_id, 
			String lvl2_2_id, String output_outer, String output_1, String output_2, JSONObject json)
	{
		String value="";
		try
		{
			String v;
			
			try
			{
				v=((JSONObject)((JSONObject)json.get(json_id)).get(lvl1_id)).get(lvl2_1_id).toString();
				value+="\""+output_1+"\":\""+v+"\",\n\t";
				
			}
			catch(Exception e){}
			
			try
			{
				v=((JSONObject)((JSONObject)json.get(json_id)).get(lvl1_id)).get(lvl2_2_id).toString();
				value+="\""+output_2+"\":\""+v+"\",\n\t";
			}
			catch(Exception e){}
			
			
			
			to_write+="\""+output_outer+"\":{"+value+"},\n\t";
			//System.out.println(output);
		}
		catch(Exception e)
		{
			/*if(to_write.contains("QC2016000017"))
			{
				System.out.println(json_id+"|"+lvl1_id);
				System.out.println(((JSONObject)((JSONObject)json.get(json_id))).get(lvl1_id).toString());
				e.printStackTrace();
			}*/
		}
	}

	private void fetch_multivalue_inner(String json_id, String inner_id, 
			String output_id, JSONObject json)
	{
		String value="";
		try
		{
			JSONArray j_a=((JSONArray)json.get(json_id));

			for(int i=0;i<j_a.size();i++)
			{
				value+="\""+(((JSONObject)j_a.get(i)).get(inner_id)).toString()+"\"";
				
				if(i!=j_a.size()-1)
					value+=",";				
			}
			
			
			
			to_write+="\""+output_id+"\":["+value+"],\n\t";
			
			//System.out.println(output);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			fetch_inner(json_id,inner_id,output_id,json);
		}
	}

	private void fetch_multivalue_inner_inner(String json_id, String lvl1_id, String lvl2_id, 
			String output_id, JSONObject json)
	{
		String value="";
		try
		{
			JSONArray j_a=((JSONArray)json.get(json_id));

			for(int i=0;i<j_a.size();i++)
			{				
				value+="\""+((JSONObject)((JSONObject)j_a.get(i)).get(lvl1_id)).get(lvl2_id).toString()+"\"";
				
				if(i!=j_a.size()-1)
					value+=",";
			}
			
			to_write+="\""+output_id+"\":["+value+"],\n\t";
			
			//System.out.println(output);
		}
		catch(Exception e)
		{
			fetch_plain_inner_inner(json_id,lvl1_id,lvl2_id,output_id,json);
		}
	}
	
	private void fetch_plain_inner_inner(String json_id, String lvl1_id, String lvl2_id, 
			String output_id, JSONObject json)
	{
		String value="";
		try
		{
						
				value+="\""+((JSONObject)((JSONObject)json.get(json_id)).get(lvl1_id)).get(lvl2_id).toString()+"\"";
							
			to_write+="\""+output_id+"\":["+value+"],\n\t";
			
			//System.out.println(output);
		}
		catch(Exception e)
		{
			
		}
	}
	
	private void fetch_multivalue(String json_id, String output_id, JSONObject json)
	{
		String value="";
		try
		{
			JSONArray j_a=((JSONArray)json.get(json_id));

			for(int i=0;i<j_a.size();i++)
			{
				value+="\""+j_a.get(i).toString()+"\"";
				
				if(i!=j_a.size()-1)
					value+=",";
			}
			
			if(value.contains("\"content\":"))
				return;
			
			to_write+="\""+output_id+"\":["+value+"],\n\t";
			
			//System.out.println(output);
		}
		catch(Exception e)
		{
			fetch_plain(json_id,output_id,json);
		}
	}
	
	private void fetch_inner(String json_id, String json_inner, String output_id, JSONObject json)
	{
		String value;
		try
		{
			value=((JSONObject)json.get(json_id)).get(json_inner).toString();
			
			
			to_write+="\""+output_id+"\":\""+value+"\",\n\t";
			
			//System.out.println(output);
		}
		catch(Exception e)
		{
			
		}
	}
	
	private void fetch_plain(String json_id, String output_id, JSONObject json)
	{
		String value;
		try
		{
			value=json.get(json_id).toString();

			if(value.contains("\"content\":"))
				return;
			
			to_write+="\""+output_id+"\":\""+value+"\",\n\t";
			
			//System.out.println(output);
		}
		catch(Exception e)
		{
			
		}
	}

	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	
}
