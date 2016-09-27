package com.agroknow.utils;

import java.util.ArrayList;

public class Annotation 
{
	public String value;
	public String uri;
	public float score;
	
	public String arn;
	public String jsonid;
	public String vocabulary; 

	public String toString()
	{
		return "jsonid:"+jsonid+",value:"+value+",URI:"+uri+",score:"+score;
		
	}
	public String toPrint()
	{
		return "";
	}
	
	public boolean exists(ArrayList<Annotation> annotations, int starting_index, String jsonid)
	{
		//System.out.println("WAS CALLED FOR:"+starting_index+" searching:"+this.toString());
		for(int i=starting_index;i<annotations.size();i++)
		{
			if(this.value.equals(annotations.get(i).value)
					&&
				this.jsonid.equals(annotations.get(i).jsonid)
				)
				//jsonid.equals(annotations.get(i).jsonid)
					//&&
				//jsonid.equals(this.jsonid))
			{
				
				return true;
			}
		}
		
		//System.out.println(starting_index+"|"+this.value);
		return false;
	}
}
