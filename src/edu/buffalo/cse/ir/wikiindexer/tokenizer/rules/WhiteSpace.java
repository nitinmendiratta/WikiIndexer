/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;

/**
 *@author Nitin
 *This class splits the stream of token based on Whitespace.
 */
@RuleClass(className = TokenizerRule.RULENAMES.WHITESPACE)
public class WhiteSpace implements TokenizerRule{
    
    public void apply(TokenStream stream) throws TokenizerException {
       String token="";
       
        if(stream!=null)
        {
            while(stream.hasNext())
            {
               token=stream.next();
               if(!token.equals(""))
               {
                   String[] arrToken= token.trim().split("\\s+");
                   stream.previous();
                   stream.set(arrToken);
                   stream.next();
               }
            }
            stream.reset();
        }
    }
    
}
