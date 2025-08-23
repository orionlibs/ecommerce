package de.hybris.platform.impex.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ImpExValidationModeEnum implements HybrisEnumValue
{
    IMPORT_STRICT("import_strict"),
    IMPORT_RELAXED("import_relaxed"),
    EXPORT_ONLY("export_only"),
    EXPORT_REIMPORT_STRICT("export_reimport_strict"),
    EXPORT_REIMPORT_RELAXED("export_reimport_relaxed");
    public static final String _TYPECODE = "ImpExValidationModeEnum";
    public static final String SIMPLE_CLASSNAME = "ImpExValidationModeEnum";
    private final String code;


    ImpExValidationModeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ImpExValidationModeEnum";
    }
}
