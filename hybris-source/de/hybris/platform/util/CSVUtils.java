package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public final class CSVUtils
{
    public static String unescapeString(String escaped, char[] toUnEscape, boolean doubleCharacter)
    {
        if(escaped == null)
        {
            return null;
        }
        if(escaped.length() == 0)
        {
            return escaped;
        }
        char[] allToUnEscape = toUnEscape;
        if(!doubleCharacter)
        {
            allToUnEscape = new char[toUnEscape.length + 1];
            System.arraycopy(toUnEscape, 0, allToUnEscape, 0, toUnEscape.length);
            allToUnEscape[toUnEscape.length] = '\\';
        }
        StringBuilder sb = new StringBuilder();
        int s = escaped.length();
        for(int i = 0; i < s; i++)
        {
            char c = escaped.charAt(i);
            if(!doubleCharacter)
            {
                if(c == '\\' && i + 1 < s && isSeparator(allToUnEscape, escaped.charAt(i + 1)))
                {
                    c = escaped.charAt(++i);
                }
            }
            else if(isSeparator(allToUnEscape, c) && i + 1 < s && c == escaped.charAt(i + 1))
            {
                i++;
            }
            sb.append(c);
        }
        return sb.toString();
    }


    public static String escapeString(String unescaped, char[] toEscape, boolean doubleCharacter)
    {
        if(unescaped == null)
        {
            return null;
        }
        if(unescaped.length() == 0)
        {
            return unescaped;
        }
        char[] allToEscape = toEscape;
        if(!doubleCharacter)
        {
            allToEscape = new char[toEscape.length + 1];
            System.arraycopy(toEscape, 0, allToEscape, 0, toEscape.length);
            allToEscape[toEscape.length] = '\\';
        }
        StringBuilder ret = new StringBuilder();
        int s = unescaped.length();
        for(int i = 0; i < s; i++)
        {
            char c = unescaped.charAt(i);
            if(isSeparator(allToEscape, c))
            {
                if(doubleCharacter)
                {
                    ret.append(c).append(c);
                }
                else
                {
                    ret.append('\\').append(c);
                }
            }
            else
            {
                ret.append(c);
            }
        }
        return ret.toString();
    }


    public static boolean escapeString(StringBuilder unescaped, char[] toEscape, char[] specialChars, boolean doubleCharacter)
    {
        if(unescaped == null)
        {
            return false;
        }
        if(unescaped.length() == 0)
        {
            return false;
        }
        char[] allToEscape = toEscape;
        if(!doubleCharacter)
        {
            allToEscape = new char[toEscape.length + 1];
            System.arraycopy(toEscape, 0, allToEscape, 0, toEscape.length);
            allToEscape[toEscape.length] = '\\';
        }
        boolean found = false;
        for(int i = 0, s = unescaped.length(); i < s; i++)
        {
            char c = unescaped.charAt(i);
            if(isSeparator(allToEscape, c))
            {
                if(doubleCharacter)
                {
                    unescaped.insert(i, c);
                }
                else
                {
                    unescaped.insert(i, '\\');
                }
                i++;
                s++;
            }
            if(!found && isSeparator(specialChars, c))
            {
                found = true;
            }
        }
        return found;
    }


    public static boolean escapeString(StringBuilder unescaped, String[] toEscape, String[] specialChars, boolean doubleToEscape)
    {
        if(unescaped == null)
        {
            return false;
        }
        if(unescaped.length() == 0)
        {
            return false;
        }
        String[] allToEscape = toEscape;
        if(!doubleToEscape)
        {
            allToEscape = new String[toEscape.length + 1];
            System.arraycopy(toEscape, 0, allToEscape, 0, toEscape.length);
            allToEscape[toEscape.length] = "\\";
        }
        int s = allToEscape.length;
        for(int i = 0; i < s; i++)
        {
            int idx = 0;
            while(idx > -1)
            {
                idx = unescaped.toString().indexOf(allToEscape[i], idx);
                if(idx > -1)
                {
                    if(doubleToEscape)
                    {
                        unescaped.insert(idx, allToEscape[i]);
                    }
                    else
                    {
                        unescaped.insert(idx, '\\');
                    }
                    idx += 2;
                }
            }
        }
        boolean found = false;
        int k = specialChars.length;
        for(int j = 0; j < k; j++)
        {
            int idx = unescaped.toString().indexOf(specialChars[j]);
            if(idx > -1)
            {
                found = true;
                break;
            }
        }
        return found;
    }


    public static boolean isSeparator(char[] tokenisers, char actual)
    {
        int s = tokenisers.length;
        for(int i = 0; i < s; i++)
        {
            if(tokenisers[i] == actual)
            {
                return true;
            }
        }
        return false;
    }


    public static List<String> splitAndUnescape(String line, char[] delimiters, boolean escapedByDoubling)
    {
        if(line == null)
        {
            return null;
        }
        int s = line.length();
        if(s == 0)
        {
            return Collections.EMPTY_LIST;
        }
        List<String> ret = new ArrayList<>();
        int lastDelimiter = 0;
        char c = Character.MIN_VALUE;
        boolean needsUnescaping = false;
        for(int i = 0; i < s; i++)
        {
            c = line.charAt(i);
            if(!escapedByDoubling && c == '\\')
            {
                i++;
                needsUnescaping = true;
            }
            else if(isSeparator(delimiters, c))
            {
                if(escapedByDoubling && i + 1 < s && line.charAt(i + 1) == c)
                {
                    i++;
                    needsUnescaping = true;
                }
                else
                {
                    String str1 = line.substring(lastDelimiter, i).trim();
                    if(needsUnescaping)
                    {
                        str1 = unescapeString(str1, delimiters, escapedByDoubling);
                    }
                    ret.add((str1.length() > 0) ? str1 : null);
                    lastDelimiter = i + 1;
                    needsUnescaping = false;
                }
            }
        }
        String str = (lastDelimiter < s) ? line.substring(lastDelimiter).trim() : null;
        if(needsUnescaping)
        {
            str = unescapeString(str, delimiters, escapedByDoubling);
        }
        ret.add((str != null && str.length() > 0) ? str : null);
        return ret;
    }


    public static String joinAndEscape(List<String> cells, char[] toEscape, char delimiter, boolean escapeByDoubling)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0, s = cells.size(); i < s; i++)
        {
            String cell = cells.get(i);
            if(i > 0)
            {
                sb.append(delimiter);
            }
            if(cell != null)
            {
                (new char[1])[0] = delimiter;
                sb.append(escapeString(cell, (toEscape == null) ? new char[1] : toEscape, escapeByDoubling));
            }
        }
        return sb.toString();
    }


    public static boolean lineStartsWith(String line, char[] prefixes, String appender)
    {
        if(line == null || line.length() == 0 || prefixes == null || prefixes.length == 0)
        {
            return false;
        }
        int s = prefixes.length;
        for(int i = 0; i < s; i++)
        {
            if(line.charAt(0) == prefixes[i] && line.startsWith("" + prefixes[i] + prefixes[i]))
            {
                return true;
            }
        }
        return false;
    }


    public static String buildCsvLine(Map fields, char fieldSeparator, char textSeparator, String lineBreak)
    {
        BitSet cells = new BitSet();
        int max = 0;
        for(Object o : fields.entrySet())
        {
            Map.Entry e = (Map.Entry)o;
            int idx = ((Integer)e.getKey()).intValue();
            if(idx < 0)
            {
                throw new IllegalArgumentException("cell index < 0 (got " + idx + "=>" + e.getValue() + ")");
            }
            cells.set(idx);
            max = Math.max(max, idx);
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i <= max; i++)
        {
            if(i > 0)
            {
                sb.append(fieldSeparator);
            }
            if(cells.get(i))
            {
                String str = (String)fields.get(Integer.valueOf(i));
                if(str != null)
                {
                    sb.append(createCSVField(str, fieldSeparator, textSeparator, lineBreak));
                }
            }
        }
        return sb.toString();
    }


    private static String createCSVField(String fieldData, char fieldSeparator, char textSeparator, String lineBreak)
    {
        if(StringUtils.isBlank(fieldData))
        {
            return "";
        }
        Set<String> specials = new HashSet<>(Arrays.asList(CSVConstants.LINE_SEPARATORS));
        specials.add(Character.toString(textSeparator));
        specials.add(Character.toString(fieldSeparator));
        specials.add(lineBreak);
        StringBuilder buf = new StringBuilder(fieldData);
        if(escapeString(buf, new String[] {Character.toString(textSeparator)}, specials
                        .<String>toArray(new String[specials.size()]), true))
        {
            buf.insert(0, textSeparator);
            buf.append(textSeparator);
        }
        return buf.toString();
    }
}
