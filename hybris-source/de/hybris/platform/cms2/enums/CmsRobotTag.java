package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CmsRobotTag implements HybrisEnumValue
{
    INDEX_FOLLOW("INDEX_FOLLOW"),
    INDEX_NOFOLLOW("INDEX_NOFOLLOW"),
    NOINDEX_FOLLOW("NOINDEX_FOLLOW"),
    NOINDEX_NOFOLLOW("NOINDEX_NOFOLLOW");
    public static final String _TYPECODE = "CmsRobotTag";
    public static final String SIMPLE_CLASSNAME = "CmsRobotTag";
    private final String code;


    CmsRobotTag(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CmsRobotTag";
    }
}
