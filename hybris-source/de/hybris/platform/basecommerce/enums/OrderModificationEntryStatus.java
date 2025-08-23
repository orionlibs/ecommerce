package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum OrderModificationEntryStatus implements HybrisEnumValue
{
    INPROGRESS("INPROGRESS"),
    FAILED("FAILED"),
    SUCCESSFULL("SUCCESSFULL");
    public static final String _TYPECODE = "OrderModificationEntryStatus";
    public static final String SIMPLE_CLASSNAME = "OrderModificationEntryStatus";
    private final String code;


    OrderModificationEntryStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "OrderModificationEntryStatus";
    }
}
