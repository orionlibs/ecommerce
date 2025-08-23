package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrOptimizeMode implements HybrisEnumValue
{
    NEVER("NEVER"),
    AFTER_INDEX("AFTER_INDEX"),
    AFTER_FULL_INDEX("AFTER_FULL_INDEX");
    public static final String _TYPECODE = "SolrOptimizeMode";
    public static final String SIMPLE_CLASSNAME = "SolrOptimizeMode";
    private final String code;


    SolrOptimizeMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrOptimizeMode";
    }
}
