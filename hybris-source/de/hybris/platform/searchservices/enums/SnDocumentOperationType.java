package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SnDocumentOperationType implements HybrisEnumValue
{
    CREATE("CREATE"),
    CREATE_UPDATE("CREATE_UPDATE"),
    PARTIAL_UPDATE("PARTIAL_UPDATE"),
    DELETE("DELETE");
    public static final String _TYPECODE = "SnDocumentOperationType";
    public static final String SIMPLE_CLASSNAME = "SnDocumentOperationType";
    private final String code;


    SnDocumentOperationType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SnDocumentOperationType";
    }
}
