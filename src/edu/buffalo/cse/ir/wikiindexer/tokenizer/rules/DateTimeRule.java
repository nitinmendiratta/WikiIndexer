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
@RuleClass(className = TokenizerRule.RULENAMES.DATES)
public class DateTimeRule implements TokenizerRule {

    Pattern p;
    Matcher m;
    StringBuffer sb;
    String month = "\\b(January|February|March|April|May|June|July|August|September|October|November|December)";
    String day = "\\b([1-9]|0[1-9]|[12][0-9]|3[01])";
    String year = "((?:19|20)[0-9][0-9])";
    String input = "";

    @Override
    public void apply(TokenStream stream) throws TokenizerException {
        try {
            if (stream != null) {
                while (stream.hasNext()) {
                    input = stream.next();
                    if (input.length() > 0) {
                        dateStep7();
                        timeStep2();
                        
                        dateImpl();
                        dateStep2();
                        dateStep3();
                        dateStep4();
                        dateStep5();
                        dateStep6();
                        
                        timeStep1();
                        //timeStep2();
                        stream.previous();
                        stream.set(sb.toString());
                        stream.next();
                    }
                }
                stream.reset();
            }
        } catch (Exception ex) {
            System.err.println("Error in date rule class:" + ex.toString());
        }
    }

    public void dateImpl() {

        String pattern = month + "[,]?[\\s]?" + day + "[,]?[\\s]?" + year;
        if (sb.length() > 0) {
            RegexHandlerDate(pattern, sb.toString());
        } else {
            RegexHandlerDate(pattern, input);
        }
    }

    public void dateStep2() {
        String pattern = day + "[,]?[\\s]?" + month + "[,]?[\\s]?" + year;
        if (sb.length() > 0) {
            RegexHandlerDate(pattern, sb.toString());
        } else {
            RegexHandlerDate(pattern, input);
        }

    }

    public void dateStep3() {
        String pattern = "\\b" + year + "\\b";
        if (sb.length() > 0) {
            RegexHandlerDate(pattern, sb.toString());
        } else {
            RegexHandlerDate(pattern, input);
        }

    }

    public void dateStep4() {
        String pattern = "\\b" + month + "[,]?[\\s]?" + day + "\\b";
        if (sb.length() > 0) {
            RegexHandlerDate(pattern, sb.toString());
        } else {
            RegexHandlerDate(pattern, input);
        }

    }

    public void dateStep5() {

        String pattern = "\\b(\\d{1,4})[\\s]?(AD)\\b";
        if (sb.length() > 0) {
            RegexHandler_AD_BC(pattern, sb.toString());
        } else {
            RegexHandler_AD_BC(pattern, input);
        }

    }

    public void dateStep6() {
        String pattern = "\\b(\\d{1,4})[\\s]?(BC)\\b";
        if (sb.length() > 0) {
            RegexHandler_AD_BC(pattern, sb.toString());
        } else {
            RegexHandler_AD_BC(pattern, input);
        }

    }

    public void dateStep7()
    {
        String strPattern="\\b((?:19|20)[0-9][0-9])\\b([\\W\\D]{1})([\\d]{2}\\b)";
        String strInput="";
        String strYear="";
        String strGroup2="";
        sb=new StringBuffer();
        int iCount;
        if (sb.length() > 0) {
            strInput= sb.toString();
        } else {
            strInput= input;
        }
        p = Pattern.compile(strPattern);
        m = p.matcher(strInput);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      sb = new StringBuffer();
        while (m.find()) {
            strYear=m.group(1);
            if(Integer.parseInt(strYear)>1900 && Integer.parseInt(strYear)<2000)
            {
                strGroup2="19"+m.group(3);
            }
           if(Integer.parseInt(strYear)>2000)
            {
                strGroup2="20"+m.group(3);
            }
            m.appendReplacement(sb, strYear+m.group(2)+strGroup2);
        }
        m.appendTail(sb);
    }
    public void timeStep1() {
        String min = "(0[1-9]|1[0-9]|2[1-9]|3[1-9]|4[1-9]|5[1-9])";
        String pattern = "\\b([1-9]|0[1-9]|1[0-9]|2[1-4])[:][\\s]?" + min + "[\\s]?((?:[aA]|[pP])[mM])";
        if (sb.length() > 0) {
            RegexHandler_Time(pattern, sb.toString());
        } else {
            RegexHandler_Time(pattern, input);
        }


    }
    public void timeStep2() {
        String MinSec = "(0[1-9]|1[0-9]|2[1-9]|3[1-9]|4[1-9]|5[1-9])";
        String pattern = "((?:[1-9]|0[0-9]|1[0-9]|2[1-4]):(?:0[1-9]|1[0-9]|2[1-9]|3[1-9]|4[1-9]|5[1-9]):(?:0[1-9]|1[0-9]|2[1-9]|3[1-9]|4[1-9]|5[1-9])) .*? (?:Sunday|MONDAY|TUESDAY|WEDNESDAY),\\s+((?:[1-9]|0[1-9]|[12][0-9]|3[01])\\s+(?:January|February|March|April|May|June|July|August|September|October|November|December)\\s+(?:19|20)[0-9][0-9])";
        String strInput="";
        //sb=new StringBuffer();
        int iCount;
        if (sb.length() > 0) {
            strInput= sb.toString();
        } else {
            strInput= input;
        }
        p = Pattern.compile(pattern);
        m = p.matcher(strInput);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      sb = new StringBuffer();
        while (m.find()) {
            iCount = m.groupCount();

           
            m.appendReplacement(sb, m.group(2) + " "+m.group(1));
        }
        m.appendTail(sb);

    }

    public void RegexHandler_Time(String pattern, String input) {
        String strOutput;
        int iCount;
        String strHours = "";
        String strMin = "";
        String strSecond = "00";
        p = Pattern.compile(pattern);
        m = p.matcher(input);
        sb = new StringBuffer();
        while (m.find()) {
            iCount = m.groupCount();

            if (m.group(3).toUpperCase().equals("PM") && Integer.parseInt(m.group(1)) < 12) {
                strHours = String.valueOf(Integer.parseInt(m.group(1)) + 12);
            } else {
                strHours = m.group(1);
            }
            if (strHours.length() < 2) {
                strHours = "0" + strHours;
            }
            m.appendReplacement(sb, strHours + ":" + m.group(2) + ":00");
        }
        m.appendTail(sb);
    }

    public void RegexHandler_AD_BC(String pattern, String input) {
        String strOutput;
        int iCount;
        p = Pattern.compile(pattern);
        m = p.matcher(input);
        sb = new StringBuffer();
        try {
            while (m.find()) {
                m.groupCount();
                strOutput = m.group(1);
                iCount = strOutput.length();

                while (iCount < 4) {
                    strOutput = "0" + strOutput;
                    iCount++;
                }
                if (m.group(2).toUpperCase().equals("BC")) {
                    strOutput = "-" + strOutput;
                }
                m.appendReplacement(sb, strOutput);
                sb.append("0101");
            }
            m.appendTail(sb);
        } catch (Exception e) {
            System.err.println("Error in Date Rule,RegexHandlerDate().Message" + e.getMessage());
        }
    }

    public void RegexHandlerDate(String pattern, String input) {
        p = Pattern.compile(pattern);
        m = p.matcher(input);
        sb = new StringBuffer();
        String text = "";
        String strMonth = "";
        String strDay = "";
        String strYear = "";
        int j = 1;
        int grpCount;
        try {
            while (m.find()) {
                grpCount = m.groupCount();
                for (int i = 1; i <= grpCount; i++) {
                    switch (m.group(i).toUpperCase()) {
                        case "JANUARY":
                            strMonth = "01";
                            break;
                        case "FEBRUARY":
                            strMonth = "02";
                            break;
                        case "MARCH":
                            strMonth = "03";
                            break;
                        case "APRIL":
                            strMonth = "04";
                            break;
                        case "MAY":
                            strMonth = "05";
                            break;
                        case "JUNE":
                            strMonth = "06";
                            break;
                        case "JULY":
                            strMonth = "07";
                            break;
                        case "AUGUST":
                            strMonth = "08";
                            break;
                        case "SEPTEMBER":
                            strMonth = "09";
                            break;
                        case "OCTOBER":
                            strMonth = "10";
                            break;
                        case "NOVEMBER":
                            strMonth = "11";
                            break;
                        case "DECEMBER":
                            strMonth = "12";
                            break;
                        default:
                            if (Integer.parseInt(m.group(i)) < 10) {
                                strDay = "0" + m.group(i);
                            } else if (Integer.parseInt(m.group(i)) <= 31) {
                                strDay = m.group(i);
                            } else {
                                if (grpCount == 1) {
                                    strMonth = "01";
                                    strDay = "01";
                                }
                                strYear = m.group(i);

                            }
                            break;
                    }
                    if (i == j * 3) {
                        m.appendReplacement(sb, strYear + strMonth + strDay);
                    }

                }
                if (grpCount == 1) {
                    m.appendReplacement(sb, strYear + strMonth + strDay);
                }
                if (grpCount == 2) {
                    strYear = "1900";
                    m.appendReplacement(sb, strYear + strMonth + strDay);
                }
                

            }
            m.appendTail(sb);
        } catch (Exception e) {
            System.err.println("Error in Date Rule,RegexHandlerDate().Message" + e.getMessage());
        }


    }
}