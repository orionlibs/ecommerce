package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum UiExperienceLevel implements HybrisEnumValue
{
    MOBILE("Mobile"),
    DESKTOP("Desktop");
    public static final String _TYPECODE = "UiExperienceLevel";
    public static final String SIMPLE_CLASSNAME = "UiExperienceLevel";
    private final String code;


    UiExperienceLevel(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "UiExperienceLevel";
    }
}
