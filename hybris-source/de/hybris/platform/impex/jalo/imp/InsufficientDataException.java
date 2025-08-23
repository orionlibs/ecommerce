package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExException;

public class InsufficientDataException extends ImpExException
{
    public static final int UNKNOWN = 0;
    public static final int MISSING_MANDATORY_VALUE = 1;
    public static final int MISSING_UNIQUE_KEY_VALUE = 2;
    public static final int TYPE_NOT_PERMITTED = 3;
    public static final int UNKNOWN_TYPE = 4;
    private final ValueLine line;


    public InsufficientDataException(ValueLine line, String message, int errorCode)
    {
        super(null, message, errorCode);
        this.line = line;
    }


    public InsufficientDataException(String message, int errorCode)
    {
        this(null, message, errorCode);
    }


    public ValueLine getValueLine()
    {
        return this.line;
    }


    public String toString()
    {
        return ((this.line != null) ? String.valueOf(this.line.getLineNumber()) : "n/a") + ":" + ((this.line != null) ? String.valueOf(this.line.getLineNumber()) : "n/a");
    }
}
