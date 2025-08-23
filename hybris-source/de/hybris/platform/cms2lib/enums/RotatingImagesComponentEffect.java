package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum RotatingImagesComponentEffect implements HybrisEnumValue
{
    ZOOM("zoom"),
    FADE("fade"),
    TURNDOWN("turnDown"),
    CURTAINX("curtainX");
    public static final String _TYPECODE = "RotatingImagesComponentEffect";
    public static final String SIMPLE_CLASSNAME = "RotatingImagesComponentEffect";
    private final String code;


    RotatingImagesComponentEffect(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "RotatingImagesComponentEffect";
    }
}
