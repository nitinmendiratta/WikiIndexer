/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nitin
 * A simple map based token view of the transformed document
 *
 */
public class IndexableDocument {

    Map<INDEXFIELD, TokenStream> mpIndex;
    private static int i32Sequence=0;
    
    String id;

    /**
     * Default constructor
     */
    public IndexableDocument() {
        mpIndex = new HashMap();
        
            ++i32Sequence;
            id=Integer.toString(i32Sequence);
        //System.out.println("IDOC Created:"+id+"-"+ i32Sequence);
    }

    /**
     * MEthod to add a field and stream to the map If the field already exists
     * in the map, the streams should be merged
     *
     * @param field: The field to be added
     * @param stream: The stream to be added.
     */
    public void addField(INDEXFIELD field, TokenStream stream) {
        if (mpIndex.containsKey(field)) {
            TokenStream strm = mpIndex.get(field);
            strm.merge(stream);
            mpIndex.put(field, strm);
        } else {
            mpIndex.put(field, stream);
        }
        //System.out.println(field+" :"+ mpIndex);
       
    }

    /**
     * Method to return the stream for a given field
     *
     * @param key: The field for which the stream is requested
     * @return The underlying stream if the key exists, null otherwise
     */
    public TokenStream getStream(INDEXFIELD key) {
        return mpIndex.get(key);
    }

    /**
     * Method to return a unique identifier for the given document. It is left
     * to the student to identify what this must be But also look at how it is
     * referenced in the indexing process
     *
     * @return A unique identifier for the given document
     */
    public String getDocumentIdentifier() {
       
        return "doc"+id;

    }
    
}
