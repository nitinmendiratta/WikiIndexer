/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import edu.buffalo.cse.ir.sax.SAXParser_Impl;
import edu.buffalo.cse.ir.wikiindexer.FileUtil;
import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;

import java.util.Collection;
import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;

import java.util.Iterator;
import java.util.List;

/**
 * @author Nitin
 *
 */
public class Parser {
    /* */

    private final Properties props;

    /**
     *
     * @param idxConfig
     * @param parser
     */
    public Parser(Properties idxProps) {
        props = idxProps;
    }

    /* TODO: Implement this method */
    /**
     *
     * @param filename
     * @param docs
     */
    public void parse(String filename, Collection<WikipediaDocument> docs) {
        try {
           
                if(filename==null)
                {
                	filename=FileUtil.xmlFileName();
                }
                SAXParser_Impl objSAXParser = new SAXParser_Impl(filename, docs);
                List<WikipediaDocument> lstDocument = objSAXParser.parseDocument();
                Iterator it = lstDocument.iterator();
                while (it.hasNext()) {
                    add((WikipediaDocument) it.next(), docs);
                }
                
            
        } catch (Exception e) {
            
             System.err.println("Error in parse() method in Parser.java.Message:" + e.getMessage());
        }


    }

    /**
     * Method to add the given document to the collection. PLEASE USE THIS
     * METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS For better
     * performance, add the document to the collection only after you have
     * completely populated it, i.e., parsing is complete for that document.
     *
     * @param doc: The WikipediaDocument to be added
     * @param documents: The collection of WikipediaDocuments to be added to
     */
    private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
        documents.add(doc);
    }
}
