package com.agroknow.utils;

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
		return "ARN:"+arn+",value:"+value+",URI:"+uri+",score:"+score;
		
	}
	public String toPrint()
	{
		return "";
	} 
}
