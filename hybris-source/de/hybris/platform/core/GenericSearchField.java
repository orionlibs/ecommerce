package de.hybris.platform.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenericSearchField extends GenericField
{
    private PK languagePK;
    private Set fieldTypes;


    public GenericSearchField(String qualifier)
    {
        this(null, qualifier);
    }


    public GenericSearchField(String typeIdentifier, String qualifier)
    {
        super(typeIdentifier, qualifier);
    }


    public void addFieldType(GenericSearchFieldType type)
    {
        if(this.fieldTypes == null)
        {
            this.fieldTypes = new HashSet();
        }
        this.fieldTypes.add(type);
    }


    public boolean hasFieldType(GenericSearchFieldType type)
    {
        return getFieldTypes().contains(type);
    }


    public Collection getFieldTypes()
    {
        return (this.fieldTypes != null) ? this.fieldTypes : Collections.EMPTY_LIST;
    }


    public void setFieldTypes(Collection<?> fieldTypes)
    {
        this.fieldTypes = (fieldTypes != null) ? new HashSet(fieldTypes) : null;
    }


    public PK getLanguagePK()
    {
        return this.languagePK;
    }


    public void setLanguagePK(PK pk)
    {
        this.languagePK = pk;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append("{");
        super.toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        if(this.languagePK != null)
        {
            queryBuffer.append("[");
            queryBuffer.append(this.languagePK.toString());
            queryBuffer.append("]");
        }
        if(!getFieldTypes().isEmpty())
        {
            queryBuffer.append(":");
            for(Iterator<FlexibleSearchTranslatable> it = getFieldTypes().iterator(); it.hasNext(); )
            {
                ((FlexibleSearchTranslatable)it.next()).toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
            }
        }
        queryBuffer.append("}");
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append("{");
        super.toPolyglotSearch(queryBuffer, typeIndexMap, valueMap);
        if(this.languagePK != null)
        {
            queryBuffer.append("[");
            queryBuffer.append(this.languagePK.toString());
            queryBuffer.append("]");
        }
        queryBuffer.append("}");
    }


    public String toString()
    {
        return "GSF(" + getTypeIdentifier() + ", " + getQualifier() + ", " + this.languagePK + ", " + getFieldTypes() + ")";
    }


    public boolean equals(Object object)
    {
        return (object != null && object instanceof GenericSearchField &&
                        getQualifier().equalsIgnoreCase(((GenericSearchField)object).getQualifier()) && (
                        getTypeIdentifier() == ((GenericSearchField)object).getTypeIdentifier() || (getTypeIdentifier() != null && getTypeIdentifier()
                                        .equals(((GenericSearchField)object).getTypeIdentifier()))) && (
                        getLanguagePK() == ((GenericSearchField)object).getLanguagePK() || (getLanguagePK() != null && getLanguagePK()
                                        .equals(((GenericSearchField)object).getLanguagePK()))));
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(getQualifier().toLowerCase())
                        .append(getTypeIdentifier())
                        .append(getLanguagePK())
                        .toHashCode();
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        return getFieldTypes().isEmpty();
    }
}
