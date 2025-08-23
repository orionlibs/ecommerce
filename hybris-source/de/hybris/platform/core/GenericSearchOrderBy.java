package de.hybris.platform.core;

import de.hybris.platform.genericsearch.impl.GenericSearchQueryAdjuster;
import java.util.Map;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenericSearchOrderBy extends FlexibleSearchTranslatable
{
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    private boolean ascendingOrder = true;
    private final GenericSearchField field;


    public GenericSearchOrderBy(GenericSearchField field)
    {
        this(field, true);
    }


    public GenericSearchOrderBy(GenericSearchField field, boolean ascendingOrder)
    {
        this.field = field;
        this.ascendingOrder = ascendingOrder;
    }


    public boolean isAscending()
    {
        return this.ascendingOrder;
    }


    public void setAscending(boolean asc)
    {
        this.ascendingOrder = asc;
    }


    public GenericSearchField getField()
    {
        return this.field;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        StringBuilder orderByBuffer = new StringBuilder();
        this.field.toFlexibleSearch(orderByBuffer, typeIndexMap, valueMap);
        GenericSearchQueryAdjuster.getDefault().adjustQueryForOrderBy(queryBuffer, orderByBuffer, this.field.getTypeIdentifier(), this.field
                        .getQualifier());
        queryBuffer.append(" ");
        queryBuffer.append(this.ascendingOrder ? "ASC" : "DESC");
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> aliasTypeMap, Map<String, Object> valueMap)
    {
        this.field.toPolyglotSearch(queryBuffer, aliasTypeMap, valueMap);
        queryBuffer.append(" ");
        queryBuffer.append(this.ascendingOrder ? "ASC" : "DESC");
    }


    public String toString()
    {
        return "GSOB(" + this.field + "," + this.ascendingOrder + ")";
    }


    public boolean equals(Object arg0)
    {
        if(!(arg0 instanceof GenericSearchOrderBy))
        {
            return false;
        }
        if(this == arg0)
        {
            return true;
        }
        return (((GenericSearchOrderBy)arg0).getField().equals(getField()) && ((GenericSearchOrderBy)arg0)
                        .isAscending() == isAscending());
    }


    public int hashCode()
    {
        return (new HashCodeBuilder()).append(getField()).append(isAscending()).toHashCode();
    }
}
