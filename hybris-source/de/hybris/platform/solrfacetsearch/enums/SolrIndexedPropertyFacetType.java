package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrIndexedPropertyFacetType implements HybrisEnumValue
{
    REFINE("Refine"),
    MULTISELECTAND("MultiSelectAnd"),
    MULTISELECTOR("MultiSelectOr");
    public static final String _TYPECODE = "SolrIndexedPropertyFacetType";
    public static final String SIMPLE_CLASSNAME = "SolrIndexedPropertyFacetType";
    private final String code;


    SolrIndexedPropertyFacetType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrIndexedPropertyFacetType";
    }
}
