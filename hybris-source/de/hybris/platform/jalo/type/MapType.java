package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class MapType extends Type
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute(CODE, allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute(ARGUMENT_TYPE, allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute(RETURN_TYPE, allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a MapType", 0);
        }
        return (Item)JaloSession.getCurrentSession().getTypeManager().createMapType((String)allAttributes.get(CODE), (Type)allAttributes
                        .get(ARGUMENT_TYPE), (Type)allAttributes
                        .get(RETURN_TYPE));
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove(CODE);
        copyMap.remove(ARGUMENT_TYPE);
        copyMap.remove(RETURN_TYPE);
        return copyMap;
    }


    public static int[] getEscapedMapPositions(String escaped, int startFrom)
    {
        escaped = escaped.trim();
        int start = escaped.indexOf('{', startFrom);
        while(start > 0 && escaped.charAt(start - 1) == '\\')
        {
            start = escaped.indexOf('{', start + 1);
        }
        if(start < 0)
        {
            throw new IllegalArgumentException("cannot find begin of escaped map within '" + escaped + "' starting at " + start);
        }
        int end = escaped.indexOf('}', start + 1);
        while(end >= 0 && escaped.charAt(end - 1) == '\\')
        {
            end = escaped.indexOf('}', end + 1);
        }
        if(end < 0)
        {
            throw new IllegalArgumentException("cannot find end of escaped map within '" + escaped + "' starting at " + start + " (found begin at " + start + ")");
        }
        return new int[] {start, end};
    }


    protected static final String escapeForMap(String raw)
    {
        return AtomicType.escape(AtomicType.escape(AtomicType.escape(AtomicType.escape(raw, '='), ','), '{'), '}');
    }


    protected static final String unescapeForMap(String escaped)
    {
        return AtomicType.unescape(AtomicType.unescape(AtomicType.unescape(AtomicType.unescape(escaped, '='), ','), '{'), '}');
    }


    public String toString(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return "<NULL>";
        }
        if(((Map)value).isEmpty())
        {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        Type argumentType = getArgumentType(ctx);
        Type returnType = getReturnType(ctx);
        for(Iterator<Map.Entry> it = ((Map)value).entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry e = it.next();
            sb.append(escapeForMap(argumentType.toString(ctx, e.getKey())));
            sb.append("=");
            sb.append(escapeForMap(returnType.toString(ctx, e.getValue())));
            if(it.hasNext())
            {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }


    public Object parseValue(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(value == null || "<NULL>".equals(value))
        {
            return null;
        }
        int[] range = getEscapedMapPositions(value, 0);
        String valueStrEscaped = value.substring(range[0] + 1, range[1]);
        if("<NULL>".equals(valueStrEscaped))
        {
            return null;
        }
        Map<Object, Object> ret = new HashMap<>();
        Type argumentType = getArgumentType(ctx);
        Type returnType = getReturnType(ctx);
        int lastFound = 0;
        int lastPos = 0;
        int pos;
        for(pos = valueStrEscaped.indexOf(',', lastPos); pos >= 0; lastPos = pos + 1, pos = valueStrEscaped.indexOf(',', lastPos))
        {
            if(valueStrEscaped.charAt(pos - 1) != '\\')
            {
                String entryStrEscaped = valueStrEscaped.substring(lastFound, pos);
                lastFound = pos + 1;
                int splitPos = entryStrEscaped.indexOf('=');
                String keyStr = unescapeForMap(entryStrEscaped.substring(0, splitPos));
                String valueStr = unescapeForMap(entryStrEscaped.substring(splitPos + 1));
                ret.put(argumentType.parseValue(ctx, keyStr), "<NULL>".equals(valueStr) ? null :
                                returnType.parseValue(ctx, valueStr));
            }
        }
        if(lastFound < valueStrEscaped.length())
        {
            String entryStrEscaped = valueStrEscaped.substring(lastFound);
            int splitPos = entryStrEscaped.indexOf('=');
            ret.put(argumentType.parseValue(ctx, unescapeForMap(entryStrEscaped.substring(0, splitPos))), returnType
                            .parseValue(ctx,
                                            unescapeForMap(entryStrEscaped.substring(splitPos + 1))));
        }
        return ret;
    }


    public static final String ARGUMENT_TYPE = "argumentType".intern();
    public static final String RETURN_TYPE = "returntype".intern();


    public Type getArgumentType()
    {
        return getArgumentType(getSession().getSessionContext());
    }


    public Type getArgumentType(SessionContext ctx)
    {
        return (Type)(new Object(this, ARGUMENT_TYPE, false, true))
                        .get(ctx);
    }


    public Type getReturnType()
    {
        return getReturnType(getSession().getSessionContext());
    }


    public Type getReturnType(SessionContext ctx)
    {
        return (Type)(new Object(this, RETURN_TYPE, false, true))
                        .get(ctx);
    }


    public String getXMLDefinition()
    {
        XMLOutputter xmlOut;
        try
        {
            xmlOut = new XMLOutputter(new StringWriter(), "UTF-8");
            xmlOut.setEscaping(true);
            xmlOut.setLineBreak(LineBreak.UNIX);
            xmlOut.setIndentation("\t");
            xmlOut.setQuotationMark('"');
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloSystemException(e);
        }
        return exportXMLDefinition(xmlOut);
    }


    public String exportXMLDefinition(XMLOutputter xout)
    {
        try
        {
            xout.startTag("maptype");
            xout.attribute("code", getCode());
            xout.attribute("argumenttype", getArgumentType().getCode());
            xout.attribute("returntype", getReturnType().getCode());
            xout.attribute("autocreate", String.valueOf(isAutocreate()));
            xout.attribute("generate", String.valueOf(isGenerate()));
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        return xout.getWriter().toString();
    }


    protected MapTypeImpl getMapTypeImpl()
    {
        return (MapTypeImpl)this.impl;
    }


    public boolean isAssignableFrom(Type type)
    {
        return (type instanceof MapType && (
                        equals(type) || (getArgumentType().isAssignableFrom(((MapType)type).getArgumentType()) && getReturnType()
                                        .isAssignableFrom(((MapType)type).getReturnType()))));
    }


    public boolean isInstance(Object o)
    {
        return (o instanceof Map && typeCheckContent((Map)o));
    }


    public boolean typeCheckContent(Map map)
    {
        if(map.isEmpty())
        {
            return true;
        }
        Type argT = getArgumentType();
        Type retT = getReturnType();
        for(Iterator<Map.Entry> iter = map.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry e = iter.next();
            if(!argT.isInstance(e.getKey()) || (e.getValue() != null && !retT.isInstance(e.getValue())))
            {
                return false;
            }
        }
        return true;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "maptype '" + getCode() + "':{" + getArgumentType().getCode() + "->" + getReturnType().getCode() + "}";
    }
}
