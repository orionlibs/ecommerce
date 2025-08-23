package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SnDocumentOperationStatus implements HybrisEnumValue
{
    CREATED("CREATED"),
    UPDATED("UPDATED"),
    DELETED("DELETED"),
    FAILED("FAILED");
    public static final String _TYPECODE = "SnDocumentOperationStatus";
    public static final String SIMPLE_CLASSNAME = "SnDocumentOperationStatus";
    private final String code;


    SnDocumentOperationStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SnDocumentOperationStatus";
    }
}
