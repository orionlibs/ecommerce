package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ArticleStatus implements HybrisEnumValue
{
    BARGAIN("bargain"),
    NEW_ARTICLE("new_article"),
    OLD_ARTICLE("old_article"),
    NEW("new"),
    USED("used"),
    REFURBISHED("refurbished"),
    CORE_ARTICLE("core_article"),
    OTHERS("others");
    public static final String _TYPECODE = "ArticleStatus";
    public static final String SIMPLE_CLASSNAME = "ArticleStatus";
    private final String code;


    ArticleStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ArticleStatus";
    }
}
