package de.hybris.platform.commons.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DocumentTypeEnum implements HybrisEnumValue
{
    PDF("pdf"),
    XML("xml"),
    TEXT("text");
    public static final String _TYPECODE = "documentTypeEnum";
    public static final String SIMPLE_CLASSNAME = "DocumentTypeEnum";
    private final String code;


    DocumentTypeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DocumentTypeEnum";
    }
}
