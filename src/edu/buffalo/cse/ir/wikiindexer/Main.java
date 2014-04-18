/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.indexer.IndexReader;
import edu.buffalo.cse.ir.wikiindexer.indexer.SharedDictionary;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Nitin
 */
public class Main {
    public static void main(String[] args) {    
        try
        {
        	Map<String, Integer> map=null;
            Collection<String> strQuery=new ArrayList<>();
            Properties properties = FileUtil.loadProperties(FileUtil.fileName());
            IndexReader obj=new IndexReader(properties, INDEXFIELD.TERM);
            if(null!=args[0])
            {
            	map=obj.getPostings(args[0]);
            }
            if(map!=null)System.out.println(map);
        }
    	catch(IOException ex)
    	{
    		System.err.println("IOError in main method. Message:"+ex.getMessage());
    	}
        catch(Exception ex)
        {
        	System.err.println("Error in main method. Message:"+ex.getMessage());
        }
    }
    
}
