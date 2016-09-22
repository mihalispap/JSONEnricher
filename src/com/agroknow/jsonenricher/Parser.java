package com.agroknow.jsonenricher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openrdf.model.Value;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;

import com.agroknow.enricher.AGROVOCEnricher;
import com.agroknow.enricher.Enricher;
import com.agroknow.enricher.LocationEnricher;
import com.agroknow.utils.AKJSONWriter;
import com.agroknow.utils.Annotation;

public class Parser {
 
	public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	
	static String output;
	static String folder_path; 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub


        if (args.length <= 3) {
            System.err.println("Usage: -e:{enrichment_methods} -s:{services_to_use} "
            		+ "-i:{path_to_input_dir} -o:{path_to_output_dir}");                
            System.exit(1);
        }
        
        for(int i=0;i<args.length;i++)
        {
        	if(args[i].startsWith("-e"))
        		handle_enrichments(args[i]);
        	else if(args[i].startsWith("-s"))
        		handle_services(args[i]);
        	else if(args[i].startsWith("-i"))
        		handle_input(args[i]);
        	else if(args[i].startsWith("-o"))
        		handle_output(args[i]);
        }
        
        //String output=args[1];
        File file = new File(output);
		file.mkdirs();
        
        //String folder_path=args[0];
        
		System.out.println("I am the folder path:"+folder_path);
		
        File folder = new File(folder_path);
        //File[] listOfFiles = folder.listFiles();

        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            } 
        });
        
        //folder.list
        
        for (int i = 0; i < listOfFiles.length; i++) 
        {
        	if (listOfFiles[i].isFile()) 
        	{
        		System.out.println("File " + listOfFiles[i].getName());
        	} 
        	else if (listOfFiles[i].isDirectory()) 
        	{
        		System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
       
        for(int i=0;i<listOfFiles.length;i++)
        {
        	if(listOfFiles[i].getName().endsWith(".json"))
        		enrich(listOfFiles[i].getAbsolutePath(), output, listOfFiles[i].getName());
        }
	}
	
	static void enrich(String filename, String output_folder, String init_fname)
	{
		ArrayList<Enricher> enrichers=new ArrayList<Enricher>();
				
		for(int i=0;i<enrichments.size();i++)
		{
			if(enrichments.get(i).equals("location"))
			{
				LocationEnricher loc_enrich=new LocationEnricher();		
				enrichers.add(loc_enrich);				
			}
			if(enrichments.get(i).equals("agrovoc"))
			{
				AGROVOCEnricher ag_enrich=new AGROVOCEnricher();		
				enrichers.add(ag_enrich);
			}
		}
		
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
		
		for(int i=0;i<enrichers.size();i++)
		{
			annotations.addAll(enrichers.get(i).enrich(filename));
		}
		
		/*Need writer class*/
		
		AKJSONWriter writer=new AKJSONWriter();
		
		try 
		{
			writer.writeCore(output_folder, filename);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


			try {
				writer.writeAnnotations(annotations, output_folder);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//System.out.println(annotations.get(i).toString());
				
		
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

	static ArrayList<String> enrichments=new ArrayList<String>();
	private static void handle_enrichments(String input)
	{
		String[] values=input.replace("-e:", "").split(",");
		
		for(int i=0;i<values.length;i++)
			enrichments.add(values[i]);
	}
	
	static ArrayList<String> services=new ArrayList<String>();
	private static void handle_services(String input)
	{
		String[] values=input.replace("-s:", "").split(",");
		
		for(int i=0;i<values.length;i++)
			services.add(values[i]);
	}

	private static void handle_input(String input)
	{
		System.out.println(input);
		folder_path=input.replace("-i:", "");
	}
	
	private static void handle_output(String input)
	{
		output=input.replace("-o:", "");
	}
	
	
	
	
	

}










