package de.hybris.platform.impex.exceptions;

import de.hybris.platform.impex.jalo.imp.ValueLine;

public class InsufficientDataException extends ImportExportException
{
    protected ValueLine valueLine;
    private final ErrorCode errorCode;


    public InsufficientDataException(ValueLine line, String message, ErrorCode detail)
    {
        this(line, message, detail, null);
    }


    public InsufficientDataException(ValueLine line, String messge, ErrorCode detail, Throwable exThrowable)
    {
        super(messge, exThrowable);
        this.valueLine = line;
        this.errorCode = detail;
    }


    public String toString()
    {
        return InsufficientDataException.class.getSimpleName() + " line [" + InsufficientDataException.class.getSimpleName() + "] message [" + (
                        (this.valueLine == null) ? "n/a" : (String)Integer.valueOf(this.valueLine.getLineNumber())) + "] code [" + getMessage() + "]";
    }


    public ErrorCode getErrorCode()
    {
        return this.errorCode;
    }


    public ValueLine getValueLine()
    {
        return this.valueLine;
    }
}
