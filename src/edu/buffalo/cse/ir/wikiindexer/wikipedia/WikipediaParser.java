/**
 *
 */
//
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Nitin 
 * This class implements Wikipedia markup processing. 
 * Wikipedia markup details are presented here:
 * http://en.wikipedia.org/wiki/Help:Wiki_markup It is expected that all methods
 * marked "todo" will be implemented by students. 
 * All methods are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {

    static WikipediaDocument objWikiDocument;
    static List<String> lstSectionsData;
    static List<String> lstSections;
    String strParsedText = "";
    StringBuffer sb;
    private static final Pattern objPatternTemplate = Pattern.compile("(?m)\\{\\{[^\\{]+?\\}\\}", Pattern.MULTILINE);
    private static final Pattern objPatternTagFormatting = Pattern.compile("\\s*(?:<.*?>|\\&lt\\;.*?\\&gt\\;)\\s*", Pattern.MULTILINE);
    private static final Pattern objPatternTextFormatting = Pattern.compile("'''''(.*?)'''''|'''(.*?)'''|''(.*?)''", Pattern.MULTILINE);
    private static final Pattern objPatternListFormatting = Pattern.compile("\\B(?:\\*|\\#|\\:|\\;){1}+\\s*", Pattern.MULTILINE);
    private static final Pattern objPatternSectionFormatting = Pattern.compile("={2,6}(.*?)={2,6}", Pattern.MULTILINE);
    private static final Pattern mPatLink = Pattern.compile("(\\[\\[)(.*?)(\\]\\])");
    private static final Pattern mPatLinkNoWiki = Pattern.compile("\\[\\[.*\\]\\]<nowiki.*?\\/>");
    private static final Pattern mPatExtLinkNoHtmlTag = Pattern.compile("(\\[(https?):\\/\\/)(.*?)\\]");
    private static final Pattern mPatExtLinkWithHtmlTag = Pattern.compile("<span class=\"plainlinks\">\\[(https?):\\/\\/(.*?)<\\/span>");
    private static final Pattern mRemoveCategories = Pattern.compile("\\[\\[(Category):(.*?)(\\]\\])");
    /* TODO */

    /**
     * Method to parse section titles or headings. Refer:
     * http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
     *
     * @param titleStr: The string to be parsed
     * @return The parsed string with the markup removed
     */
    public static String parseSectionTitle(String titleStr) {
        Matcher m;
        StringBuffer sb = new StringBuffer();
        String text = "";
        String textData = "";
        String strOutput = "";
        int count = 0;
        int istart;
        int iend = 0;
        int startIndex = 0;

        if (null != titleStr && !titleStr.equals("")) {
            m = objPatternSectionFormatting.matcher(titleStr);
            lstSectionsData = new ArrayList();
            lstSections = new ArrayList();
            while (m.find()) {
                count++;
                int grpCount = m.groupCount();

                istart = m.start();
                if (istart > 0 && count == 1) {
                    startIndex = m.start();
                }
                if (istart != 0 && iend != 0 && istart > iend) {
                    textData = titleStr.substring(iend, istart).trim();
                }
                if (textData != null) {
                    lstSectionsData.add(textData.trim());
                }
                iend = m.end();
                while (grpCount > 0) {
                    if (m.group(grpCount) != null) {
                        text = m.group(grpCount);
                        lstSections.add(text.trim());
                    }
                    grpCount--;
                }

                m.appendReplacement(sb, Matcher.quoteReplacement(text.trim()));

            }
            if (startIndex > 0) {
                lstSections.add(0, "Default");
                lstSectionsData.add(0, titleStr.substring(0, startIndex).trim());
            }
            if (iend > 0 && titleStr.length() > iend) {
                //lstSectionsData.add(titleStr.substring(iend, titleStr.indexOf("Category") - 2).trim());
                lstSectionsData.add(titleStr.substring(iend).trim());
            }
            if (count == 0) {
                lstSections.add("Default");
                //lstSectionsData.add(titleStr.substring(0, titleStr.indexOf("Category") - 2).trim());
                lstSectionsData.add(titleStr.substring(iend).trim());
            }
            m.appendTail(sb);
            strOutput = sb.toString().trim();
        } else {
            strOutput = titleStr;
        }

        return strOutput;
    }

    /* TODO */
    /**
     * Method to parse list items (ordered, unordered and definition lists).
     * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
     *
     * @param itemText: The string to be parsed
     * @return The parsed string with markup removed
     */
    public static String parseListItem(String itemText) {
        String strOutput = "";
        try {
            if (null != itemText && !itemText.equals("")) {
                Matcher m = objPatternListFormatting.matcher(itemText);
                while (m.find()) {
                    //m.group();
                    itemText = m.replaceAll("");
                    m = objPatternListFormatting.matcher(itemText);
                }
                strOutput = itemText.trim();
            } else {
                strOutput = itemText;
            }
        } catch (PatternSyntaxException ex) {
            System.err.println("Error in WikipediaParser:parseListItem" + ex.toString());
        }
        return strOutput;
    }

    /* TODO */
    /**
     * Method to parse text formatting: bold and italics. Refer:
     * http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
     *
     * @param text: The text to be parsed
     * @return The parsed text with the markup removed
     */
    public static String parseTextFormatting(String text) {
        StringBuffer sb = new StringBuffer();
        String strOutput = "";
        try {
            if (null != text && !text.equals("")) {
                Matcher m = objPatternTextFormatting.matcher(text);
                String strTempMatch = "";
                while (m.find()) {
                    int grpCount = m.groupCount();
                    while (grpCount > 0) {
                        if (m.group(grpCount) != null) {
                            strTempMatch = m.group(grpCount);
                        }
                        grpCount--;
                    }
                    if (strTempMatch.equals("")) {
                        strTempMatch = m.group();
                    }
                    m.appendReplacement(sb, Matcher.quoteReplacement(strTempMatch));
                }
                m.appendTail(sb);
                strOutput = sb.toString();
            } else {
                strOutput = text;
            }
        } catch (PatternSyntaxException ex) {
            System.err.println("Error in WikipediaParser:parseTextFormatting" + ex.toString());
        }
        return strOutput;
    }

    /* TODO */
    /**
     * Method to parse *any* HTML style tags like: <xyz ...> </xyz>
     * For most cases, simply removing the tags should work.
     *
     * @param text: The text to be parsed
     * @return The parsed text with the markup removed.
     */
    public static String parseTagFormatting(String text) {
        String strOutput = "";
        try {
            if (null != text && !text.equals("")) {
                Matcher m = objPatternTagFormatting.matcher(text);
                while (m.find()) {
                    text = m.replaceAll(" ");
                }
                strOutput = text.trim();

            } else {
                strOutput = text;
            }

        } catch (PatternSyntaxException ex) {
            System.err.println("Error in WikipediaParser:parseTagFormatting" + ex.toString());
        }
        return strOutput;
    }

    /* TODO */
    /**
     * Method to parse wikipedia templates. These are *any* {{xyz}} tags For
     * most cases, simply removing the tags should work.
     *
     * @param text: The text to be parsed
     * @return The parsed text with the markup removed
     */
    public static String parseTemplates(String text) {
        try {
            Matcher m = objPatternTemplate.matcher(text);
            while (m.find()) {
                text = m.replaceAll("");
                m = objPatternTemplate.matcher(text);

            }
        } catch (PatternSyntaxException ex) {
            System.err.println("Error in WikipediaParser:parseTemplates" + ex.toString());
        }
        return text;
    }

    /* TODO */
    /**
     * Method to parse links and URLs. Refer:
     * http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
     *
     * @param text: The text to be parsed
     * @return An array containing two elements as follows - The 0th element is
     * the parsed text as visible to the user on the page The 1st element is the
     * link url
     */
    public static String[] parseLinks(String text) {
        //System.println("WikipediaParser::parseLinks");

        String url = "";
        String visibleToUser = "";
        String beforeLink="";
        String afterLink ="";

        if (text == null || text.length() == 0) {
            return new String[]{visibleToUser, url};
        }

        Matcher mLink = mPatExtLinkWithHtmlTag.matcher(text);
        while (mLink.find()) {
            //System.println("found ext link with span tag");
            beforeLink = text.substring(0, mLink.start());
            afterLink = text.substring(mLink.end());
            String unformattedUrl = WikipediaParser.parseTagFormatting(mLink.group()).trim();
            if (unformattedUrl.contains(" ")) {
                visibleToUser = beforeLink + unformattedUrl.replaceAll("[\\[\\]]", "").substring(1 + visibleToUser.indexOf(" ")) + afterLink;
            } else {
                visibleToUser = beforeLink + "" + afterLink;
            }

            text = visibleToUser;
            mLink = mPatExtLinkWithHtmlTag.matcher(text);

            //return new String[]{visibleToUser, url};
        }

        /*
         * for links that are in []
         */

        mLink = mPatExtLinkNoHtmlTag.matcher(text);
        while (mLink.find()) {
            //System.println("External link without html tag");
            beforeLink = text.substring(0, mLink.start());
            afterLink = text.substring(mLink.end());
            String unformattedUrl = mLink.group().replaceAll("[\\[\\]]", "").trim();
            //////System.println("-->" + unformattedUrl);
            if (unformattedUrl.contains(" ")) {
                //////System.println("contains space");
                visibleToUser = beforeLink + unformattedUrl.substring(1 + unformattedUrl.indexOf(" ")) + afterLink;
            } else {
                visibleToUser = beforeLink + "" + afterLink;
            }

            //////System.println("text=" + visibleToUser);
            //////System.println("url=" + url);
            text = visibleToUser;
            mLink = mPatExtLinkNoHtmlTag.matcher(text);
            //return new String[]{visibleToUser, url};
        }
        String regToTrim = "[\\[\\]\\|]";

        Matcher mNoWiki = mPatLinkNoWiki.matcher(text);
        while (mNoWiki.find()) {
            //System.println("external link with no wiki tag");
            beforeLink = text.substring(0, mNoWiki.start());
            afterLink = text.substring(mNoWiki.end());
            String parsedLink = WikipediaParser.parseTagFormatting(mNoWiki.group());
            visibleToUser = beforeLink + parsedLink.replaceAll(regToTrim, "") + afterLink;
            url = WikipediaParser.capitalize(parsedLink.replaceAll(regToTrim, ""));
            //////System.println("MAtch no wiki before=" + beforeLink + " after=" + afterLink + " text=" + visibleToUser);

            text = visibleToUser;
            mNoWiki = mPatLinkNoWiki.matcher(text);
            //return new String[] { visibleToUser, url };
        }

        /*
         * For the links that are in between [[ ]]
         */
        mLink = mPatLink.matcher(text);

        while (mLink.find()) {
            //System.println("insideWhileloop");
            String unformattedLink = mLink.group();

            if (unformattedLink == null || unformattedLink.length() == 0
                    || unformattedLink.replaceAll(regToTrim, "").trim().length() == 0) {
                return new String[]{visibleToUser, url};
            }

            beforeLink = text.substring(0, mLink.start());
            afterLink = text.substring(mLink.end());

            //////System.println("beforeLink--->" + beforeLink);
            //////System.println("group-------->" + mLink.group());
            //////System.println("afterLink---->" + afterLink);

            // //////System.println(unformattedLink);

            if (unformattedLink.replaceAll("[\\[\\]]", "").trim().toLowerCase().startsWith("media:")) {
                visibleToUser = unformattedLink.replaceAll("\\[\\[[mM][eE][dE][iI][aA]:(.*?)\\||\\]\\]", "").trim();
                visibleToUser = beforeLink + visibleToUser + afterLink;
                text = visibleToUser;
                mLink = mPatLink.matcher(text);
                continue;
            } else if (unformattedLink.matches("\\[\\[(:?Category):(.*?)(\\]\\])")) {
                //System.println("GotCategort-----");
                if (!unformattedLink.replaceAll("[\\[\\]]", "").toLowerCase().startsWith(":category")) {
                    String wholeLink = unformattedLink.replaceAll("[\\[\\]]", "");
                    //////System.println("--" + wholeLink);
                    if (wholeLink.contains("|")) {
                        //////System.println("ifpart");
                        visibleToUser = wholeLink.substring(1 + wholeLink.indexOf(":"), wholeLink.indexOf("|"));
                    } else {
                        //////System.println("elsepart");
                        visibleToUser = wholeLink.substring(1 + wholeLink.indexOf(":"));
                        //////System.println(visibleToUser);
                    }
                    if (visibleToUser.trim().length() != 0 && objWikiDocument != null) {
                        objWikiDocument.addCategory(visibleToUser);
                    }
                } else {
                    //it starts with :category
                    visibleToUser = unformattedLink.replaceAll("[\\[\\]]", "").replaceAll("^:", "");
                }
                text = visibleToUser = beforeLink + visibleToUser + afterLink;
                mLink = mPatLink.matcher(text);
                //////System.println("text=" + visibleToUser);
                continue;
            } else if (unformattedLink.matches("\\[\\[File:(.*?)\\]\\]")) {
                visibleToUser = unformattedLink.replaceAll("\\[\\[File:(.*)\\||\\]\\]|\\[\\[File:.*", "").trim();
                if (visibleToUser.contains("=")) {
                    visibleToUser = "";
                }
                text = visibleToUser = beforeLink + visibleToUser + afterLink;
                mLink = mPatLink.matcher(text);
                //////System.println("Text=" + visibleToUser + " url=" + url);
                continue;
            } else if (unformattedLink.matches("\\[\\[:File:(.*?)\\]\\]")) {
                visibleToUser = unformattedLink.replaceAll("\\[\\[:|\\]", "").trim();
                text = beforeLink + visibleToUser + afterLink;
                mLink = mPatLink.matcher(text);
                continue;
            }

            if (unformattedLink.contains("|")) {
                if (unformattedLink.matches("(.*?),(.*?)\\|\\]\\]")) {
                    // [[Seattle, Washington|]]
                    //System.println("------>Path::1::");
                    visibleToUser = unformattedLink.substring(0, unformattedLink.indexOf(","));
                    url = unformattedLink.replaceAll(regToTrim, "").trim().replaceAll(" ", "_");
                    url = url.substring(0, 1).toUpperCase() + url.substring(1);
                } else if (unformattedLink.matches("(.*?)\\((.*?)\\)\\|\\]\\]")) {
                    // [[Wikipedia:Manual of Style (headings)|]]
                    //System.println("------->Path::2::");
                    visibleToUser = unformattedLink.substring(0, unformattedLink.indexOf("("));
                    url = unformattedLink.replaceAll(regToTrim, "").trim();
                    if (visibleToUser.matches("(.*?):(.*?)")) {
                        visibleToUser = visibleToUser.substring(1 + visibleToUser.indexOf(":"));
                        url = "";
                    }
                } else if (unformattedLink.matches("(.*?)#(.*?)\\|(.+)\\]\\]")) {
                    // [[string#string|string]]
                    //System.println("---------->Path::4::");
                    visibleToUser = unformattedLink.substring(unformattedLink.indexOf("|"), unformattedLink.length());
                    url = unformattedLink
                            .substring(0, unformattedLink.indexOf("|"))
                            .replaceAll(regToTrim, "")
                            .trim()
                            .replaceAll(" ", "_");

                } else if (unformattedLink.matches("(.*?)#(.*?)\\|\\]\\]")) {
                    // string#string|
                    //System.println("------------->Path::5::");
                    visibleToUser = unformattedLink;
                    if (unformattedLink.matches("(.*?):(.*?)#(.*?)\\|\\]\\]")) {
                    } else {
                        url = unformattedLink;
                    }

                } else if (unformattedLink.matches("(.*?)\\|\\]\\]")) {
                    // string|]]
                    // also matches for multiple namespaces woki:fr:crap so
                    // namespace check required
                    //System.println("------->Path::8::");
                    visibleToUser = unformattedLink.substring(1 + unformattedLink.indexOf(":"));

                } else if (unformattedLink.matches("\\[\\[\\|(.+)\\]\\]")) {
                    //System.println("------------------>Path::6.5::");
                    ////System.println("Got u Bitch" + unformattedLink);
                    visibleToUser = unformattedLink.substring(unformattedLink.indexOf("|"));
                    url = unformattedLink
                            .replaceAll(regToTrim, "")
                            .trim()
                            .replaceAll(" ", "_")
                            + "_(disambiguation)";

                } else if (unformattedLink.matches("(.*?)\\|(.+)\\]\\]")) {
                    // string|string]]
                    //System.println("--------->Path::7::");
                    visibleToUser = unformattedLink.substring(unformattedLink.indexOf("|"));
                    //System.println("" + visibleToUser);
                    if (unformattedLink.matches("(.*?):(.*?)\\|(.*?)")) {
                        //System.println("WTF--------------> GOT IT");
                        url = "";
                    } else {
                        //System.println("else part");
                        url = unformattedLink
                                .substring(0, unformattedLink.indexOf("|"))
                                .replaceAll(regToTrim, "")
                                .replaceAll(" ", "_");
                        //System.println("url=" + url);
                    }
                }
            } else {
                if (unformattedLink.matches("(.*?)#(.*?)")) {
                    // string#string or #string will remain what they are
                    //////System.println("----------------\t\t\t\t\t----------\t\t\t\t\t---------------------------->Path::6::");
                    visibleToUser = unformattedLink;
                    url = visibleToUser.replaceAll(regToTrim, "").replaceAll(" ", "_");

                } else if (unformattedLink.matches("\\[\\[[\\w\\s]+\\]\\]")) {
                    // word word no special characters
                    //////System.println("---------------------\t\t\t\t\t-------------\t\t\t\t\t-------------------->Path::9::");
                    visibleToUser = unformattedLink;
                    url = visibleToUser.replaceAll("[\\[\\]\\|]", "").replaceAll(" ", "_");

                } else if (unformattedLink.matches("\\[\\[(.*?)\\]\\]")) {
                    //////System.println("---------------------\t\t\t\t\t-------------\t\t\t\t\t-------------------->Path::10::->"	+ unformattedLink);
                    visibleToUser = unformattedLink;
                    if (unformattedLink.contains(":")) {
                        url = "";
                    } else {
                        url = visibleToUser.replaceAll("[\\[\\]\\|]", "").replaceAll(" ", "_");
                    }
                } else if (unformattedLink.matches("(.*?)\\]\\]")) {
                    // word word no special characters //////System.println(
                    //////System.println("---------------------\t\t\t\t\t-------------\t\t\t\t\t-------------------->Path::11::");
                    visibleToUser = unformattedLink;
                    url = visibleToUser.replaceAll("[\\[\\]\\|]", "").replaceAll(" ", "_");
                } else {
                    //System.println("\n\n----------->CouldNotEval="+ unformattedLink+ "--------------------\n\n");
                }
            }
            //System.println("fafadfdsafdsafadf");
            visibleToUser = visibleToUser.replaceAll("[\\[\\]\\|]", "").trim();
            text = visibleToUser = beforeLink + visibleToUser + afterLink;
            url = WikipediaParser.capitalize(url).trim();
            //System.println("qqqqqqqqq");
            if (url.length() != 0 && objWikiDocument != null) {
                objWikiDocument.addLink(url);
            }
            //System.println("4444444");
            //System.println("Text---------->" + visibleToUser);
            //System.println("url-->" + WikipediaParser.capitalize(url));
            //System.println("url=" + WikipediaParser.capitalize(url) + "\n\n\n");
            mLink = mPatLink.matcher(text);
        }

        return new String[]{visibleToUser.trim(), url};
    }
    private static String capitalize(String url) {
		if( url == "" ) {
			return "";
		}
		url = url.substring(0, 1).toUpperCase() + url.substring(1);
		return url.replaceAll("\\s", "_");
	}
//===TODO=======
//This method is called from outside and it is the single point of entry to Wikipedia parser.
// The input text is cleaned and then store into wikipedia object.
//	output-> objWikiDocument.
    public WikipediaDocument getDocumentInstance(int id, String date, String author, String title, String text) throws ParseException {
        // Validate parameters TODO
        try {
            objWikiDocument = new WikipediaDocument(id, date, author, title);
            
           // .println("Wikipedia Parsing start........");
            
            Matcher matchCate = mRemoveCategories.matcher(text);
		StringBuffer categories = new StringBuffer();
		while(matchCate.find()) {
			categories.append(matchCate.group());
		}
		text = matchCate.replaceAll("");
		//This will add the categories in the collection
		WikipediaParser.parseLinks(categories.toString());
		//this is replace links in text and add link to the collection
		
            //strParsedText = parseSectionTitle(parseTextFormatting(parseTagFormatting(parseTemplates(text)))).trim();
            strParsedText = parseListItem(parseTextFormatting(parseTagFormatting(parseTemplates(text))));
            text = WikipediaParser.parseLinks(strParsedText)[0];
            strParsedText = parseSectionTitle(strParsedText);
            String strTitle = "";
            String strSectionData = "";
            if (lstSections != null && lstSectionsData != null) {
                Iterator it = lstSections.iterator();
                Iterator itSectionData = lstSectionsData.iterator();
                while (it.hasNext()) {
                    strTitle = (String) it.next();
                    strSectionData = (String) itSectionData.next();
                    objWikiDocument.addSection(strTitle, strSectionData);
                }
            }
            //.println("Wikipedia Parsing end........");

        } catch (Exception ex) {
            System.err.println("Error in WikiPediaParser:getInstance-" + ex.getMessage());
        }
        return objWikiDocument;
    }
}
