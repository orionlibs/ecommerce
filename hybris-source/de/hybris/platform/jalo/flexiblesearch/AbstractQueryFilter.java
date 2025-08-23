package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractQueryFilter
{
    private final String code;
    private final ComposedType restrictionType;
    private final String query;


    AbstractQueryFilter(String code, ComposedType type, String query)
    {
        if(type == null)
        {
            throw new NullPointerException("type was null");
        }
        if(StringUtils.isBlank(query))
        {
            throw new IllegalArgumentException("query was null or empty");
        }
        this.code = code;
        this.restrictionType = type;
        this.query = query;
    }


    public ComposedType getRestrictionType()
    {
        return this.restrictionType;
    }


    public String getQuery()
    {
        return this.query;
    }


    public String getCode()
    {
        return (this.code != null) ? this.code : "";
    }


    public boolean equals(Object obj)
    {
        return (super.equals(obj) || (
                        getCode().equals(((AbstractQueryFilter)obj).getCode()) && getRestrictionType().equals(((AbstractQueryFilter)obj)
                                        .getRestrictionType())));
    }


    public int hashCode()
    {
        return getCode().hashCode();
    }
}
