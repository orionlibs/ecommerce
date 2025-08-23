package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum KeywordRedirectMatchType implements HybrisEnumValue
{
    EXACT("EXACT"),
    STARTS_WITH("STARTS_WITH"),
    ENDS_WITH("ENDS_WITH"),
    CONTAINS("CONTAINS"),
    REGEX("REGEX");
    public static final String _TYPECODE = "KeywordRedirectMatchType";
    public static final String SIMPLE_CLASSNAME = "KeywordRedirectMatchType";
    private final String code;


    KeywordRedirectMatchType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "KeywordRedirectMatchType";
    }
}
