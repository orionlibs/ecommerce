package de.hybris.platform.auditreport.service;

public class ReportGenerationException extends RuntimeException
{
    public ReportGenerationException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ReportGenerationException(String message)
    {
        super(message);
    }


    public ReportGenerationException(Throwable cause)
    {
        super(cause);
    }
}
