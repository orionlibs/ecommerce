package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum IndexMode implements HybrisEnumValue
{
    DIRECT("DIRECT"),
    TWO_PHASE("TWO_PHASE");
    public static final String _TYPECODE = "IndexMode";
    public static final String SIMPLE_CLASSNAME = "IndexMode";
    private final String code;


    IndexMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "IndexMode";
    }
}
