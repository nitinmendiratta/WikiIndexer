/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;

/**
 * @author Nitin 
 * An abstract class that represents a dictionary object for a given index
 */
public abstract class Dictionary implements Writeable {

    Map<String, Integer> dictIndex = null;
    //Map<INDEXFIELD, Map<String, Integer>> docDict = null;
    INDEXFIELD idx;
    Properties prop;

    public Dictionary(Properties props, INDEXFIELD field) {
        //TODO Implement this method
        dictIndex = new TreeMap();
        //docDict = new HashMap();
        idx = field;
        this.prop = props;
    }

    /* (non-Javadoc)
     * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
     */
    public void writeToDisk() throws IndexerException {
        // TODO Implement this method
        try {
            File file = new File("files/DICTIONARY",idx + "_dictionary.TXT");
            file.getParentFile().mkdirs();
            FileOutputStream f = new FileOutputStream(file, true);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(dictIndex);
            s.flush();
        } catch (IOException ex) {
            System.err.println("Error in Dictionary writeToDisk():" + ex.getMessage());
        }

    }

    /* (non-Javadoc)
     * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
     */
    public void cleanUp() {
        // TODO Implement this method
        this.dictIndex=null;
        
    }

    /**
     * Method to check if the given value exists in the dictionary or not Unlike
     * the subclassed lookup methods, it only checks if the value exists and
     * does not change the underlying data structure
     *
     * @param value: The value to be looked up
     * @return true if found, false otherwise
     */
    public boolean exists(String value) {
        boolean flag = false;
        try {
            if (dictIndex.containsKey(value)) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            System.err.println("Error in Dictionary-exists()" + e.getMessage());
        } finally {
            return flag;
        }
    }

    /**
     * MEthod to lookup a given string from the dictionary. The query string can
     * be an exact match or have wild cards (* and ?) Must be implemented ONLY
     * AS A BONUS
     *
     * @param queryStr: The query string to be searched
     * @return A collection of ordered strings enumerating all matches if found
     * null if no match is found
     */
    public Collection<String> query(String queryStr) {
        Set<String> set = null;
        try {
            if (null != queryStr && !queryStr.equals("")) {
                Pattern objPatternWildCardQuery;
                String strMatch = "";
                String strWildCardInput = "";

                //Map<String, Integer> dictIndex = new HashMap<>();
                if (dictIndex == null) {
                    File file = new File("files/" + idx + "dictionary");
                    FileInputStream f = new FileInputStream(file);
                    ObjectInputStream s = new ObjectInputStream(f);
                    dictIndex = (Map<String, Integer>) s.readObject();
                } else if (queryStr.startsWith("*") || queryStr.startsWith("?")) {
                    strWildCardInput = "." + queryStr + "\\b";
                } else if (queryStr.endsWith("*") || queryStr.endsWith("?")) {
                    strWildCardInput = "\\b" + queryStr.substring(0, queryStr.length() - 1) + ".*?\\b";
                } else if (queryStr.contains("*")) {
                    strWildCardInput = "\\b"+queryStr.split("\\*")[0] + ".*" + queryStr.split("\\*")[1]+"\\b";
                } else if (queryStr.contains("?")) {
                    strWildCardInput = "\\b"+queryStr.split("\\?")[0] + ".?" + queryStr.split("\\?")[1]+"\\b";
                }
                boolean flag = false;
                set = new HashSet<>();
                for (Map.Entry<String, Integer> map : dictIndex.entrySet()) {
                    strMatch = map.getKey();

                    if (queryStr.equals(strMatch) && !set.contains(queryStr)) {
                        flag = true;
                        set.add(queryStr);
                    } else if (!strWildCardInput.equals("")) {
                        //set = new HashSet<>();

                        objPatternWildCardQuery = Pattern.compile(strWildCardInput);
                        Matcher m = objPatternWildCardQuery.matcher(strMatch);
                        while (m.find()) {
                            flag = true;
                            set.add(strMatch);
                        }

                    }

                }
                if (flag == false) {
                    set = null;
                }
            }

            if (null != set) {

                System.out.println(set);
            }

        } catch (Exception e) {
            System.err.println("Error in query() in Dictionary class. Message:" + e.getMessage());
        }

        return set;

    }

    /**
     * Method to get the total number of terms in the dictionary
     *
     * @return The size of the dictionary
     */
    public int getTotalTerms() {
        return dictIndex.size();
    }

   }
