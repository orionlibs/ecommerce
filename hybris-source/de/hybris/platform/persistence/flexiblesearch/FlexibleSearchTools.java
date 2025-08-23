package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.LinkedList;
import java.util.List;

final class FlexibleSearchTools
{
    public static final String PK_PROP = Item.PK;
    public static final String TYPE_PROP = Item.TYPE;
    private static final int EMPTY = 0;
    private static final int GOT_TYPE = 1;
    private static final int GOT_ATTRIBUTE = 2;
    private static final int READING_LANGUAGE = 3;
    private static final int GOT_LANGUAGE = 4;


    public static TableFieldDescriptor splitField(String field)
    {
        String type = null;
        String attribute = null;
        String lang = null;
        String options = null;
        int lastPos = 0;
        int state = 0;
        for(int pos = 0; pos < field.length(); pos++)
        {
            char character = field.charAt(pos);
            if(character == '.')
            {
                if(state != 0)
                {
                    continue;
                }
                String str = field.substring(lastPos, pos).trim();
                if(str.length() == 0)
                {
                    str = null;
                }
                type = str;
                lastPos = pos + 1;
                state = 1;
            }
            if(character == ':')
            {
                if(state != 3)
                {
                    String str = field.substring(lastPos, pos).trim();
                    if(str.length() == 0)
                    {
                        str = null;
                    }
                    lastPos = pos + 1;
                    if(state == 0)
                    {
                        type = str;
                        state = 1;
                    }
                    else if(state == 1)
                    {
                        if(str == null)
                        {
                            throw new IllegalArgumentException("wrong search field definiton '" + field + "' empty attribute qualifier at " + pos);
                        }
                        attribute = str;
                        state = 2;
                    }
                    else if(state == 2 || state == 4)
                    {
                        break;
                    }
                }
            }
            else if(character == '[')
            {
                if(state == 3)
                {
                    throw new IllegalArgumentException("wrong search field definiton '" + field + "' due to duplicate '[' at " + pos);
                }
                if(state != 0 && state != 1)
                {
                    throw new IllegalArgumentException("wrong search field definiton '" + field + "' found '[' outside attribute spec at " + pos);
                }
                String str = field.substring(lastPos, pos).trim();
                if(str.length() == 0)
                {
                    str = null;
                }
                if(str == null)
                {
                    throw new IllegalArgumentException("wrong search field definiton '" + field + "' missing attribute qualifier before '[' at " + pos);
                }
                attribute = str;
                lastPos = pos + 1;
                state = 3;
            }
            else if(character == ']')
            {
                if(state != 3)
                {
                    throw new IllegalArgumentException("wrong search field definiton '" + field + "' due to single ']' at " + pos);
                }
                String str = field.substring(lastPos, pos).trim();
                if(str.length() == 0)
                {
                    str = null;
                }
                lang = str;
                lastPos = pos + 1;
                state = 4;
            }
            continue;
        }
        if(lastPos < field.length())
        {
            String str = field.substring(lastPos).trim();
            if(str.length() == 0)
            {
                str = null;
            }
            if(state == 0 || state == 1)
            {
                attribute = str;
            }
            else if(state == 2 || state == 4)
            {
                options = str;
            }
            else
            {
                throw new IllegalArgumentException("wrong search field definiton '" + field + "' unecpected state " + state + " at " + lastPos);
            }
        }
        return new TableFieldDescriptor(parseTypeQualifier(type), attribute, lang, options);
    }


    public static Object parseTypeQualifier(String str)
    {
        if(str != null)
        {
            if(str.length() == 0)
            {
                return null;
            }
            boolean allDigits = true;
            for(int i = 0; allDigits && i < str.length(); i++)
            {
                allDigits = Character.isDigit(str.charAt(i));
            }
            return allDigits ? Integer.valueOf(Integer.parseInt(str) - 1) : str;
        }
        return null;
    }


    public static boolean ignoreLanguage(PK languagePK)
    {
        return (languagePK != null && PK.NULL_PK.equals(languagePK));
    }


    public static boolean isSpecialField(String name)
    {
        return (PK_PROP.equals(name) || TYPE_PROP.equals(name));
    }


    public static StringBuilder replace(ParsedText text, String expr, String open, String close, Translator trans) throws FlexibleSearchException
    {
        StringBuilder res = new StringBuilder();
        int openOffset = open.length();
        int closeOffset = close.length();
        int last = 0;
        int start = expr.indexOf(open);
        int current = (start >= 0) ? (start + openOffset) : -1;
        int openCount = 0;
        while(current >= 0)
        {
            int nextClose = expr.indexOf(close, current);
            int nextOpen = expr.indexOf(open, current);
            if(nextClose >= 0)
            {
                if(nextOpen >= 0 && nextOpen < nextClose)
                {
                    openCount++;
                    current = nextOpen + openOffset;
                    continue;
                }
                if(openCount > 0)
                {
                    openCount--;
                    current = nextClose + closeOffset;
                    continue;
                }
                res.append(expr.substring(last, start));
                res.append(trans.translate(res.length(), text, expr.substring(start + openOffset, nextClose)));
                last = nextClose + closeOffset;
                start = expr.indexOf(open, last);
                current = (start >= 0) ? (start + openOffset) : -1;
                continue;
            }
            throw new FlexibleSearchException(null, "missing '" + close + "' for '" + open + "' at " + current + " in '" + expr + "'", 0);
        }
        if(openCount != 0)
        {
            throw new FlexibleSearchException(null, "illegal flexible text '" + expr + "' (opencount=" + openCount + ",start=" + start + ",current=" + current + ",last=" + last + ")", 0);
        }
        if(last < expr.length())
        {
            res.append((last == 0) ? expr : expr.substring(last));
        }
        return res;
    }


    public static List split(String text, String delimiter, boolean ignoreCase)
    {
        if(text == null)
        {
            return null;
        }
        String searchText = ignoreCase ? text.toLowerCase(LocaleHelper.getPersistenceLocale()) : text;
        String searchDelimiter = ignoreCase ? delimiter.toLowerCase(LocaleHelper.getPersistenceLocale()) : delimiter;
        int offset = delimiter.length();
        List<String> ret = new LinkedList();
        int pos = ParsedText.getWholeWordTokenPosition(searchText, searchDelimiter);
        int last = 0;
        while(pos >= 0)
        {
            ret.add(text.substring(last, pos).trim());
            last = pos + offset;
            pos = ParsedText.getWholeWordTokenPosition(searchText, searchDelimiter, last);
        }
        ret.add((last > 0) ? text.substring(last).trim() : text.trim());
        return ret;
    }
}
