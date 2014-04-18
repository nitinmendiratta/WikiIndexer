/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Nitin
 * This class represents a stream of tokens as the name suggests. It wraps the
 * token stream and provides utility methods to manipulate it
 */
public class TokenStream implements Iterator<String> {

    private List<String> lstStream;
    private int arrIndex = 0;
    int set=0;
    

    /**
     * Default constructor
     *
     * @param bldr: THe stringbuilder to seed the stream
     */
    public TokenStream(StringBuilder bldr) {
        //lstStream.add(bldr.toString());
        arrIndex = 0;
        set=0;
        lstStream = new ArrayList<String>();
        if (null != bldr && !bldr.equals("")) {

            lstStream.add(bldr.toString());
        } else {
            lstStream.add((String) null);
        }
    }

    /**
     * Overloaded constructor
     *
     * @param bldr: THe stringbuilder to seed the stream
     */
    public TokenStream(String string) {
        arrIndex = 0;
        set=0;
        lstStream = new ArrayList<String>();
        if (null != string && !string.equals("")) {
            
            lstStream.add(string);
        } else {
            
            lstStream.add((String) null);
        }

    }

    /**
     * Method to append tokens to the stream
     *
     * @param tokens: The tokens to be appended
     */
    public void append(String... tokens) {
        if(tokens!=null && tokens.length>0 && !tokens.equals(""))
        {
           for(String strToken: tokens)
           {
               if(null!=strToken && !strToken.equals(""))
               {
                   lstStream.add(strToken);
               }
           } 
        }
    }

    /**
     * Method to retrieve a map of token to count mapping This map should
     * contain the unique set of tokens as keys The values should be the number
     * of occurrences of the token in the given stream
     *
     * @return The map as described above, no restrictions on ordering
     * applicable
     */
    public Map<String, Integer> getTokenMap() {
        Map<String, Integer> map = null;
        String strNext = "";
        try {
            map = new HashMap<>();
            Iterator it = lstStream.iterator();
            while (it.hasNext()) {
                strNext = (String) it.next();
                if (strNext!=null && !map.containsKey(strNext)) {
                    map.put(strNext, Collections.frequency(lstStream, strNext));
                }

            }
            if(map.size()==0)
            {
                map=null;
            }
        } catch (Exception e) {
            System.err.println("Error in TokenStream:getTokenMap" + e.getMessage());
        } finally {
            return map;
        }
    }

    /**
     * Method to get the underlying token stream as a collection of tokens
     *
     * @return A collection containing the ordered tokens as wrapped by this
     * stream Each token must be a separate element within the collection.
     * Operations on the returned collection should NOT affect the token stream
     */
    public Collection<String> getAllTokens() {
        if(lstStream.size()>0)
        {
            if(arrIndex==0 && lstStream.get(arrIndex)==null)
            return null;
            else
            {
                return lstStream;
            }
        }
        return null;
    }

    /**
     * Method to query for the given token within the stream
     *
     * @param token: The token to be queried
     * @return: THe number of times it occurs within the stream, 0 if not found
     */
    public int query(String token) {
        int count = 0;
        if (lstStream.size() > 0) {
            count = Collections.frequency(lstStream, token);
        }
        return count;
    }

    /**
     * Iterator method: Method to check if the stream has any more tokens
     *
     * @return true if a token exists to iterate over, false otherwise
     */
    public boolean hasNext() {
        boolean flag = false;
        try {
            if (arrIndex < lstStream.size() && lstStream.get(arrIndex)!=null) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            System.err.println("Error in hasNext:" + e.getMessage());
        } finally {
            return flag;
        }
    }

    /**
     * Iterator method: Method to check if the stream has any more tokens
     *
     * @return true if a token exists to iterate over, false otherwise
     */
    public boolean hasPrevious() {
        boolean flag = false;
        try {

            if (arrIndex > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            System.err.println("Error in hasNext:" + e.getMessage());
        } finally {
            return flag;
        }
    }

    /**
     * Iterator method: Method to get the next token from the stream Callers
     * must call the set method to modify the token, changing the value of the
     * token returned by this method must not alter the stream
     *
     * @return The next token from the stream, null if at the end
     */
    public String next() {
        String strNext = "";
        set=1;
        try {
            if (arrIndex < lstStream.size()) {
                strNext = lstStream.get(arrIndex);
                arrIndex++;
            } else {
                strNext = null;
            }
        } catch (Exception e) {
            System.err.println("Error in next():" + e.getMessage());
        } finally {
            return strNext;
        }

    }

    /**
     * Iterator method: Method to get the previous token from the stream Callers
     * must call the set method to modify the token, changing the value of the
     * token returned by this method must not alter the stream
     *
     * @return The next token from the stream, null if at the end
     */
    public String previous() {
        String strPrevious = "";
        set=0;
        try {

            if (arrIndex > 0) {
                arrIndex--;
                strPrevious = lstStream.get(arrIndex);

            } else {
                strPrevious = null;
            }
        } finally {
            return strPrevious;
        }

    }

    /**
     * Iterator method: Method to remove the current token from the stream
     */
    public void remove() {
        if (arrIndex >= 0 && arrIndex < lstStream.size()) {
            lstStream.remove(arrIndex);
        }
    }

    /**
     * Method to merge the current token with the previous token, assumes
     * whitespace separator between tokens when merged. The token iterator
     * should now point to the newly merged token (i.e. the previous one)
     *
     * @return true if the merge succeeded, false otherwise
     */
    public boolean mergeWithPrevious() {
        boolean flag = false;
        try {
            if (arrIndex != 0) {
                flag = true;
                String strCurrentElement = lstStream.get(arrIndex);
                String strPreviousElement = lstStream.get(arrIndex - 1);
                lstStream.remove(arrIndex);
                lstStream.remove(arrIndex - 1);
                arrIndex--;
                lstStream.add(arrIndex, strPreviousElement + " " + strCurrentElement);
            }

        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    /**
     * Method to merge the current token with the next token, assumes whitespace
     * separator between tokens when merged. The token iterator should now point
     * to the newly merged token (i.e. the current one)
     *
     * @return true if the merge succeeded, false otherwise
     */
    public boolean mergeWithNext() {
        boolean flag = true;
        try {
            String strCurrentElement = lstStream.get(arrIndex);
            String strNextElement = lstStream.get(arrIndex + 1);
            lstStream.remove(arrIndex + 1);
            lstStream.remove(arrIndex);
            lstStream.add(arrIndex, strCurrentElement + " " + strNextElement);
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    /**
     * Method to replace the current token with the given tokens The stream
     * should be manipulated accordingly based upon the number of tokens set It
     * is expected that remove will be called to delete a token instead of
     * passing null or an empty string here. The iterator should point to the
     * last set token, i.e, last token in the passed array.
     *
     * @param newValue: The array of new values with every new token as a
     * separate element within the array
     */
    public void set(String... newValue) {
         try {
            int count=0;
            if (newValue.length > 0 && arrIndex<lstStream.size() && lstStream.get(arrIndex)!=null) {
                
                //arrIndex--;&& 
                for (String str : newValue) {
                    count++;
                    if (str!=null && str.trim().length() > 0) {
                        if(count==1)
                        {
                            lstStream.remove(arrIndex);
                            arrIndex--;
                        }
                        arrIndex++;
                        lstStream.add(arrIndex, str);
                        
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Error in set Method:" + e.getMessage());
        }

    }

    /**
     * Iterator method: Method to reset the iterator to the start of the stream
     * next must be called to get a token
     */
    public void reset() {
        arrIndex = 0;
    }

    /**
     * Iterator method: Method to set the iterator to beyond the last token in
     * the stream previous must be called to get a token
     */
    public void seekEnd() {
        arrIndex = lstStream.size();
    }

    /**
     * Method to merge this stream with another stream
     *
     * @param other: The stream to be merged
     */
    public void merge(TokenStream other) {
        if(other!=null && other.lstStream.get(other.arrIndex)!=null)
        {
            if(lstStream.get(arrIndex)==null)
            {
                lstStream.remove(arrIndex);
            }
            lstStream.addAll(lstStream.size(), other.lstStream);
        }
    }
}
