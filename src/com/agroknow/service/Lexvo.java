package com.agroknow.service;

import java.util.ArrayList;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;

import com.agroknow.utils.Annotation;

public class Lexvo extends Service {

	public Lexvo() {
		// TODO Auto-generated constructor stub
		this.started_on=System.currentTimeMillis()/1000;
		this.name="Lexvo";
	}
	
	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		input=input.toLowerCase();
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
				
			String base="";
			if(language.length()==3)
				base="http://lexvo.org/id/iso639-3/";
			if(language.length()==2)
				base="http://lexvo.org/id/code/";
			
			if(base.isEmpty())
				return annotations;
			
			Annotation annotation=new Annotation();
			annotation.value=language;
			annotation.score=1.0f;
			annotation.uri=base+language;
			annotation.vocabulary="lexvo";
			
			annotations.add(annotation);

			for(int i=0;i<annotations.size();i++)
				annotations.get(i).jsonid="language";
			return annotations;
	}
	
	

}
