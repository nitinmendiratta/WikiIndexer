/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.sax;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    private String tmpValue;
    private String docXmlFileName;
    private WikipediaDocument objWikipediaDoc;
    private WikipediaParser objWikipediaParser;
    private Collection<WikipediaDocument> docs;
    private List<WikipediaDocument> lstDocument;
    int txtIdentifier = 0;
    int idIdentifier = 0;
    String title = "";
    String txtPage = "";
    int id = 0;
    String author = "";
    String timestamp = "";

    public SAXParser_Impl(String docXmlFileName, Collection<WikipediaDocument> docs) {
        this.docXmlFileName = docXmlFileName;
        this.docs = docs;
    }

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

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        // .println("startElement");
        if (elementName.equalsIgnoreCase("page")) {
            objWikipediaDoc = null;
            objWikipediaParser = null;
            id = 0;
        }
        if (elementName.equalsIgnoreCase("text")) {
            txtIdentifier = 1;
        }
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
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
