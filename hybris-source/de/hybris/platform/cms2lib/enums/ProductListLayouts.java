package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ProductListLayouts implements HybrisEnumValue
{
    LISTVIEWLAYOUT("listViewLayout"),
    THUMBVIEWLAYOUT("thumbViewLayout");
    public static final String _TYPECODE = "ProductListLayouts";
    public static final String SIMPLE_CLASSNAME = "ProductListLayouts";
    private final String code;


    ProductListLayouts(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ProductListLayouts";
    }
}
