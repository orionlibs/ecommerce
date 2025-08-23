package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrIndexedPropertyFacetSort implements HybrisEnumValue
{
    COUNT("Count"),
    ALPHA("Alpha"),
    CUSTOM("Custom");
    public static final String _TYPECODE = "SolrIndexedPropertyFacetSort";
    public static final String SIMPLE_CLASSNAME = "SolrIndexedPropertyFacetSort";
    private final String code;


    SolrIndexedPropertyFacetSort(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrIndexedPropertyFacetSort";
    }
}
