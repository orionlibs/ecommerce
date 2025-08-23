package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CategoryDifferenceMode implements HybrisEnumValue
{
    CATEGORY_NEW("category_new"),
    CATEGORY_REMOVED("category_removed");
    public static final String _TYPECODE = "CategoryDifferenceMode";
    public static final String SIMPLE_CLASSNAME = "CategoryDifferenceMode";
    private final String code;


    CategoryDifferenceMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CategoryDifferenceMode";
    }
}
