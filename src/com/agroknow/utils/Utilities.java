package com.agroknow.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.agroknow.service.Service;

public class Utilities 
{	
	private String[] stop_words = new String[]
				{"a","about","above","across","after","again","against","all","almost","alone",
						"along","already","also","although","always","among","an","and","another",
						"any","anybody","anyone","anything","anywhere","are","area","areas","around",
						"as","ask","asked","asking","asks","at","away","b","back","backed","backing",
						"backs","be","became","because","become","becomes","been","before","began",
						"behind","being","beings","best","better","between","big","both","but","by",
						"c","came","can","cannot","case","cases","certain","certainly","clear","clearly",
						"come","could","d","did","differ","different","differently","do","does","done",
						"down","down","downed","downing","downs","during","e","each","early","either",
						"end","ended","ending","ends","enough","even","evenly","ever","every","everybody",
						"everyone","everything","everywhere","f","face","faces","fact","facts","far","felt",
						"few","find","finds","first","for","four","from","full","fully","further","furthered",
						"furthering","furthers","g","gave","general","generally","get","gets","give","given",
						"gives","go","going","good","goods","got","great","greater","greatest","group","grouped",
						"grouping","groups","h","had","has","have","having","he","her","here","herself","high",
						"high","high","higher","highest","him","himself","his","how","however","i","if",
						"important","in","interest","interested","interesting","interests","into","is","it",
						"its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l",
						"large","largely","last","later","latest","least","less","let","lets","like","likely",
						"long","longer","longest","m","made","make","making","man","many","may","me","member","members",
						"men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary",
						"need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody",
						"non","noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old",
						"older","oldest","on","once","one","only","open","opened","opening","opens","or","order",
						"ordered","ordering","orders","other","others","our","out","over","p","part","parted",
						"parting","parts","per","perhaps","place","places","point","pointed","pointing","points",
						"possible","present","presented","presenting","presents","problem","problems","put","puts",
						"q","quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say",
						"says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall",
						"she","should","show","showed","showing","shows","side","sides","since","small","smaller",
						"smallest","so","some","somebody","someone","something","somewhere","state","states","still",
						"still","such","sure","t","take","taken","than","that","the","their","them","then","there",
						"therefore","these","they","thing","things","think","thinks","this","those","though","thought",
						"thoughts","three","through","thus","to","today","together","too","took","toward","turn",
						"turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses",
						"v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went",
						"were","what","when","where","whether","which","while","who","whole","whose","why","will","with",
						"within","without","work","worked","working","works","would","x","y","year","years","yet","you",
						"young","younger","youngest","your","yours","z"};
	
	public boolean isStopWord(String value)
	{
		if(value.isEmpty() || value.equals("") || value.equals(" "))
			return true;
				
		for(int i=0;i<stop_words.length;i++)
		{
			//if(value.toLowerCase().contains(stop_words[i]))
			//	return true;
			if(stop_words[i].equals(value.toLowerCase()))
				return true;
		}
		return false;
	}
	
	public boolean exists(String input, ArrayList<Annotation> annotations)
	{
		for(int i=0;i<annotations.size();i++)
		{
			if(input.equals(annotations.get(i).value))
				return true;
		}
		return false;
	}
	
	public boolean isWorthRunning(Service service, ArrayList<Annotation> annotations, String input)
	{
		if(!this.exists(input, annotations))
			return true;
		
		for(int i=0;i<annotations.size();i++)
		{
			if(annotations.get(i).value.equals(input)
					&&
				annotations.get(i).uri.contains(service.base_uri))
				return false;
		}
		
		return true;
	}
	
	public boolean alreadyEnriched(String value, ArrayList<Annotation> annotations)
	{
		for(int i=0;i<annotations.size();i++)
		{
			if(annotations.get(i).value.equals(value))
				return true;
		}
		return false;
	}
	
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
