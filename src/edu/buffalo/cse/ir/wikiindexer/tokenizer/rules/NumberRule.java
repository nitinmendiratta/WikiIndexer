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
@RuleClass(className = TokenizerRule.RULENAMES.NUMBERS)
public class NumberRule implements TokenizerRule{
    private static final Pattern objPatternNumber=Pattern.compile("[0-9]+(?:(?:\\.|\\,)[0-9]*)?");
    
    @Override
    public void apply(TokenStream stream) throws TokenizerException{
        String strToken;
        StringBuffer sb;
        Matcher m;
        if(stream!=null)
        {
            while(stream.hasNext())
            {
                //sb.delete(0, sb.length());
                sb=new StringBuffer();
                strToken=stream.next();
                sb.append(strToken);
                if(!verifyDate(strToken))
                {
                    m=objPatternNumber.matcher(strToken);
                    while(m.find())
                    {
                        strToken=m.replaceAll("");
                    }
                    stream.previous();
                    if(strToken.trim().equals(""))
                    {
                        stream.remove();
                    }
                    else
                    stream.set(strToken);
                    stream.next();
                }
            }
            stream.reset();
        }
    }
    public boolean verifyDate(String strToken)
    {
        boolean blFlag=false;
        String strMonth;
        String strDay;
        String strYear;
        String strRegex="[0-9]{4}(?:0[1-9]|1[012])(?:0[1-9]|1[0-9]|2[0-9]|3[01])";
        try
        {
            if(strToken.length()!=8)
            {
                blFlag=false;
            }else{
                blFlag=strToken.matches(strRegex);
            }
        }
        catch(Exception e)
        {
            System.err.println("Error in verifyDate method in Date Rule class. Message:"+e.getMessage());
        }
        finally
        {
            return blFlag;
        }
        
    }
}
