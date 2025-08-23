package de.hybris.platform.core;

import java.util.Map;
import org.apache.commons.lang.StringUtils;

public abstract class GenericField extends FlexibleSearchTranslatable
{
    private String typeIdentifier;
    private String qualifier;


    public GenericField(String typeIdentifier, String qualifier)
    {
        setQualifier(qualifier);
        setTypeIdentifier(typeIdentifier);
    }


    protected void setQualifier(String qualifier)
    {
        if(StringUtils.isBlank(qualifier))
        {
            throw new IllegalArgumentException("qualifier cannot be null or empty string.");
        }
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getTypeCode()
    {
        return this.typeIdentifier;
    }


    public String getTypeIdentifier()
    {
        return this.typeIdentifier;
    }


    protected void setTypeIdentifier(String typeCode)
    {
        this.typeIdentifier = typeCode;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> aliasTypeMap, Map<String, Object> valueMap)
    {
        queryBuffer.append(getAliasFromTypeMap(aliasTypeMap, getTypeIdentifier()));
        queryBuffer.append(":");
        queryBuffer.append(getQualifier());
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> aliasTypeMap, Map<String, Object> valueMap)
    {
        queryBuffer.append(getQualifier());
    }
}
