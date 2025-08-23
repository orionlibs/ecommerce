package com.hybris.pcmbackoffice.widgets.shortcuts;

import de.hybris.platform.core.HybrisEnumValue;

public enum ShortcutsFlagEnum implements HybrisEnumValue
{
    QUICK_LIST("quicklist"),
    BLOCKED_LIST("blockedlist"),
    ALL_PRODUCTS("all_products");
    public static final String TYPECODE = "ShortcutFlagEnum";
    public static final String SIMPLE_CLASSNAME = "ShortcutFlagEnum";
    private final String code;


    ShortcutsFlagEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ShortcutFlagEnum";
    }
}
