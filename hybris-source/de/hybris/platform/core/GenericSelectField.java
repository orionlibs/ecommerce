package de.hybris.platform.core;

import java.util.Map;

public class GenericSelectField extends GenericField
{
    private Class returnClass;


    public GenericSelectField(String qualifier)
    {
        this(null, qualifier, null);
    }


    public GenericSelectField(String qualifier, Class returnClass)
    {
        this(null, qualifier, returnClass);
    }


    public GenericSelectField(String typeIdentifier, String qualifier, Class returnClass)
    {
        super(typeIdentifier, qualifier);
        setReturnClass(returnClass);
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append("{");
        super.toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        queryBuffer.append("}");
    }


    public Class getReturnClass()
    {
        return this.returnClass;
    }


    protected void setReturnClass(Class returnClass)
    {
        this.returnClass = returnClass;
    }
}
