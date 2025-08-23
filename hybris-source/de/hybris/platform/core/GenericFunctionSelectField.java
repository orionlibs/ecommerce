package de.hybris.platform.core;

import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class GenericFunctionSelectField extends GenericSelectField
{
    private final String functionName;


    public GenericFunctionSelectField(String qualifier, String functionName)
    {
        this(null, qualifier, null, functionName);
    }


    public GenericFunctionSelectField(String qualifier, Class returnClass, String functionName)
    {
        this(null, qualifier, returnClass, functionName);
    }


    public GenericFunctionSelectField(String typeIndentifier, String qualifier, Class returnClass, String functionName)
    {
        super(typeIndentifier, qualifier, returnClass);
        if(StringUtils.isBlank(functionName))
        {
            throw new IllegalArgumentException("function name cannot be null or empty");
        }
        this.functionName = functionName;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append(this.functionName.trim());
        queryBuffer.append("(");
        super.toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        queryBuffer.append(")");
    }
}
