/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;

/**
 *
 * @author Nitin
 */
@RuleClass(className = TokenizerRule.RULENAMES.CAPITALIZATION)
public class CapitilizationRule implements TokenizerRule {
    @Override
    public void apply(TokenStream stream) throws TokenizerException{
        
    }
}
