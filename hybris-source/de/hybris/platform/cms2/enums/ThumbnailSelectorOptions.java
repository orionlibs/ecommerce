package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ThumbnailSelectorOptions implements HybrisEnumValue
{
    NO_THUMBNAIL("NO_THUMBNAIL"),
    UPLOAD_THUMBNAIL("UPLOAD_THUMBNAIL");
    public static final String _TYPECODE = "ThumbnailSelectorOptions";
    public static final String SIMPLE_CLASSNAME = "ThumbnailSelectorOptions";
    private final String code;


    ThumbnailSelectorOptions(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ThumbnailSelectorOptions";
    }
}
