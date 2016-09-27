package com.agroknow.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.agroknow.utils.Annotation;

public class Geonames extends Service {

	public Geonames() { 
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
		
		String absolute_path=System.getProperty("user.dir")+System.getProperty("file.separator")+""
				+ "assets"+System.getProperty("file.separator");
		
			String toCheck; 
			
			toCheck=input.replace(",", "");
			toCheck=toCheck.replace("(", "");
			toCheck=toCheck.replace(")", "");
			toCheck=toCheck.replace("-", "");
			toCheck=toCheck.replace("{", "");
			toCheck=toCheck.replace("}", "");
			toCheck=toCheck.replace("/", "");
			//toCheck=toCheck.replace(" ", "");
			
			FileInputStream fstream = new FileInputStream(absolute_path+"countries.db");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			while ((strLine = br.readLine()) != null)   
			{

				String[] geonames=strLine.split("\t");
			  
				boolean found=false;
				String geonames_id="";
				
				//System.out.println("Going to compare:"+toCheck+", with:"+geonames[4]);
				
				if(toCheck.equalsIgnoreCase(geonames[4]))
				{
						found=true;
						geonames_id=geonames[16];
						//System.out.println("FOUND!!");
				}
				
				/*if(toCheck.compareToIgnoreCase(geonames[4])>0)
					break;*/
				
				if(found)
				{
					if(geonames_id.equals("geonameid"))
						continue;
					
					Annotation annotation=new Annotation();
					
					annotation.score=1.0f;
					annotation.value=geonames[4];
					annotation.uri="http://sws.geonames.org/"+geonames_id;
					annotation.vocabulary="geonames";
					
					annotations.add(annotation);
					
					//System.out.println(annotation.toString());

					break;
				}
			}
			br.close();
		
		
			
			fstream = new FileInputStream(absolute_path+"continents.db");
			br = new BufferedReader(new InputStreamReader(fstream));

			while ((strLine = br.readLine()) != null)   
			{

				String[] geonames=strLine.split("\t");
			  
				boolean found=false;
				String geonames_id="";
				
				if(toCheck.equalsIgnoreCase(geonames[1]))
				{
						found=true;
						geonames_id=geonames[2];
				}
				
				//System.out.println("Comparing:"+toCheck+" with:"+geonames[1]+" "
				//		+ "value:"+toCheck.compareToIgnoreCase(geonames[1]));
				
				/*if(toCheck.compareToIgnoreCase(geonames[1])>0)
					break;*/
				
				if(found)
				{
					Annotation annotation=new Annotation();
					
					annotation.score=1.0f;
					annotation.value=geonames[1];
					annotation.uri="http://sws.geonames.org/"+geonames_id;
					annotation.vocabulary="geonames";
					annotations.add(annotation);
					
					//System.out.println(annotation.toString());
					
					break;
				}
			}
			br.close();
			
			return annotations;
	}

}
