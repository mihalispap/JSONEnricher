package com.agroknow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agroknow.utils.Annotation;


public class FremeAGROVOC extends Service { 

	public FremeAGROVOC() {
		// TODO Auto-generated constructor stub 
		this.started_on=System.currentTimeMillis()/1000;
		this.name="Freme AGROVOC";
		
		this.base_uri="i_should_always_run";
	}

	
	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		String uri = "http://api-dev.freme-project.eu/current/e-terminology/tilde?"
				+ "input="+URLEncoder.encode(input,"UTF-8")+"&informat=text&outformat=json-ld"
						+ "&source-lang=en&target-lang=en&domain=TaaS-1001";
	
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();

		if(input.length()<20)
			return annotations;
		
		
		URL url = new URL(uri);
		//logger.info("Calling FREME e-terminology: "+uri);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		connection.setRequestMethod("POST");
		//connection.setRequestProperty("Content-Type", "application/ld+json;charset=UTF-8");
		connection.setRequestProperty("Content-Type", "text/n3;charset=UTF-8");
		
		BufferedReader streamReader;
		try {
			streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return null;
		} 
		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		try {
			while ((inputStr = streamReader.readLine()) != null)
			    responseStrBuilder.append(inputStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		
	    int responseCode;
		try {
			responseCode = connection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
	    
	    //System.out.println(responseStrBuilder);
	    
	    switch (responseCode) {
			case 200:
				JSONObject root = new JSONObject(responseStrBuilder.toString());
				JSONArray annotations_f = (JSONArray) root.get("@graph");
				if ( annotations_f !=null ) 
				{
					for(int i=0;i<annotations_f.length();i++)
					{
						JSONObject annotation = (JSONObject) annotations_f.get(i);
						if (annotation.has("taConfidence"))
						{
							//System.out.println(annotation+" Confidence:"+Double.parseDouble(annotation.get("taConfidence").toString()));
							
							if (Double.parseDouble(annotation.get("taConfidence").toString()) >= 0.60)
							{
								Annotation annotation_f=new Annotation();
								annotation_f.value=annotation.getJSONObject("label").getString("@value");
								annotation_f.vocabulary="agrovoc";
								annotation_f.score=(float) Double.parseDouble(annotation.get("taConfidence").toString());
								annotation_f.uri=null;
								
								String id=annotation.getString("@id");
								
								for(int j=0;j<annotations_f.length();j++)
								{
									JSONObject searcher=(JSONObject) annotations_f.get(j);
									if (searcher.has("annotationUnit"))
									{
										if(searcher.getString("annotationUnit").equals(id))
										{	
											//subject.uri=searcher.getString("termInfoRef");
											URL refUrl = new URL( searcher.get("termInfoRef").toString() );
											annotation_f.uri = searcher.get("termInfoRef").toString();
											break;
										}
									}
								}
								if(!annotation_f.uri.isEmpty())
								{
									annotations.add(annotation_f);
								}
								//System.out.println(subject.value);
							}
						}
						else if(annotation.has("itsrdf:taConfidence"))
						{
							try
							{
								
								if(Integer.parseInt(annotation.getString("itsrdf:taConfidence"))==1)
								{
									Annotation annotation_f=new Annotation();
									annotation_f.value=annotation.getJSONObject("label").getString("@value");
									annotation_f.vocabulary="agrovoc";
									annotation_f.score=1.0f;//Double.parseDouble(annotation.get("itsrdf:taConfidence").toString());
									annotation_f.uri=null;
									
									String id=annotation.getString("@id");
									
									for(int j=0;j<annotations_f.length();j++)
									{
										JSONObject searcher=(JSONObject) annotations_f.get(j);
										if (searcher.has("annotationUnit"))
										{
											if(searcher.getString("annotationUnit").equals(id))
											{	
												//subject.uri=searcher.getString("termInfoRef");
												URL refUrl = new URL( searcher.get("termInfoRef").toString() );
												annotation_f.uri = searcher.get("termInfoRef").toString();
												break;
											}
										}
									}
									if(!annotation_f.uri.isEmpty())
									{
										int k;
										for(k=0;k<annotations.size();k++)
										{
											if(annotations.get(k).value.equalsIgnoreCase(annotation_f.value))
											{
												break;
											}
										}
										
										if(k==annotations.size())
											annotations.add(annotation_f);
										//System.out.println(subject.value+", score:"+subject.score);
									}
								}
							}
							catch(java.lang.NumberFormatException e)
							{
								//e.printStackTrace();
								continue;
							}
							catch(org.codehaus.jettison.json.JSONException e)
							{
								//e.printStackTrace();
								continue;
							}
						}
					}
				
				}			
	    }
	    
	    return annotations;
	}
}
