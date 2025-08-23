package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrQueryMethod implements HybrisEnumValue
{
    GET("GET"),
    POST("POST");
    public static final String _TYPECODE = "SolrQueryMethod";
    public static final String SIMPLE_CLASSNAME = "SolrQueryMethod";
    private final String code;


    SolrQueryMethod(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrQueryMethod";
    }
}
