 /**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.Map;
import java.util.concurrent.Callable;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.AUTHOR;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.CATEGORY;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.LINK;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.TERM;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Nitin
 * A Callable document transformer that converts the given WikipediaDocument object
 * into an IndexableDocument object using the given Tokenizer
 */
public class DocumentTransformer implements Callable<IndexableDocument> {
	Map<INDEXFIELD, Tokenizer> tknizerMap;
        WikipediaDocument doc;
        /**
	 * Default constructor, DO NOT change
	 * @param tknizerMap: A map mapping a fully initialized tokenizer to a given field type
	 * @param doc: The WikipediaDocument to be processed
	 */
	public DocumentTransformer(Map<INDEXFIELD, Tokenizer> tknizerMap, WikipediaDocument doc) {
		this.tknizerMap=tknizerMap;
                this.doc=doc;
	}
	
	/**
	 * Method to trigger the transformation
	 * @throws TokenizerException Inc ase any tokenization error occurs
	 */
	public IndexableDocument call() throws TokenizerException {
        List<String> lstCategories;
        Set<INDEXFIELD> iField = tknizerMap.keySet();
        Iterator it = iField.iterator();
        Tokenizer tkn;
        StringBuilder sb = null;
        TokenStream tknStream;
        INDEXFIELD idx;
        IndexableDocument indxDocument=new IndexableDocument();
        //indxDocument.setTitle(doc.getTitle());
            //.println("Test ID"+indxDocument.getDocumentIdentifier());
        try {
            while (it.hasNext()) {
                idx=(INDEXFIELD) it.next();
                tkn = tknizerMap.get(idx);
                switch (idx) {
                    case CATEGORY:
                        sb=new StringBuilder();
                        lstCategories = doc.getCategories();
                        
                        for (String s : lstCategories) {
                            sb.append(s);
                        }
                        tknStream=new TokenStream(sb);
                        indxDocument.addField(INDEXFIELD.CATEGORY, tknStream);
                        tkn.tokenize(tknStream);
                        break;
                    case AUTHOR:
                        tknStream=new TokenStream(doc.getAuthor());
                        indxDocument.addField(INDEXFIELD.AUTHOR, tknStream);
                        tkn.tokenize(tknStream);
                        break;
                    case TERM:
                        sb=new StringBuilder();
                        for(WikipediaDocument.Section section:doc.getSections())
                        {
                            sb.append(section.getText());
                            sb.append(section.getTitle());
                        }
                        tknStream=new TokenStream(sb);
                        indxDocument.addField(INDEXFIELD.TERM, tknStream);
                        tkn.tokenize(tknStream);
                        break;
                    case LINK:
                        sb=new StringBuilder();
                        //.println("============="+indxDocument.getDocumentIdentifier());
                        //sb.append(indxDocument.getDocumentIdentifier());
                        sb.append(doc.getLinks());
                        tknStream=new TokenStream(sb);
                        indxDocument.addField(INDEXFIELD.LINK, tknStream);
                        tkn.tokenize(tknStream);
                        break;
                    default:
                        break;
                }
                
            }
            
        } 
        catch (TokenizerException ex) {
            System.err.println("Error in DocumentTransformer" + ex.getMessage());
        } 
        return indxDocument ;
	}
	
}
