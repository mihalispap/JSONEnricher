package com.agroknow.enricher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.agroknow.service.Service;
import com.agroknow.utils.Annotation;
import com.agroknow.utils.Utilities;
import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public abstract class Enricher 
{ 
	public ArrayList<Service> services=new ArrayList<Service>();
	public ArrayList<Annotation> enrich(String jsonfile){return null;}; 
	
	public String language="";
	
	protected ArrayList<Annotation> run_services(String input, ArrayList<Annotation> annotations_g) throws Exception
	{
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
		
		Utilities utils = new Utilities();
		try
		{
			for(int i=0;i<services.size();i++)
			{
				/*Logging*/
				//services.get(i).setStartedTime();
				
				/*TODO:
				 * 		check if i should also pass annotations variable (current annotations)				 * 
				 * */
				if(!utils.isWorthRunning(services.get(i), annotations_g, input))
					continue;
				
				try
				{
					annotations.addAll(services.get(i).run(input, language));
				}
				catch(Exception e){}
				/*Printing*/
				//services.get(i).printStats();
			}
			return annotations;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return annotations;
		}
	}

	protected ArrayList<Annotation> check(JSONObject json, String identifier)
	{
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();

		try
    	{
    		String value=((JSONObject)json).get(identifier).toString();

    		Utilities utils = new Utilities();
   		
    		value=value.replace("[", " ");
    		value=value.replace("]", " ");
    		value=value.replace("\"", " ");
    		value=value.replace("(", " ");
    		value=value.replace(")", " ");
    		value=value.replace(".", " ");
			
    		identify_lang(value);
    		
    		annotations.addAll(run_services(value, annotations));    		
    		String[] values=value.split(",");
    		
    		for(int j=0;j<values.length;j++)
    		{

    			String[] inner=values[j].split(" ");
    			
    			for(int k=0;k<inner.length;k++)
    			{
 				
    				if(utils.isStopWord(inner[k]))
    					continue;
    				
    				
    				annotations.addAll(run_services(inner[k], annotations));
    				if(k!=inner.length-1)
    					annotations.addAll(run_services(inner[k]+" "+inner[k+1], annotations));
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		return annotations;
    	}
		
		return annotations;
	}

	protected ArrayList<Annotation> check(String input)
	{
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();

		try
    	{
    		String value=input;

    		value=value.replace("[", " ");
    		value=value.replace("]", " ");
    		value=value.replace("\"", " ");
    		value=value.replace("(", " ");
    		value=value.replace(")", " ");
    		value=value.replace(".", " ");
    		
    		Utilities utils = new Utilities();
    		
    		identify_lang(value);
    		
    		annotations.addAll(run_services(value, annotations));    
    		
    		String[] values=value.split(",");
    		
    		for(int j=0;j<values.length;j++)
    		{
    			
    			String[] inner=values[j].split(" ");
    			
    			for(int k=0;k<inner.length;k++)
    			{
    				if(utils.isStopWord(inner[k]))
    					continue;

    				//if(utils.alreadyEnriched(inner[k],annotations))
    				//	continue;
    				
    				annotations.addAll(run_services(inner[k], annotations));
    				if(k!=inner.length-1)
    					annotations.addAll(run_services(inner[k]+" "+inner[k+1], annotations));
    			}
    		}
    	}
    	catch(Exception e)
    	{

    	}
		
		
		return annotations;
	}

	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	

	protected void identify_lang(String value)
	{
		//load all languages:
		List<LanguageProfile> languageProfiles = null;
		try {
			languageProfiles = new LanguageProfileReader().readAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//build language detector:
		LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
		        .withProfiles(languageProfiles)
		        .build();

		//create a text object factory
		//TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

		TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingShortCleanText();
		
		//query:
		//System.out.println("I am trying to detect:"+value);
		TextObject textObject = textObjectFactory.forText(value.toLowerCase());/*+""
				+ " i am an english text and not only a word");*/
		Optional<LdLocale> lang = languageDetector.detect(textObject);
		
		try
		{
			//System.out.println(lang.toString());
			String iso2=lang.get().getLanguage();
			if(!iso2.isEmpty() && iso2!="")
				language=iso2;
		}
		catch(java.lang.IllegalStateException e)
		{
			this.language="en";
			//e.printStackTrace();
		}
	}
	
}
