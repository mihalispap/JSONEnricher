package com.agroknow.service;

import java.util.ArrayList;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;

import com.agroknow.utils.Annotation;

public class AKSPARQL_FAOGeo extends Service {

	public AKSPARQL_FAOGeo() {
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
				String repositoryId="faogeopolitical";
				String ApiKey = "";
				String ApiPass = "";
				String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
						+ "select * "
						+ "where "
						//+ " { ?s <http://www.fao.org/countryprofiles/geoinfo/geopolitical/resource/nameShort> \""+toCheck+"\"@"+language+" . } "
						+ " { ?s <http://www.fao.org/countryprofiles/geoinfo/geopolitical/resource/nameShort> "
						+ "			?name. "
						+ "FILTER (lcase(str(?name)) = \""+input+"\")  } "
								+ "limit 1";
				
				//System.out.println("Will run query:"+queryString);
				
				//System.exit(1);
				
				// Abstract representation of a remote repository accessible over HTTP
		        HTTPRepository repository = new HTTPRepository("http://lod.akstem.com/repositories/faogeopolitical");
		        
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
					annotation.vocabulary="faogeopolitical";
					
					annotations.add(annotation);
					
					//System.out.println("I found:"+annotation.toString());
					System.out.println("AKSPARQL_FAOGEO");
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
				annotations.get(i).jsonid="location";
			return annotations;
	}
	
	
}
