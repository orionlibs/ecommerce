package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrWildcardType implements HybrisEnumValue
{
    PREFIX("PREFIX"),
    POSTFIX("POSTFIX"),
    PREFIX_AND_POSTFIX("PREFIX_AND_POSTFIX");
    public static final String _TYPECODE = "SolrWildcardType";
    public static final String SIMPLE_CLASSNAME = "SolrWildcardType";
    private final String code;


    SolrWildcardType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrWildcardType";
    }
}
