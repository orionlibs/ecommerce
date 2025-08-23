package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SolrCommitMode implements HybrisEnumValue
{
    NEVER("NEVER"),
    AFTER_INDEX("AFTER_INDEX"),
    AFTER_BATCH("AFTER_BATCH"),
    MIXED("MIXED");
    public static final String _TYPECODE = "SolrCommitMode";
    public static final String SIMPLE_CLASSNAME = "SolrCommitMode";
    private final String code;


    SolrCommitMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SolrCommitMode";
    }
}
