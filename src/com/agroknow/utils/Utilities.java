package com.agroknow.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Utilities 
{	
	public String fetch_uris(ArrayList<Annotation> annotations, String value, String jsonid)
	{
		String uris="";
		
		for(int i=0;i<annotations.size();i++)
		{
			if(annotations.get(i).jsonid.equals(jsonid)
					&& annotations.get(i).value.equals(value))
			{
				if(!uris.contains(annotations.get(i).uri))
				{
					uris+=",\""+annotations.get(i).uri+"\"";
				}
			}
		}
			
			
		return uris;
	}

	public String fetch_vocs(ArrayList<Annotation> annotations, String value, String jsonid)
	{
		String vocs="";
		
		for(int i=0;i<annotations.size();i++)
		{
			if(annotations.get(i).jsonid.equals(jsonid)
					&& annotations.get(i).value.equals(value))
			{
				if(!vocs.contains(annotations.get(i).vocabulary))
				{
					vocs+=",\""+annotations.get(i).vocabulary+"\"";
				}
			}
		}
			
			
		return vocs;
	}

	public float fetch_max_score(ArrayList<Annotation> annotations, String value, String jsonid)
	{
		float score=-1;
		
		for(int i=0;i<annotations.size();i++)
		{
			if(annotations.get(i).jsonid.equals(jsonid)
					&& annotations.get(i).value.equals(value))
			{
				if(score<annotations.get(i).score)
					score=annotations.get(i).score;
			}
		}
			
			
		return score;
	}
	
	
	public ArrayList<Annotation> cleanseAnnotations(ArrayList<Annotation> annotations)
	{
		ArrayList<Annotation> newer = new ArrayList<Annotation>();
		
		for(int i=0;i<annotations.size();i++)
		{
			Annotation a = new Annotation();
			a.value = annotations.get(i).value.toLowerCase();
			a.uri = annotations.get(i).uri;
			a.score = annotations.get(i).score;
			
			a.jsonid = annotations.get(i).jsonid;
			a.arn = annotations.get(i).arn;
			a.vocabulary = annotations.get(i).vocabulary;
			
			newer.add(a);
		}
		
		/*System.out.println("-INIT CASE-");
		for(int i=0;i<annotations.size();i++)
			System.out.println(annotations.get(i));
		System.out.println("---");*/
		
		Collections.sort(newer, new Comparator<Annotation>() {
	        @Override
	        public int compare(Annotation a2, Annotation a1)
	        {
	        		if(a1.jsonid.compareTo(a2.jsonid)<0)
	        			return 1;	        		
	        		else if(a1.jsonid.compareTo(a2.jsonid)>0)
	        			return -1;
	        		else
	        		{
	        			return a1.value.compareTo(a2.value);
	        		}
	        		
	            //return  fruit1.fruitName.compareTo(fruit2.fruitName);
	        }
	    });

		/*System.out.println("-ENDING CASE-");
		for(int i=0;i<annotations.size();i++)
			System.out.println(annotations.get(i));
		System.out.println("---");*/

		return newer;
	}
}
