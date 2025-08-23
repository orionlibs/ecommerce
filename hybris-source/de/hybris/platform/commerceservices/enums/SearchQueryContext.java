package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SearchQueryContext implements HybrisEnumValue
{
    DEFAULT("DEFAULT"),
    SUGGESTIONS("SUGGESTIONS");
    public static final String _TYPECODE = "SearchQueryContext";
    public static final String SIMPLE_CLASSNAME = "SearchQueryContext";
    private final String code;


    SearchQueryContext(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SearchQueryContext";
    }
}
