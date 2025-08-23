package com.hybris.backoffice.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum BackofficeSpecialCollectionType implements HybrisEnumValue
{
    QUICKLIST("quicklist"),
    BLOCKEDLIST("blockedlist");
    public static final String _TYPECODE = "BackofficeSpecialCollectionType";
    public static final String SIMPLE_CLASSNAME = "BackofficeSpecialCollectionType";
    private final String code;


    BackofficeSpecialCollectionType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "BackofficeSpecialCollectionType";
    }
}
