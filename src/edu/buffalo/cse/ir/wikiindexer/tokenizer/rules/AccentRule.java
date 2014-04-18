/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import java.text.Normalizer;

/**
 *@author Nitin
 */
@RuleClass(className = TokenizerRule.RULENAMES.ACCENTS)
public class AccentRule implements TokenizerRule {

    @Override
    public void apply(TokenStream stream) throws TokenizerException {
        String strToken;
        StringBuilder sb = new StringBuilder();
        try {
            if (stream != null) {
                while (stream.hasNext()) {
                    strToken = stream.next();
                    if (!strToken.equals("")) {
                       strToken=Normalizer.normalize(strToken, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

                        for (char c : strToken.toCharArray()) {
                            if (c <= '\u007F') {
                                sb.append(c);
                            }
                        }
                        stream.previous();
                        stream.set(strToken);
                        stream.next();
                    }
                }
                stream.reset();
            }
        } catch (Exception e) {
            System.err.println("Error in Acent rule" + e.getMessage());
        }
    }
}
