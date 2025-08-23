package de.hybris.platform.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

public final class GenericSearchFieldType extends FlexibleSearchTranslatable
{
    public static final GenericSearchFieldType CORE = new GenericSearchFieldType(0, "c");
    public static final GenericSearchFieldType PROPERTY = new GenericSearchFieldType(1, "");
    public static final GenericSearchFieldType LOCALIZED = new GenericSearchFieldType(2, "l");
    public static final GenericSearchFieldType INTERNAL = new GenericSearchFieldType(4, "i");
    public static final GenericSearchFieldType OUTER_JOIN = new GenericSearchFieldType(16, "o");
    private int code = -1;
    private final String flexible_query_str;


    private GenericSearchFieldType(int code, String flexible_query_str)
    {
        this.code = code;
        this.flexible_query_str = flexible_query_str;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append(this.flexible_query_str);
    }


    public int getCode()
    {
        return this.code;
    }


    public boolean isCore()
    {
        return ((getCode() & CORE.getCode()) == CORE.getCode());
    }


    public boolean isProperty()
    {
        return ((getCode() & PROPERTY.getCode()) == PROPERTY.getCode());
    }


    public boolean isLocalized()
    {
        return ((getCode() & LOCALIZED.getCode()) == LOCALIZED.getCode());
    }


    public boolean isInternal()
    {
        return ((getCode() & INTERNAL.getCode()) == INTERNAL.getCode());
    }


    public boolean isOptional()
    {
        return ((getCode() & OUTER_JOIN.getCode()) == OUTER_JOIN.getCode());
    }


    public static Collection toCollection(int newType)
    {
        if(newType == CORE.getCode())
        {
            return Collections.singletonList(CORE);
        }
        Collection<GenericSearchFieldType> types = new HashSet();
        if((newType & PROPERTY.getCode()) == PROPERTY.getCode())
        {
            types.add(PROPERTY);
        }
        if((newType & LOCALIZED.getCode()) == LOCALIZED.getCode())
        {
            types.add(LOCALIZED);
        }
        if((newType & OUTER_JOIN.getCode()) == OUTER_JOIN.getCode())
        {
            types.add(OUTER_JOIN);
        }
        if((newType & INTERNAL.getCode()) == INTERNAL.getCode())
        {
            types.add(INTERNAL);
        }
        if(types.isEmpty())
        {
            throw new IllegalArgumentException("Type '" + newType + "' unknown");
        }
        return types;
    }


    public int hashCode()
    {
        return this.code;
    }


    public boolean equals(Object arg0)
    {
        if(arg0 == null)
        {
            return false;
        }
        if(this == arg0)
        {
            return true;
        }
        if(!(arg0 instanceof GenericSearchFieldType))
        {
            return false;
        }
        return (((GenericSearchFieldType)arg0).getCode() == getCode());
    }


    public String toString()
    {
        return this.flexible_query_str;
    }
}
