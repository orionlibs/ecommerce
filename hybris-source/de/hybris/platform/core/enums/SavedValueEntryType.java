package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SavedValueEntryType implements HybrisEnumValue
{
    CREATED("created"),
    REMOVED("removed"),
    CHANGED("changed");
    public static final String _TYPECODE = "SavedValueEntryType";
    public static final String SIMPLE_CLASSNAME = "SavedValueEntryType";
    private final String code;


    SavedValueEntryType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SavedValueEntryType";
    }
}
