package com.agroknow.service;

import java.util.ArrayList;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;

import com.agroknow.utils.Annotation;

public class AKSPARQL_GACS extends Service {

	public AKSPARQL_GACS() {
		// TODO Auto-generated constructor stub
		this.started_on=System.currentTimeMillis()/1000;
		this.name="AK GACS";
		
		this.base_uri="http://id.agrisemantics.org/";
	}
	

	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		//input=input.toLowerCase();
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
				
			String toCheck;
			
			toCheck=input.replace(",", "");
			toCheck=toCheck.replace("(", "");
			toCheck=toCheck.replace(")", "");
			toCheck=toCheck.replace("-", "");
			toCheck=toCheck.replace("{", "");
			toCheck=toCheck.replace("}", "");
			toCheck=toCheck.replace("/", "");

			toCheck=toCheck.replace("  ", " ");
			
			if(toCheck.startsWith(" "))
				toCheck=toCheck.replaceFirst(" ", "");
			if(toCheck.endsWith(" "))
			{
				StringBuilder b = new StringBuilder(toCheck);
				b.replace(toCheck.lastIndexOf(" "), toCheck.lastIndexOf(" ") + 1, "" );
				toCheck = b.toString();
			}
			String queryString ="";
			try
			{
				String dbaasURL = "http://lod.akstem.com";///repositories/agris";
				String repositoryId="gacs";
				String ApiKey = "";
				String ApiPass = "";
				
				/*String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
						+ "select * "
						+ "where "
						+ " { ?s skos:prefLabel ?label."
						+ "FILTER (lcase(str(?label)) = \""+input+"\")  } "
								+ "limit 1";*/
				queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
						+ "select * "
						+ "where "
						+ " { ?s skos:prefLabel \""+input.toLowerCase()+"\"@"+language+"."
								+ "?s skos:exactMatch ?o. } "
								+ " ";
				
				//System.out.println("Will run query:"+queryString);
								
				// Abstract representation of a remote repository accessible over HTTP
		        HTTPRepository repository = new HTTPRepository("http://lod.akstem.com/repositories/gacs");
		        
		        // Separate connection to a repository
		        RepositoryConnection connection = repository.getConnection();
				
		        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
						queryString);
		
				TupleQueryResult result = tupleQuery.evaluate();
		
				if(!result.hasNext())
				{				
					
					queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
							+ "select * "
							+ "where "
							+ " { ?s skos:prefLabel \""+input.toLowerCase()+"\"@"+language+"."
									+ "?s skos:exactMatch ?o. } "
									+ " ";
					
			        tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
							queryString);
			
					result = tupleQuery.evaluate();
				}
				
				while (result.hasNext()) { // iterate over the result
					BindingSet bindingSet = result.next();
					//System.out.println(bindingSet.getBinding("s").getValue());
					
					Annotation annotation=new Annotation();
					annotation.value=toCheck;
					annotation.score=1.0f;
					annotation.uri=bindingSet.getBinding("o").getValue().stringValue();
					
					if(annotation.uri.contains("agrisemantics.org/gacs"))
						annotation.vocabulary="gacs";
					else if(annotation.uri.contains("nal.usda.gov/nalt"))
						annotation.vocabulary="nalt";
					else if(annotation.uri.contains("cabi.org/cabt"))
						annotation.vocabulary="cabt";
					
					
					annotations.add(annotation);
					
					//System.out.println("I found:"+annotation.toString());
					
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
