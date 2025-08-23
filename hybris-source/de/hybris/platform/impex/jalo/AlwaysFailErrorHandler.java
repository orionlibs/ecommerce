package de.hybris.platform.impex.jalo;

public class AlwaysFailErrorHandler implements ErrorHandler
{
    public ErrorHandler.RESULT handleError(ImpExException exception, ImpExReader reader)
    {
        return ErrorHandler.RESULT.FAIL;
    }
}
