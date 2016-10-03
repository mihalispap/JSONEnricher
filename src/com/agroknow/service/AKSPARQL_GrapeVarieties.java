package com.agroknow.service;

import java.util.ArrayList;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;

import com.agroknow.utils.Annotation;

public class AKSPARQL_GrapeVarieties extends Service {

	public AKSPARQL_GrapeVarieties() {
		// TODO Auto-generated constructor stub

		this.started_on=System.currentTimeMillis()/1000;
		this.name="AK GV";
		
		this.base_uri="http://vitis.agroknow.com/";
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
			
			try
			{
				String dbaasURL = "http://lod.akstem.com";///repositories/agris";
				String repositoryId="grape_varieties";
				String ApiKey = "";
				String ApiPass = "";
				String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
						+ "select * "
						+ "where "
						+ " { ?s rdfs:label ?label."
						+ "FILTER (lcase(str(?label)) = \""+input+"\")  }"
						+ "limit 1";
				
				//System.out.println("Will run query:"+queryString);
				
				// Abstract representation of a remote repository accessible over HTTP
		        HTTPRepository repository = new HTTPRepository("http://lod.akstem.com/repositories/grape_varieties");
		        
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
					annotation.vocabulary="grape_varieties";
					
					annotations.add(annotation);
					
					// do something interesting with the values here...
					//System.out.println("I found:"+annotation.toString()+"!!!");
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
