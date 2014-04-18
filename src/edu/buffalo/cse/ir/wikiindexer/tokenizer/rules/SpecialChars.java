/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;

/**
 * @author Nitin
 */
@RuleClass(className = TokenizerRule.RULENAMES.SPECIALCHARS)
public class SpecialChars implements TokenizerRule {

    @Override
    public void apply(TokenStream stream) throws TokenizerException {
        // TODO Auto-generated method stub
        if (stream != null) {
            String token;
            while (stream.hasNext()) {
                token = stream.next();
                if (token != null) {
                    if (token.matches("[^a-zA-Z0-9\\.-]+")) {
                        stream.previous();
                        stream.remove();
//						stream.next();

                    } else {


                        if (token.matches("[a-zA-Z0-9\\.-]+[^a-zA-Z0-9\\.-]+[a-zA-Z0-9\\.-]+")) {

                            String[] newArray = token.split("[^a-zA-Z0-9\\.-]");
                            stream.previous();
                            stream.set(newArray);
                            stream.next();
                        } else {
                            if (token.matches("\\d+-\\d+")) {
                                token = token.replaceAll("[^a-zA-Z0-9\\.]", "");

                            } else if (token.matches("[^a-zA-Z0-9\\.]+[a-zA-Z0-9\\.]+-+[a-zA-Z0-9\\.]+")) {
                                token = token.replaceAll("[^a-zA-Z0-9\\.-]", "");
                            } else {
                                token = token.replaceAll("[^a-zA-Z0-9\\.-]", "");
                            }


                            stream.previous();
                            stream.set(token);
                            stream.next();
                        }

                    }

                }
            }
            stream.reset();
        }
    }
}
