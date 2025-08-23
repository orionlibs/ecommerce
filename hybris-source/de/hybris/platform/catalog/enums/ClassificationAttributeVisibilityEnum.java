package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ClassificationAttributeVisibilityEnum implements HybrisEnumValue
{
    VISIBLE("visible"),
    NOT_VISIBLE("not_visible"),
    VISIBLE_IN_BASE("visible_in_base"),
    VISIBLE_IN_VARIANT("visible_in_variant");
    public static final String _TYPECODE = "ClassificationAttributeVisibilityEnum";
    public static final String SIMPLE_CLASSNAME = "ClassificationAttributeVisibilityEnum";
    private final String code;


    ClassificationAttributeVisibilityEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ClassificationAttributeVisibilityEnum";
    }
}
