/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author Nitin 
 * This class is used to write an index to the disk
 *
 */
public class IndexWriter implements Writeable {

    INDEXFIELD idx = null;
    Map<String, Integer> mapDictionary = null;
    Map<String, Integer> mapDocTermFrequency = null;
    Map<String, Map<String, Integer>> mapIndex = new HashMap();
    Properties props;
    int i32PartitionNum;

    /**
     * Constructor that assumes the underlying index is inverted Every index
     * (inverted or forward), has a key field and the value field The key field
     * is the field on which the postings are aggregated The value field is the
     * field whose postings we are accumulating For term index for example: Key:
     * Term (or term id) - referenced by TERM INDEXFIELD Value: Document (or
     * document id) - referenced by LINK INDEXFIELD
     *
     * @param props: The Properties file
     * @param keyField: The index field that is the key for this index
     * @param valueField: The index field that is the value for this index
     */
    public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField) {
        this(props, keyField, valueField, false);
    }

    /**
     * Overloaded constructor that allows specifying the index type as inverted
     * or forward Every index (inverted or forward), has a key field and the
     * value field The key field is the field on which the postings are
     * aggregated The value field is the field whose postings we are
     * accumulating For term index for example: Key: Term (or term id) -
     * referenced by TERM INDEXFIELD Value: Document (or document id) -
     * referenced by LINK INDEXFIELD
     *
     * @param props: The Properties file
     * @param keyField: The index field that is the key for this index
     * @param valueField: The index field that is the value for this index
     * @param isForward: true if the index is a forward index, false if inverted
     */
    public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField, boolean isForward) {
        //TODO: Implement this method
        //Map<IndexField,Map<TermId,List<docId>>>key
        this.props = props;
        idx = keyField;
        //mapDictionary = new HashMap();
        mapDocTermFrequency = new HashMap();
        mapIndex = new HashMap<>();

    }

    /**
     * Method to make the writer self aware of the current partition it is
     * handling Applicable only for distributed indexes.
     *
     * @param pnum: The partition number
     */
    public void setPartitionNumber(int pnum) {
        //TODO: Optionally implement this method
        this.i32PartitionNum = pnum;
    }

    /**
     * Method to add a given key - value mapping to the index
     *
     * @param keyId: The id for the key field, pre-converted
     * @param valueId: The id for the value field, pre-converted
     * @param numOccurances: Number of times the value field is referenced by
     * the key field. Ignore if a forward index
     * @throws IndexerException: If any exception occurs while indexing
     */
    public void addToIndex(int keyId, int valueId, int numOccurances) throws IndexerException {
        //Lookup both----for link/document
        //keyId=docId
        //valueId=termId
        //.println(idx.toString()+"Rahul:="+keyId+"="+valueId+"="+numOccurances);
        try {
            //mapDocTermFrequency.put(String.valueOf(keyId), numOccurances);
            //mapIndex.put(Integer.toString(valueId), mapDocTermFrequency);
            
            if(mapIndex.containsKey(Integer.toString(keyId)))
            {
                mapDocTermFrequency=mapIndex.get(Integer.toString(keyId));
                mapDocTermFrequency.put(Integer.toString(valueId), numOccurances);
                mapIndex.put(Integer.toString(keyId), mapDocTermFrequency);
            }
            else
            {
                mapDocTermFrequency=new HashMap();
                mapDocTermFrequency.put(Integer.toString(valueId), numOccurances);
                mapIndex.put(Integer.toString(keyId), mapDocTermFrequency);
            }
            
            
            
            //.println(mapIndex);
        } catch (Exception e) {
            System.err.println("Error in addToIndex(int keyId, int valueId, int numOccurances).MEssage:" + e.getMessage());
        }

    }

    /**
     * Method to add a given key - value mapping to the index
     *
     * @param keyId: The id for the key field, pre-converted
     * @param value: The value for the value field
     * @param numOccurances: Number of times the value field is referenced by
     * the key field. Ignore if a forward index
     * @throws IndexerException: If any exception occurs while indexing
     */
    public void addToIndex(int keyId, String value, int numOccurances) throws IndexerException {
        //TODO: Implement this method---keyId==docId
        //.println(idx.toString()+"Rahul:="+keyId+"="+value+"="+numOccurances);
        try {
            //mapDocTermFrequency.put(String.valueOf(keyId), numOccurances);
            //mapIndex.put(value, mapDocTermFrequency);
            
            if(mapIndex.containsKey(Integer.toString(keyId)))
            {
                mapDocTermFrequency=mapIndex.get(Integer.toString(keyId));
                mapDocTermFrequency.put(value, numOccurances);
                mapIndex.put(Integer.toString(keyId), mapDocTermFrequency);
            }
            else
            {
                mapDocTermFrequency=new HashMap();
                mapDocTermFrequency.put(value, numOccurances);
                mapIndex.put(Integer.toString(keyId), mapDocTermFrequency);
            }
            
            
        } catch (Exception e) {
            System.err.println("Error in addToIndex(int keyId, String value, int numOccurances).MEssage:" + e.getMessage());
        }
    }

    /**
     * Method to add a given key - value mapping to the index
     *
     * @param key: The key for the key field
     * @param valueId: The id for the value field, pre-converted
     * @param numOccurances: Number of times the value field is referenced by
     * the key field. Ignore if a forward index
     * @throws IndexerException: If any exception occurs while indexing
     */
    public void addToIndex(String key, int valueId, int numOccurances) throws IndexerException {
        //TODO: Implement this method
        //key=term
        //valueID= dicId
        //.println(key+"="+valueId+"="+numOccurances);
        //.println(idx.toString()+":="+key+"="+valueId+"="+numOccurances);
        //Map<String, Integer> mapTemp=new HashMap<>();
        try {
            if(mapIndex.containsKey(key))
            {
                mapDocTermFrequency=mapIndex.get(key);
                mapDocTermFrequency.put(Integer.toString(valueId), numOccurances);
                mapIndex.put(key, mapDocTermFrequency);
            }
            else
            {
                mapDocTermFrequency=new HashMap();
                mapDocTermFrequency.put(Integer.toString(valueId), numOccurances);
                mapIndex.put(key, mapDocTermFrequency);
            }
            
        } catch (Exception e) {
            System.err.println("Error in addToIndex(String key, int valueId, int numOccurances).MEssage:" + e.getMessage());
        }

    }

    /**
     * Method to add a given key - value mapping to the index
     *
     * @param key: The key for the key field
     * @param value: The value for the value field
     * @param numOccurances: Number of times the value field is referenced by
     * the key field. Ignore if a forward index
     * @throws IndexerException: If any exception occurs while indexing
     */
    public void addToIndex(String key, String value, int numOccurances) throws IndexerException {
        //TODO: Implement this method
        //.println(idx.toString()+":="+key+"="+value+"="+numOccurances);
        //mapDocTermFrequency.put(Integer.toString(valueId), numOccurances);
        //mapIndex.put(key, mapDocTermFrequency);
        
        try {
            if(mapIndex.containsKey(key))
            {
                mapDocTermFrequency=mapIndex.get(key);
                mapDocTermFrequency.put(value, numOccurances);
                mapIndex.put(key, mapDocTermFrequency);
            }
            else
            {
                mapDocTermFrequency=new HashMap();
                mapDocTermFrequency.put(value, numOccurances);
                mapIndex.put(key, mapDocTermFrequency);
            }
            
        } catch (Exception e) {
            System.err.println("Error in addToIndex(String key, int valueId, int numOccurances).MEssage:" + e.getMessage());
        }
        
    }

    /* (non-Javadoc)
     * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
     */
    public void writeToDisk() throws IndexerException {
        // TODO Implement this method
        //.println("INdexing start:" + idx+":"+mapIndex);
    	int count=0;
        try {
            //test();
        	
            File file = new File(props.getProperty(IndexerConstants.TEMP_DIR), idx.toString() + "_" + this.i32PartitionNum + ".txt");
            file.getParentFile().mkdirs();
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(mapIndex);
            Iterator it = mapIndex.entrySet().iterator();
            System.out.println("Index Output in format 'token= {document id= frequency of token}'");
            while (it.hasNext()) {
            	if(count>10) break;
                Map.Entry pairs = (Map.Entry)it.next();
                System.out.println(pairs.getKey() + " = " + pairs.getValue());
                it.remove(); // avoids a ConcurrentModificationException
                count++;
            }
           
        } catch (FileNotFoundException ex) {
            System.err.println("Error in writeToDisk(). Message:" + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error in writeToDisk(). Message:" + ex.getMessage());
        }


    }

    /* (non-Javadoc)
     * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
     */
    public void cleanUp() {
        // TODO Implement this method
    }

    public void test() {
        Iterator it = mapIndex.keySet().iterator();
        String strKey = "";
        while (it.hasNext()) {
            strKey = (String) it.next();
        }
    }

    public void createDictionary(String value) {
        int i32UniqueId = value.hashCode();
        mapDictionary.put(value, i32UniqueId);
    }
}
