package de.hybris.platform.util;

import de.hybris.platform.jalo.JaloSystemException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SearchTools
{
    public static String IN(Collection coll, boolean asString)
    {
        StringBuilder sb = new StringBuilder();
        IN(sb, coll, asString);
        return sb.toString();
    }


    public static void IN(StringBuilder sb, Collection coll, boolean asString)
    {
        sb.append("IN(");
        if(asString)
        {
            for(Iterator it = coll.iterator(); it.hasNext(); )
            {
                asString(sb, it.next());
                if(it.hasNext())
                {
                    sb.append(",");
                }
            }
        }
        else
        {
            for(Iterator<E> it = coll.iterator(); it.hasNext(); )
            {
                sb.append(it.next().toString());
                if(it.hasNext())
                {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
    }


    public static String asString(Object o)
    {
        StringBuilder sb = new StringBuilder();
        asString(sb, o);
        return sb.toString();
    }


    public static void asString(StringBuilder sb, Object o)
    {
        sb.append("'");
        sb.append(o.toString());
        sb.append("'");
    }


    public static boolean isEMPTYPattern(String pattern)
    {
        return pattern.trim().equals("%");
    }


    public static boolean isLIKEPattern(String pattern)
    {
        return (pattern != null && (pattern.indexOf('%') > -1 || pattern.indexOf('_') > -1));
    }


    public static boolean writeAttributeStatement(StringBuilder buffer, String attributeName, String valueName, Object attributes, Object values, boolean ignoreCase, boolean attributeORMode, boolean valueORMode, String connectionOP)
    {
        if(attributes != null && values != null)
        {
            int oldLength = buffer.length();
            boolean empty = true;
            if(connectionOP != null)
            {
                buffer.append(connectionOP);
            }
            String innerConnectionOp = attributeORMode ? " OR " : " AND ";
            buffer.append(" ( ");
            if(attributes instanceof List)
            {
                if(!(values instanceof List))
                {
                    throw new JaloSystemException(null, "multiple attribute search requires values being passed as List!", 0);
                }
                List<E> aList = (List)attributes;
                List vList = (List)values;
                int aSize = aList.size();
                int lastLength = buffer.length();
                for(int i = 0; i < aSize; i++)
                {
                    if(!empty)
                    {
                        buffer.append(innerConnectionOp);
                    }
                    buffer.append(" ( ");
                    String a = aList.get(i).toString();
                    if(ignoreCase)
                    {
                        buffer.append("UPPER(");
                    }
                    buffer.append(attributeName);
                    if(ignoreCase)
                    {
                        buffer.append(")");
                    }
                    buffer.append(isLIKEPattern(a) ? " LIKE " : " = ");
                    if(ignoreCase)
                    {
                        buffer.append("UPPER(");
                    }
                    asString(buffer, a);
                    if(ignoreCase)
                    {
                        buffer.append(")");
                    }
                    if(writeLikeStmt(buffer, valueName, vList.get(i), ignoreCase, valueORMode, " AND "))
                    {
                        buffer.append(" ) ");
                        lastLength = buffer.length();
                        empty = false;
                    }
                    else
                    {
                        buffer.setLength(lastLength);
                    }
                }
            }
            else
            {
                String a = attributes.toString();
                if(ignoreCase)
                {
                    buffer.append("UPPER(");
                }
                buffer.append(attributeName);
                if(ignoreCase)
                {
                    buffer.append(")");
                }
                buffer.append(isLIKEPattern(a) ? " LIKE " : " = ");
                if(ignoreCase)
                {
                    buffer.append("UPPER(");
                }
                asString(buffer, a);
                if(ignoreCase)
                {
                    buffer.append(")");
                }
                empty = !writeLikeStmt(buffer, valueName, values, ignoreCase, valueORMode, " AND ");
            }
            if(empty)
            {
                buffer.setLength(oldLength);
                return false;
            }
            buffer.append(" ) ");
            return true;
        }
        return false;
    }


    public static boolean writeLikeStmt(StringBuilder buffer, String attribute, Object value, boolean ignoreCase, boolean orMode, String connectionOP)
    {
        if(value != null)
        {
            int oldLength = buffer.length();
            boolean empty = true;
            if(connectionOP != null)
            {
                buffer.append(connectionOP);
            }
            String innerConnectionOp = orMode ? " OR " : " AND ";
            buffer.append(" ( ");
            if(value instanceof Collection)
            {
                for(Iterator<E> it = ((Collection)value).iterator(); it.hasNext(); )
                {
                    String v = it.next().toString();
                    if(!isEMPTYPattern(v))
                    {
                        if(ignoreCase)
                        {
                            buffer.append(" UPPER(");
                        }
                        buffer.append(attribute);
                        if(ignoreCase)
                        {
                            buffer.append(") ");
                        }
                        buffer.append(isLIKEPattern(v) ? " LIKE " : " = ");
                        if(ignoreCase)
                        {
                            buffer.append("UPPER( ");
                        }
                        asString(buffer, v);
                        if(ignoreCase)
                        {
                            buffer.append(") ");
                        }
                        if(it.hasNext())
                        {
                            buffer.append(innerConnectionOp);
                        }
                        empty = false;
                    }
                }
            }
            else
            {
                String v = value.toString();
                if(!isEMPTYPattern(v))
                {
                    if(ignoreCase)
                    {
                        buffer.append(" UPPER(");
                    }
                    buffer.append(attribute);
                    if(ignoreCase)
                    {
                        buffer.append(" ) ");
                    }
                    buffer.append(isLIKEPattern(v) ? " LIKE " : " = ");
                    if(ignoreCase)
                    {
                        buffer.append(" UPPER( ");
                    }
                    asString(buffer, v);
                    if(ignoreCase)
                    {
                        buffer.append(" ) ");
                    }
                    empty = false;
                }
            }
            if(empty)
            {
                buffer.setLength(oldLength);
                return false;
            }
            buffer.append(" ) ");
            return true;
        }
        return false;
    }
}
