package de.hybris.platform.impex.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ImpExProcessModeEnum implements HybrisEnumValue
{
    INSERT("insert"),
    UPDATE("update"),
    REMOVE("remove"),
    IGNORE("ignore"),
    INSERT_UPDATE("insert_update");
    public static final String _TYPECODE = "ImpExProcessModeEnum";
    public static final String SIMPLE_CLASSNAME = "ImpExProcessModeEnum";
    private final String code;


    ImpExProcessModeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ImpExProcessModeEnum";
    }
}
