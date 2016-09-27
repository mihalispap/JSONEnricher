package com.agroknow.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;

import com.agroknow.utils.Annotation;
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

public class AKSPARQL_AGROVOC extends Service 
{ 

	public AKSPARQL_AGROVOC() {
		// TODO Auto-generated constructor stub 
	}

	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
				
			String toCheck;
			
			toCheck=input.replace(",", "");
			toCheck=toCheck.replace("(", "");
			toCheck=toCheck.replace(")", "");
			toCheck=toCheck.replace("-", "");
			toCheck=toCheck.replace("{", "");
			toCheck=toCheck.replace("}", "");
			toCheck=toCheck.replace(" ", "");
			toCheck=toCheck.replace("/", "");
			
			try
			{
				String dbaasURL = "http://lod.akstem.com";///repositories/agris";
				String repositoryId="agrovoc";
				String ApiKey = "";
				String ApiPass = "";
				String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
						+ "select * "
						+ "where "
						//+ " { ?s skos:prefLabel \""+toCheck+"\"@"+language+" . } "
						+ " { ?s skos:prefLabel ?name . "
						+ "FILTER (lcase(str(?name)) = \""+input+"\")  } "
								+ "limit 500";
				
				//System.out.println("Will run query:"+queryString);
				
				// Abstract representation of a remote repository accessible over HTTP
		        HTTPRepository repository = new HTTPRepository("http://lod.akstem.com/repositories/agrovoc");
		        
		        // Separate connection to a repository
		        RepositoryConnection connection = repository.getConnection();
				
		        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
						queryString);
		
				TupleQueryResult result = tupleQuery.evaluate();
		
				while (result.hasNext()) { // iterate over the result
					BindingSet bindingSet = result.next();
					//System.out.println(bindingSet.getBinding("s").getValue());
					
					Annotation annotation=new Annotation();
					annotation.value=toCheck;
					annotation.score=1.0f;
					annotation.uri=bindingSet.getBinding("s").getValue().stringValue();
					annotation.vocabulary="agrovoc";
					
					annotations.add(annotation);
					System.out.println("AKSPARQL_AGROVOC");
					// do something interesting with the values here...
				}
		
				result.close();
		
				connection.close();
				
			}
			catch(java.lang.Exception e)
			{
				e.printStackTrace();
			}

			for(int i=0;i<annotations.size();i++)
				annotations.get(i).jsonid="subject";
			return annotations;
	}
	
}

















