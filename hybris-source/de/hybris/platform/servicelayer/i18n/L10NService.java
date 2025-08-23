package de.hybris.platform.servicelayer.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface L10NService
{
    ResourceBundle getResourceBundle();


    ResourceBundle getResourceBundle(Locale[] paramArrayOfLocale);


    ResourceBundle getResourceBundle(String paramString);


    ResourceBundle getResourceBundle(String paramString, Locale[] paramArrayOfLocale);


    String getLocalizedString(String paramString, Object[] paramArrayOfObject);


    String getLocalizedString(String paramString);
}
