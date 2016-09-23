package com.agroknow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.openrdf.query.BindingSet;

import javax.net.ssl.*;

import com.agroknow.utils.Annotation;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class FremeGeonames extends Service {

	public FremeGeonames() { 
		// TODO Auto-generated constructor stub
		
	}

	public ArrayList<Annotation> run(String input, String language) throws Exception
	{
		if(input.isEmpty() || input.equals(" "))
			return null;
		
		/*
	     *  fix for
	     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
	     *       sun.security.validator.ValidatorException:
	     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
	     *               unable to find valid certification path to requested target
	     */
	    TrustManager[] trustAllCerts = new TrustManager[] {
	       new X509TrustManager() {
	          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	            return null;
	          }

	          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

	          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

			public void checkClientTrusted1(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			public void checkServerTrusted1(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub
				
			}

	       }
	    };

	    SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    // Create all-trusting host name verifier
	    HostnameVerifier allHostsValid = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	          return true;
	        }
	    };
	    // Install the all-trusting host verifier
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    /*
	     * end of the fix
	     */
		
		
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
		
		String uri = "https://api-dev.freme-project.eu/current/pipelining/chain";
	
		//System.out.print("test\n");
		URL url = new URL(uri);
		//logger.info("Calling FREME e-terminology: "+uri);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		//connection.setRequestProperty("Content-Type", "application/ld+json;charset=UTF-8");
		//connection.setRequestProperty("Content-Type", "text/n3;charset=UTF-8");
		
		connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
		String query="[ {   \"method\": \"POST\",   \"endpoint\": \"https://api.freme-project.eu/current/e-entity/freme-ner/documents\",   \"parameters\": {     \"language\": \"en\" ,  \"dataset\": \"dbpedia\"  },   \"headers\": {     \"content-type\": \"text/plain\",     \"accept\": \"text/turtle\"   },   \"body\": \""+input+"\" }, {   \"method\": \"POST\",   \"endpoint\": \"https://api.freme-project.eu/current/e-link/documents/\",   \"parameters\": {     \"templateid\": \"3\"   },   \"headers\": {     \"content-type\": \"text/turtle\"   } } ]";
		
		if (query != null) 
		{
			try
			{
				connection.setRequestProperty("Content-Length", Integer.toString(query.length()));
				connection.getOutputStream().write(query.getBytes("UTF8"));
				//System.out.println("i got in here");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		BufferedReader streamReader;
		try {
			streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Input was:"+input);
			e1.printStackTrace();
			return null;
		} 
		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		try {
			while ((inputStr = streamReader.readLine()) != null)
			    responseStrBuilder.append(inputStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	    int responseCode;
		try {
			responseCode = connection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	   	try
	   	{
	   		Model model = ModelFactory.createDefaultModel()
	   		        .read(IOUtils.toInputStream(responseStrBuilder.toString(), "UTF-8"), null, "TURTLE");
	   		    
	   		 
	   		String getItemsLists = "" +
	                "prefix xsd:   <http://www.w3.org/2001/XMLSchema#>\n"+
	                "prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#>\n"+
	                "prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#>\n" +
	                "\n" +
	                "select ?s ?ref ?cs ?value where {\n" +
	                "  ?s itsrdf:taClassRef <http://dbpedia.org/ontology/Location>.\n"
	                + "?s itsrdf:taConfidence ?cs.\n"
	                + "?s itsrdf:taIdentRef ?ref.\n"
	                + "?s nif:anchorOf ?value."
	                + "FILTER (?cs > 0.6)"
	                + 
	                "}";
	        ResultSet results = QueryExecutionFactory.create( getItemsLists, model ).execSelect();
	        while ( results.hasNext() ) {
	        		        	
	        	QuerySolution qs = results.next();
	        	//System.out.println(qs.g);
	        	//System.out.println(qs.getLiteral("cs").getFloat());
	        	
	        	Annotation annotation = new Annotation();	        	
	        	annotation.score=qs.getLiteral("cs").getFloat();
	        	annotation.vocabulary="dbpedia";
	        	annotation.uri=qs.getResource("ref").toString();
	        	annotation.value=qs.getLiteral("value").getString();	        	
	        	annotations.add(annotation);
	        	
	        	String dbpedia_sparql=""
	        			+ "prefix owl: <http://www.w3.org/2002/07/owl#>"
	        			+ "select ?geouri where {<"+annotation.uri+"> owl:sameAs ?geouri."
	        			+ "filter( regex(str(?geouri), \"http://sws.geonames.org\" ))"
	        					+ "} "
	        			+ "limit 5";
	        	QueryExecution qExe = QueryExecutionFactory
	        			.sparqlService( "http://dbpedia.org/sparql", dbpedia_sparql );
	        	ResultSet results_dbpedia = qExe.execSelect();
	        	while(results_dbpedia.hasNext())
	    	    {
	    	        QuerySolution sol = results_dbpedia.nextSolution();
	    	        RDFNode str = sol.get("geouri"); 

	    	        Annotation annotation_n = new Annotation();	        	
	    	        annotation_n.score=qs.getLiteral("cs").getFloat();
	    	        annotation_n.vocabulary="geonames";
	    	        annotation_n.uri=str.toString();
	    	        annotation_n.value=qs.getLiteral("value").getString();	        	
		        	annotations.add(annotation_n);
	    	        
	    	        System.out.println(annotation_n.toString());
	    	    }
	        	//System.out.println(annotation.toString());
	        	//System.out.println(qs.getResource("ref").toString());
	        }
	   		
	   	}
	   	catch(Exception e){e.printStackTrace();}
	    
		return annotations;
	}
	
}
