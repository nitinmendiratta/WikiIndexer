/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 ** @author Nitin
 */
public class RegexPatternImpl {

    String token = "";
    String text = "";
    Pattern p;
    Matcher m;
    StringBuffer sb;

    public void patternImpl(TokenStream stream, String strPattern, String strRuleType) {
        
        if (stream != null) {
            p = Pattern.compile(strPattern);
            while (stream.hasNext()) {
                sb = new StringBuffer();
                token = stream.next();
                m = p.matcher(token);
                while (m.find()) {
                    if (strRuleType.toLowerCase()=="hyphen") {
                        int grpCount = m.groupCount();
                        text = m.group(grpCount);
                        m.appendReplacement(sb, " ");
                    }
                    else
                    {
                        m.appendReplacement(sb, "");
                    }
                }
                m.appendTail(sb);
                stream.previous();
                stream.set(sb.toString());
                stream.next();
            }
            stream.reset();
        }
    }
}
