package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CmsApprovalStatus implements HybrisEnumValue
{
    CHECK("check"),
    APPROVED("approved"),
    UNAPPROVED("unapproved");
    public static final String _TYPECODE = "CmsApprovalStatus";
    public static final String SIMPLE_CLASSNAME = "CmsApprovalStatus";
    private final String code;


    CmsApprovalStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CmsApprovalStatus";
    }
}
