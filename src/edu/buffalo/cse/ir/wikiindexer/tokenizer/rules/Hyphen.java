/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nitin
 */
@RuleClass(className = TokenizerRule.RULENAMES.HYPHEN)
public class Hyphen implements TokenizerRule {

    StringBuffer sb;
    String token = "";

    @Override
    public void apply(TokenStream stream) throws TokenizerException {
        if (stream != null) {
            while (stream.hasNext()) {
                token = stream.next();
                if (token.length() > 0) {
                    hyphenImpl();
                    hyphenStep2();
                    hyphenstep3();
                    stream.previous();
                    if(sb.toString().trim().length()==0)
                    {
                        stream.remove();
                    }
                    else
                    stream.set(sb.toString().trim());
                    stream.next();
                }
            }
            stream.reset();
        }
    }

    public void hyphenImpl() {
        String pattern = "[a-zA-z]+(\\-+)\\B";
        hyphenRegexHandler(pattern, token);
    }

    public void hyphenStep2() {
        String pattern = "\\B(-)[.]?";
        if (sb.length() > 0) {
            hyphenRegexHandler(pattern, sb.toString());
        } else {
            hyphenRegexHandler(pattern, token);
        }
    }

    public void hyphenstep3() {
        String pattern = "[a-zA-Z]+(-)[a-zA-Z]+$";
        if (sb.length() > 0) {
            hyphenRegexHandler(pattern, sb.toString());
        } else {
            hyphenRegexHandler(pattern, token);
        }
    }

    public void hyphenRegexHandler(String pattern, String input) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        sb = new StringBuffer();
        while (m.find()) {
            String str = m.group();
            str = str.replaceAll(m.group(1), " ");
            m.appendReplacement(sb, str.trim());
        }
        m.appendTail(sb);
    }
}
