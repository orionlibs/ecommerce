package de.hybris.platform.customerreview.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CustomerReviewApprovalType implements HybrisEnumValue
{
    APPROVED("approved"),
    PENDING("pending"),
    REJECTED("rejected");
    public static final String _TYPECODE = "CustomerReviewApprovalType";
    public static final String SIMPLE_CLASSNAME = "CustomerReviewApprovalType";
    private final String code;


    CustomerReviewApprovalType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CustomerReviewApprovalType";
    }
}
