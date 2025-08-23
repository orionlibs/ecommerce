package de.hybris.platform.servicelayer.i18n;

import de.hybris.platform.servicelayer.i18n.impl.DefaultI18NService;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class I18NServiceMock extends DefaultI18NService
{
    public ResourceBundle getBundle(String baseName)
    {
        throw new UnsupportedOperationException();
    }


    public ResourceBundle getBundle(String baseName, Locale[] locales)
    {
        throw new UnsupportedOperationException();
    }


    public ResourceBundle getBundle(String baseName, Locale[] locales, ClassLoader loader)
    {
        throw new UnsupportedOperationException();
    }


    public void setCurrentTimeZone(TimeZone zone)
    {
        throw new UnsupportedOperationException();
    }


    public TimeZone getCurrentTimeZone()
    {
        throw new UnsupportedOperationException();
    }
}
