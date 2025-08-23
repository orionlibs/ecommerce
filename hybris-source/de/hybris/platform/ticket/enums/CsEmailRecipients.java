package de.hybris.platform.ticket.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CsEmailRecipients implements HybrisEnumValue
{
    CUSTOMER("Customer"),
    ASSIGNEDAGENT("AssignedAgent"),
    ASSIGNEDGROUP("AssignedGroup");
    public static final String _TYPECODE = "CsEmailRecipients";
    public static final String SIMPLE_CLASSNAME = "CsEmailRecipients";
    private final String code;


    CsEmailRecipients(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CsEmailRecipients";
    }
}
