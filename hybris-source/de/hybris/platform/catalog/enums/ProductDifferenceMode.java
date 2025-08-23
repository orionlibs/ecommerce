package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ProductDifferenceMode implements HybrisEnumValue
{
    PRODUCT_NEW("product_new"),
    PRODUCT_REMOVED("product_removed"),
    PRODUCT_PRICEDIFFERENCE("product_pricedifference");
    public static final String _TYPECODE = "ProductDifferenceMode";
    public static final String SIMPLE_CLASSNAME = "ProductDifferenceMode";
    private final String code;


    ProductDifferenceMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ProductDifferenceMode";
    }
}
