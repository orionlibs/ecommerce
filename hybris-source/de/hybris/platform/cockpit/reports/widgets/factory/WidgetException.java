package de.hybris.platform.cockpit.reports.widgets.factory;

public class WidgetException extends RuntimeException
{
    public WidgetException()
    {
    }


    public WidgetException(String message, Throwable exception)
    {
        super(message, exception);
    }


    public WidgetException(String message)
    {
        super(message);
    }


    public WidgetException(Throwable exception)
    {
        super(exception);
    }
}
