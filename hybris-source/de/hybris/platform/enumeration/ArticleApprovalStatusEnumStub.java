package de.hybris.platform.enumeration;

import de.hybris.platform.core.HybrisEnumValue;

public enum ArticleApprovalStatusEnumStub implements HybrisEnumValue
{
    CHECK("check"),
    APPROVED("approved"),
    UNAPPROVED("unapproved");
    private final String code;


    ArticleApprovalStatusEnumStub(String code)
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
