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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.XMLGregorianCalendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import javassist.bytecode.Descriptor.Iterator;

public class AKJSONWriter 
{
	private String to_write="";
	
	public String map(String key)
	{
		/*
		 * 
			dct:language
			dct:title
			bibo:pageStart
			dc:subject
			bibo:pageEnd
			dct:type
			rdf:about
			dct:identifier
			dct:source
			dct:isPartOf
			dct:issued
			dct:dateSubmitted
			dct:creator
			dct:description
			dct:subject
			bibo:authorList
			bibo:issue
			bibo:abstract
			
			
			!!PREV (readable) version!!
			
			if(key.equals("dct:language"))
			return "language";
		if(key.equals("dct:title"))
			return "title";
		if(key.equals("bibo:pageStart"))
			return "page_start";
		if(key.equals("dc:subject"))
			return "subject";
		if(key.equals("bibo:pageEnd"))
			return "page_end";
		if(key.equals("dct:type"))
			return "type";
		if(key.equals("rdf:about"))
			return "uri";
		if(key.equals("dct:identifier"))
			return "arn";
		if(key.equals("dct:source"))
			return "source";
		if(key.equals("dct:isPartOf"))
			return "is_part_of";
		if(key.equals("dct:issued"))
			return "date_issued";
		if(key.equals("dct:dateSubmitted"))
			return "date_submitted";
		if(key.equals("dct:creator"))
			return "author";
		if(key.equals("dct:description"))
			return "description";
		if(key.equals("dct:subject"))
			return "subject";
		if(key.equals("bibo:authorList"))
			return "authors";
		if(key.equals("bibo:issue"))
			return "issue";
		if(key.equals("bibo:abstract"))
			return "abstract";
		if(key.equals("bibo:Journal"))
			return "journal";
		if(key.equals("foaf:name"))
			return "name";
		if(key.equals("foaf:Person"))
			return "person";
		if(key.equals("bibo:ISSN"))
			return "issn";
		if(key.equals("bibo:ISBN"))
			return "isbn";
		if(key.equals("bibo:issn"))
			return "issn";
		if(key.equals("bibo:isbn"))
			return "isbn";
		if(key.equals("content"))
			return "value";
		if(key.equals("xml:lang"))
			return "language";
		if(key.equals("rdf:resource"))
			return "uri";
		if(key.equals("bibo:volume"))
			return "volume";
		if(key.equals("foaf:Organization"))
			return "organization";
		if(key.equals("bibo:doi"))
			return "doi";
		if(key.equals("bibo:uri"))
			return "url";
		if(key.equals("dct:publisher"))
			return "publisher";
		if(key.equals("dct:medium"))
			return "medium";
		if(key.equals("dct:extent"))
			return "extent";
		
		return "field:"+key;
			
		 * 
		 * */
		if(key.equals("dct:language"))
			return "dct:language";
		
		if(key.equals("dct:title"))
			return "dct:title";
		
		if(key.equals("bibo:pageStart"))
			return "bibo:pageStart";
		
		if(key.equals("dc:subject"))
			return "dc:subject";
		
		if(key.equals("bibo:pageEnd"))
			return "bibo:pageEnd";
		
		if(key.equals("dct:type"))
			return "dct:type";
		
		if(key.equals("rdf:about"))
			return "rdf:about";
		
		if(key.equals("dct:identifier"))
			return "dct:identifier";
		
		if(key.equals("dct:source"))
			return "dct:source";
		
		if(key.equals("dct:isPartOf"))
			return "dct:isPartOf";
		
		if(key.equals("dct:issued"))
			return "dct:issued";
		
		if(key.equals("dct:dateSubmitted"))
			return "dct:dateSubmitted";
		
		if(key.equals("dct:creator"))
			return "dct:creator";
		
		if(key.equals("dct:description"))
			return "dct:description";
		
		if(key.equals("dct:subject"))
			return "dct:subject";
		
		if(key.equals("bibo:authorList"))
			return "bibo:authorList";
		
		if(key.equals("bibo:issue"))
			return "bibo:issue";
		
		if(key.equals("bibo:abstract"))
			return "bibo:abstract";
		
		if(key.equals("bibo:Journal"))
			return "bibo:Journal";
		
		if(key.equals("foaf:name"))
			return "foaf:name";
		
		if(key.equals("foaf:Person"))
			return "foaf:Person";
		
		if(key.equals("bibo:ISSN"))
			return "bibo:ISSN";
		
		if(key.equals("bibo:ISBN"))
			return "bibo:ISBN";
		
		if(key.equals("bibo:issn"))
			return "bibo:issn";
		
		if(key.equals("bibo:isbn"))
			return "bibo:isbn";
		
		if(key.equals("content"))
			return "value";
		
		if(key.equals("xml:lang"))
			return "xml:lang";
		
		if(key.equals("rdf:resource"))
			return "rdf:resource";
		
		if(key.equals("bibo:volume"))
			return "bibo:volume";
		
		if(key.equals("foaf:Organization"))
			return "foaf:Organization";
		
		if(key.equals("bibo:doi"))
			return "bibo:doi";
		
		if(key.equals("bibo:uri"))
			return "bibo:uri";
		
		if(key.equals("dct:publisher"))
			return "dct:publisher";
		
		if(key.equals("dct:medium"))
			return "dct:medium";
		
		if(key.equals("dct:extent"))
			return "dct:extent";
		
		return "field:"+key;
	}
	
	public boolean combo_flag=false;
	
	public static String html2text(String html) {
	    return Jsoup.parse(html).text();
	}
	
	public boolean isSpecialCase(String key)
	{
		if(key.equals("bibo:authorList") || key.equals("dct:subject"))
			return true;
		return false;
	}
	
	public void isPossibleCombo(String key)
	{
		if(key.equals("dct:title")
				||
			key.equals("bibo:abstract")
				||
			key.equals("dc:subject")
				||
			key.equals("dct:isPartOf"))
				this.combo_flag=true;
		else {
			this.combo_flag=false;
		}
	}
	
	public void handleJSONArray(JSONArray json, String key)
	{
		String mapped=map(key);
		
		to_write+="\""+mapped+"\":[\n\t\t";
		
			for(int i=0;i<json.size();i++)
			{
				if(json.get(i).getClass().equals(org.json.simple.JSONArray.class))
				{
					System.out.println("What to do?");
				}
				else if(json.get(i).getClass().equals(org.json.simple.JSONObject.class))
				{
					handleJSONObject((JSONObject) json.get(i),"plain");
				}
				else
				{
					//System.out.println(json.get(i).getClass()+", "+mapped+", "
    				//		+json.get(i).toString());
					handlePlainValue(json.get(i), "plain");
				}
			}
		
		to_write+="],\n\t";
	}
	
	public void handleJSONObject(JSONObject json, String key)
	{
		String mapped=map(key);
		
		if(!key.equals("plain"))
			to_write+="\""+mapped+"\":{";
		else
			to_write+="{";
		
			Set<String> elements = json.keySet();		        	
		    java.util.Iterator<String> it=elements.iterator();
		    while (it.hasNext()) 
		    {
		    	String key_inner=it.next();
		        //System.out.println(key+"|"+json.get(key_inner).getClass());
		      		
		    	if(json.get(key_inner).getClass().equals(org.json.simple.JSONArray.class))
		    	{
		    		handleJSONArray((JSONArray)json.get(key_inner), key_inner);
		    	}
		    	else if(json.get(key_inner).getClass().equals(org.json.simple.JSONObject.class))
		    	{
		    		handleJSONObject((JSONObject)json.get(key_inner), key_inner);
		    	}
		    	else
		    	{
		    		handlePlainValue(json.get(key_inner), key_inner);
		    	}
		    }	
		to_write+="},\n\t";
	}
	
	public void handlePlainValue(Object value, String key)
	{
		String mapped = map(key);
		
		if(!key.equals("plain"))
			to_write+="\""+mapped+"\":";
		
		if(this.combo_flag==true)
		{
			//System.out.println("--------------\ntowrite was:"+to_write+"");
			to_write+="{\"value\":";
			//System.out.println("towrite iss:"+to_write+"\n-------------------");
		}
		try
		{
			float v = Float.parseFloat(value.toString());
			
			double x = v - Math.floor(v);
			
			if(x == 0.0f)
				to_write+=(int)(Math.floor(v));
			else
				to_write+=v;
			
			//to_write+=Float.parseFloat(value.toString());
		}
		catch(NumberFormatException e)
		{
			to_write+="\""+this.html2text(value.toString().replace("\"","\\\""))+"\"";
		}
		
		if(this.combo_flag==true)
		{
			to_write+="}";
			//this.combo_flag=false;
		}
		/*if(value instanceof Number)
			to_write+=value;
		else
			to_write+="\""+value+"\"";*/
				
		to_write+=",\n\t";
		
		//to_write=to_write.replace("{{\"value\":", "{\"value\":");
		//to_write=to_write.replace("\"xml:lang\":{\"value\":", "\"xml:lang\":");

		String regex="\\{\"value\":\\{\"value\":\"(.*?)\"\\}";
		
		Pattern p = Pattern.compile(regex);
	      
	     // get a matcher object
	     Matcher m = p.matcher(to_write); 
	     to_write = m.replaceAll("{\"value\":\"$1\"");
		
	     regex="\"xml:lang\":\\{\"value\":\"(.*?)\"\\}";
	     p = Pattern.compile(regex);
	     m = p.matcher(to_write);
	     to_write = m.replaceAll("\"xml:lang\":\"$1\"");
		//to_write = to_write.replaceAll("{\"value\":{\"value\":\"(.*?)\"}","$1");
		
		
		
	}
	
	public void writeAnnotations(ArrayList<Annotation> annotations, String output_folder) throws ParseException, FileNotFoundException, UnsupportedEncodingException
	{
		
		if(annotations.size()==0) 
			return;
		
    	PrintWriter writer = new PrintWriter(output_folder+File.separator+
				annotations.get(0).arn+".annotation.json", "UTF-8");
 
    	Utilities utils = new Utilities();
    	annotations = utils.cleanseAnnotations(annotations);
    	
    	/*System.out.println("-INIT CASE-");
		for(int i=0;i<annotations.size();i++)
			System.out.println(annotations.get(i));
		System.out.println("---");*/
    	
		to_write="{\"enriched\":{\n\t\"identifier\":\""+annotations.get(0).arn+"\",\n\t";
		for(int i=0;i<annotations.size();i++)
		{
			String jsonid=annotations.get(i).jsonid;
			
			if(annotations.get(i).exists(annotations, (i+1), jsonid))
					continue;
					
			//System.out.println("Until here with value:"+annotations.get(i).toString());
			
            			String uri=annotations.get(i).uri;
            			String values=annotations.get(i).value;
            			String scores=String.valueOf(annotations.get(i).score);
            			
            			String initid=jsonid;
            			
            			/*to_write+="\""+jsonid+"\":[{\"value\":"
            					+ "\""+annotations.get(i).value+"\", "
            							+ "\"uri\":\""+annotations.get(i).uri+"\","
            							+ "\"score\":"+annotations.get(i).score+","
            							+ "\"vocabulary\":\""+annotations.get(i).vocabulary+"\"}";
            			*/
    					to_write+="\""+jsonid+"\":[{";
    						to_write+="\"value\":"
    								+ "\""+annotations.get(i).value.toLowerCase()+"\",";
    						to_write+="\"uri\":[";
    							to_write+=utils.fetch_uris(annotations, annotations.get(i).value, jsonid);
    						to_write+="],";
    						
    						to_write+="\"score\":"+utils.fetch_max_score(annotations, 
    									annotations.get(i).value, jsonid);
    						
    						to_write+=",\"vocabulary\":[";
    							to_write+=utils.fetch_vocs(annotations, 
    									annotations.get(i).value, jsonid);
    						to_write+="]\n\t}";
    						
    						if(i!=annotations.size()-1)
                			{
                				i++;
                				while(annotations.get(i).jsonid==initid)
                				{
                					if(annotations.get(i).exists(annotations, (i+1), jsonid))
                					{
                						i++;
                						continue;
                					}
                					to_write+=",\n\t{\"value\":"
                        					+ "\""+annotations.get(i).value+"\", ";
                					
                					
                					to_write+="\"uri\":[";
	        							to_write+=utils.fetch_uris(annotations, annotations.get(i).value, jsonid);
	        						to_write+="],";
	        						
	        						to_write+="\"score\":"+utils.fetch_max_score(annotations, 
	        									annotations.get(i).value, jsonid);
	        						
	        						to_write+=",\"vocabulary\":[";
	        							to_write+=utils.fetch_vocs(annotations, 
	        									annotations.get(i).value, jsonid);
	        						to_write+="]\n\t}";
                					
                					i++;
                					if(i==annotations.size())
                						break;
                				}
                				i--;
                			}
    					to_write+="],\n\t";
            			
        } 
		to_write+="}}";
		to_write=to_write.replace(",]", "]");
		to_write=to_write.replace("[,", "[");
		to_write=to_write.replace("],}}","]}}");
		to_write=to_write.replace("}],\n\t}", "}]\n\t}");
		
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
        
        Utilities utils = new Utilities();
        
        for(int i=0;i<json_a.size();i++)
        {   
        	String id=((JSONObject)json_a.get(i)).get("dct:identifier").toString();
        	
        	//if(!utils.isStevia(id))
        	//	continue;
        	
        	PrintWriter writer = new PrintWriter(output_folder+File.separator+
    				id+".resource.json", "UTF-8");
        	
        	//System.out.println("I am doing ARN:"+id+" will write to:"+output_folder+File.separator+
    		//		id+".resource.json");
        	
        	to_write="";
        	
        	to_write+="{\"resource\": {\n\t";
        	
        	JSONObject objectInArray = (JSONObject)json_a.get(i);        	
        	Set<String> elements = objectInArray.keySet();
        	
        	java.util.Iterator<String> it=elements.iterator();
        	while (it.hasNext()) 
        	{
        		String key=it.next();
        		
        		if(isSpecialCase(key))
        			continue;
        		
        		this.isPossibleCombo(key);
        		
        	    //System.out.println(key+"|"+objectInArray.get(key).getClass()+" OUTER");
        		//System.out.println(key);
        		if(objectInArray.get(key).getClass().equals(org.json.simple.JSONArray.class))
        		{
        			handleJSONArray((JSONArray)objectInArray.get(key), key);
        		}
        		else if(objectInArray.get(key).getClass().equals(org.json.simple.JSONObject.class))
        		{
        			handleJSONObject((JSONObject)objectInArray.get(key), key);
        		}
        		else
        		{
        			handlePlainValue(objectInArray.get(key), key);
        		}
        	}
        	
        	to_write+="}}";
    		
    		to_write=to_write.replace(",\n}}", "\n}}");
    		to_write=to_write.replace(",\n\t}", "\n\t}");
    		to_write=to_write.replace(",]", "]");
    		to_write=to_write.replace(",\n\t]", "\n\t]");
    		
    		//System.out.println(to_write);
    		writer.println(to_write);
    		writer.flush();
    		writer.close();
        
    		//if(i==1)
    		//	System.exit(0);
    		
        }
        
        if(true)
        	return;
        
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
            	                	    
            	    fetch_inner_array("dct:isPartOf", "rdf:resource","journal_uri",(JSONObject)json_a.get(i));            	    
            	    fetch_array_inner_inner_combo
            	    	("dct:isPartOf","bibo:Journal", "dct:title","bibo:ISSN",
            	    			"partof","title","issn",(JSONObject)json_a.get(i));
            	    
            	    fetch_plain("bibo:pageStart", "page_start",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:pageEnd", "page_end",(JSONObject)json_a.get(i));
            	    fetch_plain("bibo:volume", "volume",(JSONObject)json_a.get(i));
            	    
        		to_write+="}}";
        		
        		to_write=to_write.replace(",\n}}", "\n}}");
        		to_write=to_write.replace(",\n\t}", "\n\t}");
        		to_write=to_write.replace(",]", "]");
        		
        		
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
		}
	}

	private void fetch_array_inner_inner_combo(String json_id, String lvl1_id, String lvl2_1_id, 
			String lvl2_2_id, String output_outer, String output_1, String output_2, JSONObject json)
	{
		String value="";
		try
		{
			JSONArray j_a=((JSONArray)json.get(json_id));

			for(int i=0;i<j_a.size();i++)
			{
				String v;
				
				try
				{
					v=((JSONObject)((JSONObject)((JSONObject)j_a.get(i))).get(lvl1_id)).get(lvl2_1_id).toString();
					value+="\""+output_1+"\":\""+v+"\",\n\t";
					
	
					v=((JSONObject)((JSONObject)((JSONObject)j_a.get(i))).get(lvl1_id)).get(lvl2_2_id).toString();
					value+="\""+output_2+"\":\""+v+"\",\n\t";
					
					
					if(i!=j_a.size()-1)
						value+=",";
				}
				catch(Exception e)
				{
				}
			}
			

			to_write+="\""+output_outer+"\":{"+value+"},\n\t";
			
			//System.out.println(to_write);
		}
		catch(Exception e)
		{
			fetch_plain_inner_inner_combo(json_id, lvl1_id, lvl2_1_id, 
					lvl2_2_id, output_outer, output_1, output_2, json);
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
				try
				{
					value+="\""+(((JSONObject)j_a.get(i)).get(inner_id)).toString()+"\"";
					
					if(i!=j_a.size()-1)
						value+=",";
				}
				catch(Exception e){}
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
			//e.printStackTrace();
		}
	}

	private void fetch_inner_array(String json_id, String json_inner, String output_id, JSONObject json)
	{
		String value="";
		try
		{
			JSONArray j_a=((JSONArray)json.get(json_id));

			for(int i=0;i<j_a.size();i++)
			{
				try
				{
					value+="\""+((JSONObject)j_a.get(i)).get(json_inner).toString()+"\"";
					
					if(i!=j_a.size()-1)
						value+=",";
				}
				catch(Exception e){}
			}
			
			
			if(value.contains("\"content\":"))
				return;
			
			to_write+="\""+output_id+"\":["+value+"],\n\t";
			//System.out.println(to_write);
			
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			fetch_inner(json_id, json_inner, output_id, json);
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
