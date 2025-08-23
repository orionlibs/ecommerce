package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CmsItemDisplayStatus implements HybrisEnumValue
{
    DRAFT("DRAFT"),
    IN_PROGRESS("IN_PROGRESS"),
    READY_TO_SYNC("READY_TO_SYNC"),
    SYNCED("SYNCED");
    public static final String _TYPECODE = "CmsItemDisplayStatus";
    public static final String SIMPLE_CLASSNAME = "CmsItemDisplayStatus";
    private final String code;


    CmsItemDisplayStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CmsItemDisplayStatus";
    }
}
