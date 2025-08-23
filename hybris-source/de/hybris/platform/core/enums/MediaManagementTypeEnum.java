package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum MediaManagementTypeEnum implements HybrisEnumValue
{
    FILES("FILES"),
    SSH("SSH"),
    FTP("FTP");
    public static final String _TYPECODE = "MediaManagementTypeEnum";
    public static final String SIMPLE_CLASSNAME = "MediaManagementTypeEnum";
    private final String code;


    MediaManagementTypeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "MediaManagementTypeEnum";
    }
}
