package de.hybris.platform.impex.jalo.header;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.translators.HeaderCellTranslator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public abstract class AbstractDescriptor
{
    private final String src;
    private HeaderCellTranslator ownTranslator;
    private final DescriptorParams descrData;


    public AbstractDescriptor(String expr, DescriptorParams params) throws HeaderValidationException
    {
        this.src = expr;
        this.descrData = params;
    }


    public AbstractDescriptor(String expr) throws HeaderValidationException
    {
        this.src = expr;
        this.ownTranslator = createTranslator(expr);
        this.descrData = getDescriptorTranslator().translate(this, expr);
    }


    public String getDefinitionSrc()
    {
        return this.src;
    }


    protected HeaderCellTranslator getDescriptorTranslator()
    {
        return this.ownTranslator;
    }


    protected abstract HeaderCellTranslator createTranslator(String paramString);


    public DescriptorParams getDescriptorData()
    {
        return this.descrData;
    }


    public static Map<String, String> extractModifiersMap(String expression) throws HeaderValidationException
    {
        CaseInsensitiveMap<String, String> caseInsensitiveMap;
        String expr = expression;
        Map<String, String> modifiersMap = null;
        int last = 0;
        String key = null;
        String value = null;
        boolean quoted = false;
        for(int i = 0; i < expr.length(); i++)
        {
            char c = expr.charAt(i);
            if(!quoted && c == '=')
            {
                key = expr.substring(last, i).trim().toLowerCase();
                last = i + 1;
            }
            else if(key != null && '\'' == c)
            {
                if(!quoted)
                {
                    quoted = true;
                    last = i + 1;
                }
                else if(expr.length() > i + 1 && expr.charAt(i + 1) == '\'')
                {
                    expr = expr.substring(0, i) + expr.substring(0, i);
                }
                else
                {
                    value = expr.substring(last, i).trim();
                    quoted = false;
                }
            }
            if(!quoted && (',' == c || i + 1 == expr.length()))
            {
                if(key != null)
                {
                    if(modifiersMap == null)
                    {
                        caseInsensitiveMap = new CaseInsensitiveMap();
                    }
                    if(caseInsensitiveMap.get(key) != null)
                    {
                        throw new HeaderValidationException("duplicate modifier '" + key + "' within '" + expr + "' - cannot parse", 0);
                    }
                    caseInsensitiveMap.put(key,
                                    (value != null) ? value : expr.substring(last, (i + 1 == expr.length()) ? (i + 1) : i).trim());
                }
                last = i + 1;
                key = null;
                value = null;
                quoted = false;
            }
        }
        if(quoted)
        {
            throw new HeaderValidationException("missing closing quote character ' within mofidier expression: " + expression, 0);
        }
        return (caseInsensitiveMap != null) ? (Map<String, String>)caseInsensitiveMap : Collections.EMPTY_MAP;
    }


    public static final List<ColumnParams>[] extractItemPathElements(String expr) throws HeaderValidationException
    {
        List<ColumnParams> currentPattern = null;
        boolean nextIsAlternative = false;
        boolean gotAlternatives = false;
        int last = 0;
        String qualifier = null;
        String subPattern = null;
        Map<String, String> modifiers = null;
        int s = expr.length();
        for(int i = 0; i < s; i++)
        {
            int c = expr.charAt(i);
            if(i + 1 == s || c == 124 || c == 44)
            {
                if(qualifier == null)
                {
                    qualifier = expr.substring(last, (i + 1 == s) ? (i + 1) : i).trim();
                }
                if(qualifier.length() > 0)
                {
                    ColumnParams el = new ColumnParams(qualifier, (subPattern != null) ? (List[])extractItemPathElements(subPattern) : null);
                    if(modifiers != null)
                    {
                        el.addAllModifier(modifiers);
                    }
                    if(currentPattern == null)
                    {
                        currentPattern = new ArrayList();
                    }
                    if(nextIsAlternative)
                    {
                        if(currentPattern.isEmpty())
                        {
                            throw new HeaderValidationException("unexpected '|' - no preceding expression found for alternative operator", 0);
                        }
                        nextIsAlternative = false;
                        gotAlternatives = true;
                        Object previous = currentPattern.get(currentPattern.size() - 1);
                        if(previous instanceof List)
                        {
                            ((List<ColumnParams>)previous).add(el);
                        }
                        else
                        {
                            currentPattern.set(currentPattern.size() - 1, new ArrayList(Arrays.asList(new Object[] {previous, el})));
                        }
                    }
                    else
                    {
                        currentPattern.add(el);
                    }
                    nextIsAlternative = (c == 124);
                }
                last = i + 1;
                qualifier = null;
                subPattern = null;
                modifiers = null;
            }
            else if(c == 40)
            {
                if(subPattern != null)
                {
                    throw new HeaderValidationException("invalid item path expression - duplicate sub-pattern found at " + i, 0);
                }
                if(qualifier == null)
                {
                    qualifier = expr.substring(last, i).trim();
                }
                int patternStart = i + 1;
                int patternEnd = -1;
                for(int current = i + 1, nested = 0; patternEnd == -1 && current < s; current++)
                {
                    char cc = expr.charAt(current);
                    if(')' == cc)
                    {
                        if(nested == 0)
                        {
                            patternEnd = current;
                        }
                        else
                        {
                            nested--;
                        }
                    }
                    else if('(' == cc)
                    {
                        nested++;
                    }
                }
                if(patternEnd == -1)
                {
                    throw new HeaderValidationException("invalid sub-pattern expression in '" + expr + "' - missing ')' for '(' at " + patternStart, 0);
                }
                subPattern = expr.substring(patternStart, patternEnd).trim();
                i = (patternEnd + 1 == s) ? (patternEnd - 1) : patternEnd;
            }
            else if(c == 91)
            {
                if(modifiers != null)
                {
                    throw new HeaderValidationException("invalid item path expression - duplicate modifiers found at " + i, 0);
                }
                if(qualifier == null)
                {
                    qualifier = expr.substring(last, i).trim();
                }
                int modStart = i + 1;
                int modEnd = -1;
                for(int current = i + 1; modEnd == -1 && current < s; current++)
                {
                    char cc = expr.charAt(current);
                    if(']' == cc)
                    {
                        modEnd = current;
                    }
                    else if('(' == cc)
                    {
                        throw new HeaderValidationException("invalid modifiers expression at " + i + " - found nested '['", 0);
                    }
                }
                if(modEnd == -1)
                {
                    throw new HeaderValidationException("invalid modifiers expression in '" + expr + "' - missing ')' for '(' at " + modStart, 0);
                }
                modifiers = extractModifiersMap(expr.substring(modStart, modEnd).trim());
                i = (modEnd + 1 == s) ? (modEnd - 1) : modEnd;
            }
        }
        (new List[1])[0] = currentPattern;
        return (currentPattern != null) ? (gotAlternatives ? (List<ColumnParams>[])expandAlternatives(currentPattern) : (List<ColumnParams>[])new List[1]) :
                        null;
    }


    private static final List[] expandAlternatives(List patternList)
    {
        List<?> lists = Collections.singletonList(new ArrayList(Arrays.asList(new Object[patternList.size()])));
        for(int i = 0, s = patternList.size(); i < s; i++)
        {
            Object element = patternList.get(i);
            if(element instanceof List)
            {
                lists = copyLists((List)lists, i, (List)element);
            }
            else
            {
                for(Iterator<?> iter = lists.iterator(); iter.hasNext(); )
                {
                    List<Object> l = (List)iter.next();
                    l.set(i, element);
                }
            }
        }
        return lists.<List>toArray(new List[lists.size()]);
    }


    private static final List copyLists(List<List> lists, int position, List alternatives)
    {
        List<List> ret = new LinkedList<>();
        for(Iterator<List> iter = lists.iterator(); iter.hasNext(); )
        {
            List<?> l = iter.next();
            ret.add(l);
            l.set(position, alternatives.get(0));
            for(int i = 1; i < alternatives.size(); i++)
            {
                List newOne = new ArrayList(l);
                ret.add(newOne);
                newOne.set(position, alternatives.get(i));
            }
        }
        return ret;
    }


    public static final HeaderParams parseHeaderDescriptor(String expr) throws HeaderValidationException
    {
        String mode, tmp = expr.toLowerCase(LocaleHelper.getPersistenceLocale());
        if(tmp.startsWith(ImpExConstants.Syntax.Mode.INSERT_UPDATE))
        {
            mode = ImpExConstants.Syntax.Mode.INSERT_UPDATE;
        }
        else if(tmp.startsWith(ImpExConstants.Syntax.Mode.INSERT))
        {
            mode = ImpExConstants.Syntax.Mode.INSERT;
        }
        else if(tmp.startsWith(ImpExConstants.Syntax.Mode.UPDATE))
        {
            mode = ImpExConstants.Syntax.Mode.UPDATE;
        }
        else if(tmp.startsWith(ImpExConstants.Syntax.Mode.REMOVE))
        {
            mode = ImpExConstants.Syntax.Mode.REMOVE;
        }
        else
        {
            throw new HeaderValidationException("invalid start of header - expected one of (" + ImpExConstants.Syntax.Mode.INSERT + "," + ImpExConstants.Syntax.Mode.INSERT_UPDATE + "," + ImpExConstants.Syntax.Mode.UPDATE + "," + ImpExConstants.Syntax.Mode.REMOVE + ")", 0);
        }
        String type = null;
        Map<String, String> modifiers = null;
        for(int i = mode.length(), s = expr.length(); i < s; i++)
        {
            char c = expr.charAt(i);
            if('(' == c)
            {
                throw new HeaderValidationException("invalid header expression at position " + i + " within expression '" + expr + "' - unexpected '('", 0);
            }
            if('[' == c)
            {
                if(type == null)
                {
                    type = expr.substring(mode.length(), i).trim();
                }
                int modStart = i + 1;
                int modEnd = -1;
                for(int current = i + 1, nested = 0; modEnd == -1 && current < s; current++)
                {
                    char cc = expr.charAt(current);
                    if(']' == cc)
                    {
                        if(nested == 0)
                        {
                            modEnd = current;
                        }
                        else
                        {
                            nested--;
                        }
                    }
                    else if('[' == cc)
                    {
                        nested++;
                    }
                }
                if(modEnd == -1)
                {
                    throw new HeaderValidationException("invalid modifier expression in '" + expr + "' - missing ']' for '[' at " + modStart, 0);
                }
                modifiers = extractModifiersMap(expr.substring(modStart, modEnd));
                i = modEnd;
            }
        }
        if(type == null && modifiers == null)
        {
            type = expr.substring(mode.length()).trim();
        }
        HeaderParams ret = new HeaderParams(mode, type);
        if(modifiers != null)
        {
            ret.addAllModifier(modifiers);
        }
        return ret;
    }


    public static final ColumnParams parseColumnDescriptor(String expr) throws HeaderValidationException
    {
        String qualifier = null;
        Map<String, String> modifiers = null;
        List[] patternLists = null;
        for(int i = 0; i < expr.length(); i++)
        {
            char c = expr.charAt(i);
            if('(' == c)
            {
                if(patternLists != null)
                {
                    throw new HeaderValidationException("invalid item pattern expression in '" + expr + "' - unexpected second item path expression at " + i, 0);
                }
                if(qualifier == null)
                {
                    qualifier = expr.substring(0, i).trim();
                }
                int patternStart = i + 1;
                int patternEnd = -1;
                for(int current = i + 1, nested = 0; patternEnd == -1 && current < expr.length(); current++)
                {
                    char cc = expr.charAt(current);
                    if(')' == cc)
                    {
                        if(nested == 0)
                        {
                            patternEnd = current;
                        }
                        else
                        {
                            nested--;
                        }
                    }
                    else if('(' == cc)
                    {
                        nested++;
                    }
                }
                if(patternEnd == -1)
                {
                    throw new HeaderValidationException("invalid item pattern expression in '" + expr + "' - missing ')' for '(' at " + patternStart, 0);
                }
                patternLists = (List[])extractItemPathElements(expr.substring(patternStart, patternEnd));
                i = patternEnd;
            }
            else if('[' == c)
            {
                if(qualifier == null)
                {
                    qualifier = expr.substring(0, i).trim();
                }
                int modStart = i + 1;
                int modEnd = -1;
                for(int current = i + 1, nested = 0; modEnd == -1 && current < expr.length(); current++)
                {
                    char cc = expr.charAt(current);
                    if(']' == cc)
                    {
                        if(nested == 0)
                        {
                            modEnd = current;
                        }
                        else
                        {
                            nested--;
                        }
                    }
                    else if('[' == cc)
                    {
                        nested++;
                    }
                }
                if(modEnd == -1)
                {
                    throw new HeaderValidationException("invalid item modifier expression in '" + expr + "' - missing ']' for '[' at " + modStart, 0);
                }
                if(modifiers != null)
                {
                    CaseInsensitiveMap<String, String> caseInsensitiveMap;
                    if(!(modifiers instanceof CaseInsensitiveMap))
                    {
                        caseInsensitiveMap = new CaseInsensitiveMap(modifiers);
                    }
                    caseInsensitiveMap.putAll(extractModifiersMap(expr.substring(modStart, modEnd)));
                }
                else
                {
                    modifiers = extractModifiersMap(expr.substring(modStart, modEnd));
                }
                i = modEnd;
            }
            else if(modifiers != null || patternLists != null)
            {
                throw new HeaderValidationException("invalid item expression '" + expr + "' - unexpected expression at " + i, 0);
            }
        }
        ColumnParams ret = new ColumnParams((qualifier == null) ? (qualifier = expr.trim()) : qualifier, patternLists);
        if(modifiers != null)
        {
            ret.addAllModifier(modifiers);
        }
        return ret;
    }


    protected abstract void validate() throws HeaderValidationException;
}
