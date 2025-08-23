package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ExportStatus implements HybrisEnumValue
{
    NOTEXPORTED("NOTEXPORTED"),
    EXPORTED("EXPORTED");
    public static final String _TYPECODE = "ExportStatus";
    public static final String SIMPLE_CLASSNAME = "ExportStatus";
    private final String code;


    ExportStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ExportStatus";
    }
}
