/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

/**
 * @author Nitin
 * Wrapper class for all indexing related exceptions.
 * You can add your own code and handling here if you like
 */
public class IndexerException extends Exception {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 8985010470005283174L;
	
	public IndexerException(String msg) {
		super(msg);
		//Add custom code here if needed
	}

}
