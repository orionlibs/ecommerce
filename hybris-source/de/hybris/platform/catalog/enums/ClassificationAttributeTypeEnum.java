package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ClassificationAttributeTypeEnum implements HybrisEnumValue
{
    STRING("string"),
    NUMBER("number"),
    BOOLEAN("boolean"),
    ENUM("enum"),
    DATE("date"),
    REFERENCE("reference");
    public static final String _TYPECODE = "ClassificationAttributeTypeEnum";
    public static final String SIMPLE_CLASSNAME = "ClassificationAttributeTypeEnum";
    private final String code;


    ClassificationAttributeTypeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ClassificationAttributeTypeEnum";
    }
}
