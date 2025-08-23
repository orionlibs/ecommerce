package de.hybris.platform.advancedsavedquery.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum EmptyParamEnum implements HybrisEnumValue
{
    IGNORE("ignore"),
    TRIMANDIGNORE("trimAndIgnore"),
    ASITIS("asitis");
    public static final String _TYPECODE = "EmptyParamEnum";
    public static final String SIMPLE_CLASSNAME = "EmptyParamEnum";
    private final String code;


    EmptyParamEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "EmptyParamEnum";
    }
}
