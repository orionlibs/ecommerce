package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ArticleApprovalStatus implements HybrisEnumValue
{
    CHECK("check"),
    APPROVED("approved"),
    UNAPPROVED("unapproved");
    public static final String _TYPECODE = "ArticleApprovalStatus";
    public static final String SIMPLE_CLASSNAME = "ArticleApprovalStatus";
    private final String code;


    ArticleApprovalStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ArticleApprovalStatus";
    }
}
