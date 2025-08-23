package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SessionParamTranslator
{
    public Map translatePathValueKeys(SessionContext ctx, List valueKeys, Map<Object, Object> _values)
    {
        for(Iterator iter = valueKeys.iterator(); iter.hasNext(); )
        {
            Object k = iter.next();
            Object value = _values.get(k);
            if(value == null && k instanceof String)
            {
                String key = (String)k;
                String[] qualifiers = key.split("\\.");
                int s = qualifiers.length;
                if(s > 1)
                {
                    StringBuilder sb = new StringBuilder();
                    boolean fromCtx = false;
                    for(int i = 0; i < s; i++)
                    {
                        if(i == 0 && "session".equalsIgnoreCase(qualifiers[i]))
                        {
                            fromCtx = true;
                        }
                        else
                        {
                            if(sb.length() > 0)
                            {
                                sb.append('.');
                            }
                            sb.append(qualifiers[i]);
                            Object o = fromCtx ? ctx.getAttribute(sb.toString()) : _values.get(sb.toString());
                            if(o != null)
                            {
                                if(i + 1 < s)
                                {
                                    value = translateObject(ctx, o, qualifiers, i, key);
                                    break;
                                }
                                value = o;
                                break;
                            }
                        }
                    }
                    if(value != null)
                    {
                        _values.put(k, value);
                        continue;
                    }
                    throw new FlexibleSearchException(null, "could not translate value expression '" + key + "'", 0);
                }
            }
        }
        return _values;
    }


    public Object translateObject(SessionContext ctx, Object tmp, String[] qualifiers, int i, String key)
    {
        Object ret = null;
        if(!(tmp instanceof Item) && !(tmp instanceof PK))
        {
            throw new FlexibleSearchException(null, "path value keys may only be applied to items (key='" + key + "', found " + tmp + ")", 0);
        }
        Item currentItem = getCurrentItem(tmp);
        int s = qualifiers.length;
        for(int j = i + 1; j < s; j++)
        {
            Object attr;
            String fdQualifier = qualifiers[j];
            try
            {
                attr = currentItem.getAttribute(ctx, fdQualifier);
            }
            catch(JaloInvalidParameterException | de.hybris.platform.jalo.security.JaloSecurityException e)
            {
                throw new FlexibleSearchException(e, "error accessing value key path '" + key + "' at '" + fdQualifier + "' of item " + currentItem, 0);
            }
            if(j < s - 1)
            {
                if(!(attr instanceof Item) && !(tmp instanceof PK))
                {
                    throw new FlexibleSearchException(null, "path value keys may only be applied to items (key='" + key + "', found " + attr + " at '" + fdQualifier + "')", 0);
                }
                currentItem = getCurrentItem(attr);
            }
            else if(attr == null || (attr instanceof Collection && ((Collection)attr).size() == 0))
            {
                AttributeDescriptor ad = currentItem.getComposedType().getAttributeDescriptorIncludingPrivate(fdQualifier);
                if(ad == null)
                {
                    ret = attr;
                }
                else
                {
                    ret = getTypedNull(ad.getAttributeType());
                }
            }
            else
            {
                ret = attr;
            }
        }
        return ret;
    }


    public List<String> extractSessionParamsFromQuery(String query)
    {
        List<String> sessionParams = new ArrayList<>();
        int last = 0;
        int pos;
        for(pos = query.indexOf('?'); pos > 0; pos = query.indexOf('?', last))
        {
            Object valueKey;
            if(pos + 1 == query.length())
            {
                throw new FlexibleSearchException(null, "missing value key after '?' at " + pos, 0);
            }
            if(Character.isDigit(query.charAt(pos + 1)))
            {
                valueKey = Integer.valueOf(readValueNumber(query, pos));
            }
            else
            {
                valueKey = readValueAlias(query, pos);
                String param = (String)valueKey;
                if(param.startsWith("session."))
                {
                    sessionParams.add((String)valueKey);
                }
            }
            last = pos + 1 + valueKey.toString().length();
        }
        return sessionParams;
    }


    private final int readValueNumber(String str, int pos)
    {
        int length = str.length();
        int number = 0;
        int digits = 0;
        for(; pos + 1 + digits < length && '0' <= str.charAt(pos + 1 + digits) && str.charAt(pos + 1 + digits) <= '9'; digits++)
        {
            number = number * 10 + str.charAt(pos + 1 + digits) - 48;
        }
        return number;
    }


    private final String readValueAlias(String str, int pos)
    {
        int length = str.length();
        StringBuilder stringBuilder = new StringBuilder();
        for(int current = pos + 1; current < length && (Character.isLetterOrDigit(str.charAt(current)) || '_' == str
                        .charAt(current) || '-' == str.charAt(current) || '.' == str.charAt(current)); current++)
        {
            stringBuilder.append(str.charAt(current));
        }
        return stringBuilder.toString();
    }


    private Item getCurrentItem(Object input)
    {
        if(input instanceof Item)
        {
            return (Item)input;
        }
        if(input instanceof PK)
        {
            return JaloSession.getCurrentSession().getItem((PK)input);
        }
        throw new FlexibleSearchException(null, "input must be an instance of Item or PK", 0);
    }


    private TypedNull getTypedNull(Type type)
    {
        if(type instanceof AtomicType)
        {
            return new TypedNull(((AtomicType)type).getJavaClass());
        }
        if(type instanceof de.hybris.platform.jalo.type.ComposedType)
        {
            return new TypedNull(PK.class);
        }
        if(type instanceof CollectionType)
        {
            return getTypedNull(((CollectionType)type).getElementType());
        }
        throw new IllegalArgumentException("try to create TypedNull from an invalid class: " + type.getClass().getName());
    }
}
