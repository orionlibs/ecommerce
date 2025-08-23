package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.impex.jalo.ImpExException;

public class HeaderValidationException extends ImpExException
{
    public static final int UNKNOWN = 0;
    public static final int MISSING_ITEMTYPE = 1;
    public static final int AMBIGUOUS_SIBLING_ATTRIBUTE = 3;
    public static final int UNKNOWN_ATTRIBUTE = 4;
    public static final int UNSUPPORTED_ATTRIBUTE_TYPE = 5;
    public static final int ATTRIBUTE_UNRESOLVABLE = 7;
    public static final int NO_PERMITTED_TYPES = 8;
    public static final int UNKNOWN_ATTRIBUTE_LANGUAGE = 9;
    public static final int INVALID_TRANSLATOR_CLASS = 10;
    public static final int MISSING_UNIQUE_MODIFER = 11;
    public static final int CUSTOM_PROCESSOR_NOT_ALLOWED = 12;
    private final HeaderDescriptor header;


    public HeaderValidationException(HeaderDescriptor header, String message, int errorCode)
    {
        super(null, message, errorCode);
        this.header = header;
    }


    public HeaderValidationException(String message, int errorCode)
    {
        this(null, message, errorCode);
    }


    public HeaderDescriptor getHeader()
    {
        return this.header;
    }
}
