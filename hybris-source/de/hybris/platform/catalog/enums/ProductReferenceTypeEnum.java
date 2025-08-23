package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ProductReferenceTypeEnum implements HybrisEnumValue
{
    ACCESSORIES("ACCESSORIES"),
    BASE_PRODUCT("BASE_PRODUCT"),
    CONSISTS_OF("CONSISTS_OF"),
    DIFF_ORDERUNIT("DIFF_ORDERUNIT"),
    FOLLOWUP("FOLLOWUP"),
    MANDATORY("MANDATORY"),
    SIMILAR("SIMILAR"),
    SELECT("SELECT"),
    SPAREPART("SPAREPART"),
    OTHERS("OTHERS"),
    UPSELLING("UPSELLING"),
    CROSSELLING("CROSSELLING");
    public static final String _TYPECODE = "ProductReferenceTypeEnum";
    public static final String SIMPLE_CLASSNAME = "ProductReferenceTypeEnum";
    private final String code;


    ProductReferenceTypeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ProductReferenceTypeEnum";
    }
}
