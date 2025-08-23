package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CmsPageStatus implements HybrisEnumValue
{
    ACTIVE("active"),
    DELETED("deleted");
    public static final String _TYPECODE = "CmsPageStatus";
    public static final String SIMPLE_CLASSNAME = "CmsPageStatus";
    private final String code;


    CmsPageStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CmsPageStatus";
    }
}
