package de.hybris.platform.commons.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum RendererTypeEnum implements HybrisEnumValue
{
    VELOCITY("velocity");
    private final String code;
    public static final String SIMPLE_CLASSNAME = "RendererTypeEnum";
    public static final String _TYPECODE = "RendererTypeEnum";


    RendererTypeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "RendererTypeEnum";
    }
}
