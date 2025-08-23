package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum IndexerOperationStatus implements HybrisEnumValue
{
    RUNNING("RUNNING"),
    ABORTED("ABORTED"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");
    public static final String _TYPECODE = "IndexerOperationStatus";
    public static final String SIMPLE_CLASSNAME = "IndexerOperationStatus";
    private final String code;


    IndexerOperationStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "IndexerOperationStatus";
    }
}
