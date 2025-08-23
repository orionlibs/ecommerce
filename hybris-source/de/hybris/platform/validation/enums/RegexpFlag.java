package de.hybris.platform.validation.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum RegexpFlag implements HybrisEnumValue
{
    UNIX_LINES("UNIX_LINES"),
    CASE_INSENSITIVE("CASE_INSENSITIVE"),
    COMMENTS("COMMENTS"),
    MULTILINE("MULTILINE"),
    DOTALL("DOTALL"),
    UNICODE_CASE("UNICODE_CASE"),
    CANON_EQ("CANON_EQ");
    public static final String _TYPECODE = "RegexpFlag";
    public static final String SIMPLE_CLASSNAME = "RegexpFlag";
    private final String code;


    RegexpFlag(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "RegexpFlag";
    }
}
