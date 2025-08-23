package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrServerModes implements HybrisEnumValue
{
    EMBEDDED("embedded"),
    STANDALONE("standalone"),
    CLOUD("cloud"),
    XML_EXPORT("xml_export");
    public static final String _TYPECODE = "SolrServerModes";
    public static final String SIMPLE_CLASSNAME = "SolrServerModes";
    private final String code;


    SolrServerModes(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrServerModes";
    }
}
