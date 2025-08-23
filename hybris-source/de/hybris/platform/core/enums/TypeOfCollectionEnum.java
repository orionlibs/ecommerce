package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum TypeOfCollectionEnum implements HybrisEnumValue
{
    COLLECTION("collection"),
    SET("set"),
    LIST("list");
    public static final String _TYPECODE = "TypeOfCollectionEnum";
    public static final String SIMPLE_CLASSNAME = "TypeOfCollectionEnum";
    private final String code;


    TypeOfCollectionEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "TypeOfCollectionEnum";
    }
}
