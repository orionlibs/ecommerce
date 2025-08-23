package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum IndexerOperationValues implements HybrisEnumValue
{
    FULL("full"),
    UPDATE("update"),
    DELETE("delete"),
    PARTIAL_UPDATE("partial_update");
    public static final String _TYPECODE = "IndexerOperationValues";
    public static final String SIMPLE_CLASSNAME = "IndexerOperationValues";
    private final String code;


    IndexerOperationValues(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "IndexerOperationValues";
    }
}
