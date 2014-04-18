/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Nitin 
 * This class is used to introspect a given index 
 * The expectation is the class should be able to read the index and all associated dictionaries.
 */
public class IndexReader {

    INDEXFIELD idx;
    Properties prop;
    Map<String, Integer> mapDocTermFrequency = null;
    Map<String, Map<String, Integer>> mapIndex = null;

    /**
     * Constructor to create an instance
     *
     * @param props: The properties file
     * @param field: The index field whose index is to be read
     */
    public IndexReader(Properties props, INDEXFIELD field) {
        //TODO: Implement this method
        this.idx = field;
        this.prop = props;
    }

    /**
     * Method to get the total number of terms in the key dictionary
     *
     * @return The total number of terms as above
     */
    public int getTotalKeyTerms() throws IOException, ClassNotFoundException {
        //TODO: Implement this method
        mapDocTermFrequency = new HashMap<>();
        mapIndex = new HashMap<>();
        int count = 0;
        switch (idx.toString().toUpperCase()) {
            case "TERM":
                for (int i = 0; i < Partitioner.getNumPartitions(); i++) {
                    mapIndex = readFromDisk(i);
                    count = count + mapIndex.keySet().size();
                }
                break;
            default:
                mapIndex = readFromDisk(0);
                count = count + mapIndex.keySet().size();
                break;
        }

        return count;
    }

    /**
     * Method to get the total number of terms in the value dictionary
     *
     * @return The total number of terms as above
     */
    public int getTotalValueTerms() {
        //TODO: Implement this method
        mapDocTermFrequency = new HashMap<>();
        mapIndex = new HashMap<>();
        int count = 0;
        switch (idx.toString().toUpperCase()) {
            case "TERM":
                for (int i = 0; i < Partitioner.getNumPartitions(); i++) {
                    mapIndex = readFromDisk(i);
                    for(String strTerm: mapIndex.keySet()){
                        count=count + mapIndex.get(strTerm).size();
                    }
                }
                break;
            default:
                mapIndex = readFromDisk(0);
                for(String strTerm: mapIndex.keySet()){
                        count=count + mapIndex.get(strTerm).size();
                    }
                break;
        }

        return count;
    }

    /**
     * Method to retrieve the postings list for a given dictionary term
     *
     * @param key: The dictionary term to be queried
     * @return The postings list with the value term as the key and the number
     * of occurrences as value. An ordering is not expected on the map
     */
    public Map<String, Integer> getPostings(String key) {
        //TODO: Implement this method
        Map<String, Integer> mapOutput = null;
        try {
            if(null!=key)
            	{
	            	mapDocTermFrequency = new HashMap<>();
	                mapIndex = readFromDisk(Partitioner.getPartitionNumber(key));
	                if(mapIndex!=null)
	                {
	                	mapDocTermFrequency = mapIndex.get(key);
		                if(null!=mapDocTermFrequency)
		                	{
		                		mapOutput = new HashMap();
		                		for (String str : mapDocTermFrequency.keySet()) {
				                    mapOutput.put("doc_"+str,mapDocTermFrequency.get(str));
				                }
		                	}
		                else{
		                	System.out.println("No such term exists in index.PLease try with other term.");
		                }
	                }
            	}

            
        } catch (Exception e) {
            System.err.println("Error in getPostings(). Message" + e.getMessage());
        } 
            return mapOutput;
        


    }

    /**
     * Method to get the top k key terms from the given index The top here
     * refers to the largest size of postings.
     *
     * @param k: The number of postings list requested
     * @return An ordered collection of dictionary terms that satisfy the
     * requirement If k is more than the total size of the index, return the
     * full index and don't pad the collection. Return null in case of an error
     * or invalid inputs
     */
    public Collection<String> getTopK(int k) {
        //TODO: Implement this method
        mapDocTermFrequency = new HashMap<>();
        mapIndex = new HashMap<>();
        int count = 0;
        switch (idx.toString().toUpperCase()) {
            case "TERM":
                for (int i = 0; i < Partitioner.getNumPartitions(); i++) {
                    mapIndex = readFromDisk(i);
                    count = count + mapIndex.keySet().size();
                }
                break;
            default:
                mapIndex = readFromDisk(0);
                count = count + mapIndex.keySet().size();
                break;
        }
        return null;
    }

    /**
     * Method to execute a boolean AND query on the index
     *
     * @param terms The terms to be queried on
     * @return An ordered map containing the results of the query The key is the
     * value field of the dictionary and the value is the sum of occurrences
     * across the different postings. The value with the highest cumulative
     * count should be the first entry in the map.
     */
    public Map<String, Integer> query(String... terms) {
        //TODO: Implement this method (FOR A BONUS)

        if (terms.length == 1) {
            return getPostings(terms[0]);
        }
        Map<String, Integer> mapOutput = new LinkedHashMap<>();
        Map<String, Integer> mapTemp = new LinkedHashMap<>();
        int i32NumOccurences;
        try {
            if (terms.length > 1) {
                mapOutput = getPostings(terms[0]);
                for (int i = 1; i < terms.length; i++) {
                    mapTemp = getPostings(terms[i]);
                    for (String strId : mapOutput.keySet()) {
                        if (!mapTemp.containsKey(strId)) {
                            mapOutput.remove(strId);
                        } else {
                            i32NumOccurences = mapTemp.get(strId) + mapOutput.get(strId);
                            mapOutput.put(strId, i32NumOccurences);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error in query() in IndexReader. Message:" + e.getMessage());
        }
        return mapOutput;
    }

    private Map<String, Map<String, Integer>> readFromDisk(int partitionNo) {
        mapIndex = null;
        try {
            File file = new File(prop.getProperty(IndexerConstants.TEMP_DIR) + "/" + idx.toString() + "_" + partitionNo + ".txt");
            if(file.exists())
            	{
	            	mapIndex=new HashMap<>();	
	            	FileInputStream f = new FileInputStream(file);
	                ObjectInputStream s = new ObjectInputStream(f);
	                mapIndex = (HashMap<String, Map<String, Integer>>) s.readObject();
	                
            	}
            else
            {
            	System.err.println("No such term exists in the index");
            }
            
        } catch (IOException e) {
            System.err.println("Error in readFromDisk() in IndexReader. Message:" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error in readFromDisk() in IndexReader. Message:" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in readFromDisk() in IndexReader. Message:" + e.getMessage());
        }
        return mapIndex;
    }
}
