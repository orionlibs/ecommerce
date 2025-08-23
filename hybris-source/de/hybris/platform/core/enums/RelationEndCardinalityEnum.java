package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum RelationEndCardinalityEnum implements HybrisEnumValue
{
    ONE("one"),
    MANY("many");
    public static final String _TYPECODE = "RelationEndCardinalityEnum";
    public static final String SIMPLE_CLASSNAME = "RelationEndCardinalityEnum";
    private final String code;


    RelationEndCardinalityEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "RelationEndCardinalityEnum";
    }
}
