/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Nitin
 */
public class SAXParser_Impl extends DefaultHandler {

    
    private String docXmlFileName;
    private WikipediaDocument objWikipediaDoc;
    private WikipediaParser objWikipediaParser;
    private List<WikipediaDocument> lstDocument;
    int txtIdentifier = 0;
    private String tmpValue;

    public SAXParser_Impl(String docXmlFileName,Properties props) {
        this.docXmlFileName = docXmlFileName;
       
    }
    /**
    *
    * This method calls the SAX parser event handler and parses the XML document.
    * return lstDocument-> list of documents within xml file
    */
    public List<WikipediaDocument> parseDocument() {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            lstDocument = new ArrayList();
            SAXParser parser = factory.newSAXParser();
            parser.parse(docXmlFileName, this);
        } catch (ParserConfigurationException e) {
            System.out.println("Error in SAXParser.parseDocument:ParserConfig error");
        } catch (SAXException e) {
            System.out.println("Error in SAXParser.parseDocument-SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("Error in SAXParser.parseDocument-IO error");
        }
        return lstDocument;
    }
    
    /**
    *
    * It is an event handler and invoked implicitly  whenever a start node is encountered.
    */
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        
        if (elementName.equalsIgnoreCase("page")) {
            objWikipediaDoc = null;
            objWikipediaParser = null;
            
        }
        if (elementName.equalsIgnoreCase("text")) {
            txtIdentifier = 1;
        }
    }
    /**
    *
    * It is an event handler and invoked implicitly  whenever a end node is encountered.
    * For each page(node) within XMl file, a WikipediaDocument object is created.
    */
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
    	 
    	    String title = "";
    	    String txtPage = "";
    	    int id = 0;
    	    String author = "";
    	    String timestamp = "";
        switch (element.toLowerCase()) {
            case "page":
                try {
                    objWikipediaParser = new WikipediaParser();
                    objWikipediaDoc = objWikipediaParser.getDocumentInstance(id, timestamp, author, title, txtPage);
                } catch (ParseException e) {
                    System.err.println("Error in endElement");
                    System.err.println(e.toString());
                }
                try {
                    lstDocument.add(objWikipediaDoc);
                } catch (NullPointerException ex) {
                    System.err.println("Error in endElement");
                    System.err.println(ex.toString());
                }
                break;
            case "title":
                title = tmpValue;
                break;
            case "ip":
                if (author.equals("")) {
                    author = tmpValue;
                }
                break;
            case "username":
                author = tmpValue;
                break;
            case "id":
                if (id == 0) {
                    id = (tmpValue != null) ? Integer.parseInt(tmpValue) : 0;
                }
                break;
            case "timestamp":
                timestamp = tmpValue;
                break;
            case "text":
                txtPage = tmpValue;
                txtIdentifier = 0;
                break;
        }

    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {

        if (txtIdentifier == 1) {
            tmpValue = tmpValue + new String(ac, i, j);
        } else {
            tmpValue = new String(ac, i, j);
        }
    }
}
