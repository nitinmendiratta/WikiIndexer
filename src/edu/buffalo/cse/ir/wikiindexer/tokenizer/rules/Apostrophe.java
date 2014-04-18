/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.FileUtil;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
* @author Nitin
 */
@RuleClass(className = TokenizerRule.RULENAMES.APOSTROPHE)
public class Apostrophe implements TokenizerRule {
private static final Pattern objApostrophePattern= Pattern.compile("\\'\\B|\\'s\\b|^\\'"); 
HashMap<String, String> map;
    
@Override
    public void apply(TokenStream stream) throws TokenizerException {
        StringBuffer sb;
        String token="";
        Matcher m;
        loadMapApostropheWord();
        verifyWord(stream);
        stream.reset();
        RegexPatternImpl objRegexPatternImpl = new RegexPatternImpl();
        if (stream != null) {
            while (stream.hasNext()) {
                sb=new StringBuffer();
                token = stream.next();
                m=objApostrophePattern.matcher(token);
                while (m.find()) {
                        m.appendReplacement(sb, "");
                }
                m.appendTail(sb);
                stream.previous();
                stream.set(sb.toString());
                stream.next();
            }
            stream.reset();
        }
        
        
    }

    /**
     *
     */
    public void loadMapApostropheWord() {
        try {
            map = new HashMap();
            Properties props = new Properties();
		FileInputStream inStream = null;
		inStream = new FileInputStream("files/Apostrophe.config");
		props.load(inStream);
		inStream.close();
            
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                map.put(key,value);
            }

        } catch (IOException ex) {
            System.err.println("Error in Apostrophe rule:loadMapApostropheWord");
        }
    }
    public void verifyWord(TokenStream stream)
    {
        String strToken;
        if(stream!=null)
        {
            while(stream.hasNext())
            {
                strToken=stream.next();
                if(strToken.toLowerCase()!="put" && map.containsKey(strToken))
                {
                    stream.previous();
                    stream.set(map.get(strToken).split(" "));
                    stream.next();
                }
            }
        }
    }
}
