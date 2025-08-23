package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class CollectionType extends Type
{
    public static final String TYPE_OF_COLLECTION_ENUM_CODE = "TypeOfCollectionEnum";
    public static final int COLLECTION = 0;
    public static final String COLLECTION_ENUM_CODE = "collection";
    public static final int SET = 1;
    public static final String SET_ENUM_CODE = "set";
    public static final int LIST = 2;
    public static final String LIST_ENUM_CODE = "list";
    @Deprecated(since = "ages", forRemoval = false)
    public static final int SORTED_SET = 3;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String SORTED_SET_ENUM_CODE = "sortedset";


    protected int convertTypeOfCollectionEnumToInt(EnumerationValue enumeration) throws JaloInvalidParameterException
    {
        String code = (enumeration != null) ? enumeration.getCode() : null;
        if(enumeration == null || "collection".equalsIgnoreCase(code))
        {
            return 0;
        }
        if("set".equalsIgnoreCase(code))
        {
            return 1;
        }
        if("list".equalsIgnoreCase(code))
        {
            return 2;
        }
        if("sortedset".equalsIgnoreCase(code))
        {
            return 3;
        }
        throw new JaloInvalidParameterException("illegal type or collection enum " + enumeration, 0);
    }


    protected EnumerationValue convertTypeOfCollectionIntToEnum(int type) throws JaloInvalidParameterException
    {
        EnumerationManager em = getSession().getEnumerationManager();
        EnumerationType t = em.getEnumerationType("TypeOfCollectionEnum");
        switch(type)
        {
            case 0:
                return em.getEnumerationValue(t, "collection");
            case 1:
                return em.getEnumerationValue(t, "set");
            case 2:
                return em.getEnumerationValue(t, "list");
            case 3:
                return em.getEnumerationValue(t, "sortedset");
        }
        throw new JaloInvalidParameterException("illegal type of collection int " + type, 0);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute(CODE, allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("elementType", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a CollectionType", 0);
        }
        return (Item)JaloSession.getCurrentSession().getTypeManager().createCollectionType((String)allAttributes
                                        .get(CODE), (Type)allAttributes.get("elementType"),
                        convertTypeOfCollectionEnumToInt((EnumerationValue)allAttributes.get("typeOfCollection")));
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove(CODE);
        copyMap.remove(Item.TYPE);
        copyMap.remove("elementType");
        copyMap.remove("typeOfCollection");
        return copyMap;
    }


    public String toString(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return "<NULL>";
        }
        if(((Collection)value).isEmpty())
        {
            return ((Collection)value).toString();
        }
        StringBuilder sb = new StringBuilder("(");
        Type elementType = getElementType(ctx);
        for(Iterator it = ((Collection)value).iterator(); it.hasNext(); )
        {
            Object o = it.next();
            sb.append(escapeForCollection(elementType.toString(ctx, o)));
            if(it.hasNext())
            {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }


    public static int[] getEscapedCollectionPositions(String escaped, int startFrom)
    {
        escaped = escaped.trim();
        int start = escaped.indexOf('(', startFrom);
        while(start > 0 && escaped.charAt(start - 1) == '\\')
        {
            start = escaped.indexOf('(', start + 1);
        }
        if(start < 0)
        {
            throw new IllegalArgumentException("cannot find begin of escaped collection within '" + escaped + "' starting at " + start);
        }
        int end = escaped.indexOf(')', start + 1);
        while(end >= 0 && escaped.charAt(end - 1) == '\\')
        {
            end = escaped.indexOf(')', end + 1);
        }
        if(end < 0)
        {
            throw new IllegalArgumentException("cannot find end of escaped collection within '" + escaped + "' starting at " + start + " (found begin at " + start + ")");
        }
        return new int[] {start, end};
    }


    protected static final String escapeForCollection(String raw)
    {
        return AtomicType.escape(AtomicType.escape(AtomicType.escape(raw, ')'), '('), ',');
    }


    protected static final String unescapeForCollection(String escaped)
    {
        return AtomicType.unescape(AtomicType.unescape(AtomicType.unescape(escaped, ')'), '('), ',');
    }


    public Object parseValue(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(value == null || "<NULL>".equals(value))
        {
            return null;
        }
        int[] range = getEscapedCollectionPositions(value, 0);
        String valueStringEscaped = value.substring(range[0] + 1, range[1]);
        Collection<Object> ret = newInstance();
        Type elementType = getElementType();
        int lastFound = 0;
        int lastPos = 0;
        int delimiterPos = valueStringEscaped.indexOf(',', lastPos);
        for(; delimiterPos >= 0; lastPos = delimiterPos + 1,
                        delimiterPos = valueStringEscaped.indexOf(',', lastPos))
        {
            if(valueStringEscaped.charAt(delimiterPos - 1) != '\\')
            {
                String elementStr = unescapeForCollection(valueStringEscaped.substring(lastFound, delimiterPos));
                ret.add("<NULL>".equals(elementStr) ? null : elementType.parseValue(ctx, elementStr));
                lastFound = delimiterPos + 1;
            }
        }
        if(lastFound < valueStringEscaped.length())
        {
            String elementStr = unescapeForCollection(valueStringEscaped.substring(lastFound));
            ret.add("<NULL>".equals(elementStr) ? null : elementType.parseValue(ctx, elementStr));
        }
        return ret;
    }


    private static final AttributeAccess _AD_TYPEOFCOLLECTION = (AttributeAccess)new Object();
    public static final String ELEMENT_TYPE = "elementType";
    public static final String TYPE_OF_COLLECTION = "typeOfCollection";
    public static final String TYPE_OF_COLLECTION_INTERNAL = "typeOfCollectionInternal";


    public Type getElementType()
    {
        return getElementType(getSession().getSessionContext());
    }


    public Type getElementType(SessionContext ctx)
    {
        return (Type)(new Object(this, "elementType"))
                        .get(ctx);
    }


    public int getTypeOfCollection()
    {
        return ((Integer)(new Object(this, "typeOfCollectionInternal"))
                        .get(null)).intValue();
    }


    public EnumerationValue getTypeOfCollectionEnum()
    {
        return (EnumerationValue)(new Object(this, "typeOfCollection"))
                        .get(null);
    }


    public Collection newInstance()
    {
        switch(getTypeOfCollection())
        {
            case 1:
            case 3:
                return new LinkedHashSet();
        }
        return new ArrayList();
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
            xout.startTag("collectiontype");
            xout.attribute("code", getCode());
            xout.attribute("elementtype", getElementType().getCode());
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


    protected CollectionTypeImpl getCollectionTypeImpl()
    {
        return (CollectionTypeImpl)this.impl;
    }


    public boolean isAssignableFrom(Type type)
    {
        return (type instanceof CollectionType && (
                        equals(type) || (isTypeAssignableFrom(getTypeOfCollection(), ((CollectionType)type)
                                        .getTypeOfCollection()) && getElementType()
                                        .isAssignableFrom(((CollectionType)type).getElementType()))));
    }


    protected boolean isTypeAssignableFrom(int type1, int type2)
    {
        switch(type1)
        {
            case 0:
                return true;
            case 1:
                return (type2 == 1 || type2 == 3);
            case 2:
                return (type2 == 2 || type2 == 3);
            case 3:
                return (type2 == 3);
        }
        return false;
    }


    public boolean isInstance(Object o)
    {
        if(o == null)
        {
            return false;
        }
        switch(getTypeOfCollection())
        {
            case 0:
                if(!(o instanceof Collection))
                {
                    return false;
                }
                break;
            case 2:
                if(!(o instanceof java.util.List))
                {
                    return false;
                }
                break;
            case 1:
                if(!(o instanceof Set))
                {
                    return false;
                }
                break;
            case 3:
                if(!(o instanceof java.util.SortedSet))
                {
                    return false;
                }
                break;
            default:
                return false;
        }
        Type elementType = getElementType();
        for(Iterator it = ((Collection)o).iterator(); it.hasNext(); )
        {
            Object el = it.next();
            if(el != null && !elementType.isInstance(el))
            {
                return false;
            }
        }
        return true;
    }
}
