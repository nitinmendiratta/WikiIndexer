/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.AUTHOR;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.CATEGORY;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.LINK;
import static edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD.TERM;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.AccentRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Apostrophe;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.DateTimeRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.EnglishStemmer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Hyphen;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.NumberRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Punctuation;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.SpecialChars;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.StopWordRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.WhiteSpace;

/**
 * @author Nitin
 * Factory class to instantiate a Tokenizer instance
 * The expectation is that you need to decide which rules to apply for which field
 * Thus, given a field type, initialize the applicable rules and create the tokenizer
 *
 */
public class TokenizerFactory {
	//private instance, we just want one factory
	private static TokenizerFactory factory;
	
	//properties file, if you want to read soemthing for the tokenizers
	private static Properties props;
	
	/**
	 * Private constructor, singleton
	 */
	private TokenizerFactory() {
		//TODO: Implement this method
	}
	
	/**
	 * MEthod to get an instance of the factory class
	 * @return The factory instance
	 */
	public static TokenizerFactory getInstance(Properties idxProps) {
		if (factory == null) {
			factory = new TokenizerFactory();
			props = idxProps;
		}
		
		return factory;
	}
	
	/**
	 * Method to get a fully initialized tokenizer for a given field type
	 * @param field: The field for which to instantiate tokenizer
	 * @return The fully initialized tokenizer
	 */
	public Tokenizer getTokenizer(INDEXFIELD field) {
		Tokenizer tk = null;
        try {
            switch (field) {
                case CATEGORY:
                    tk = new Tokenizer(new WhiteSpace());
                    break;
                case AUTHOR:
                    tk = new Tokenizer(new WhiteSpace());
                    break;
                case TERM:
                    tk = new Tokenizer(new WhiteSpace(),new AccentRule(),new Punctuation(),new SpecialChars(),new StopWordRule(),new EnglishStemmer());
                    break;
                case LINK:
                    tk = new Tokenizer(new WhiteSpace());
                    break;
                default:
                    break;
            }
        } catch (TokenizerException ex) {
            System.err.println("Error in TokenizerFactory:getTokenizer.Message:" + ex.getMessage());
        }
        return tk;
	}
}
