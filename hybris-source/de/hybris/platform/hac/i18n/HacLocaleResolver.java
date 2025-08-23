package de.hybris.platform.hac.i18n;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.LocaleResolver;

public class HacLocaleResolver implements LocaleResolver
{
    private static final Locale HAC_DEFAULT_LOCALE = Locale.ENGLISH;


    public Locale resolveLocale(HttpServletRequest request)
    {
        return HAC_DEFAULT_LOCALE;
    }


    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
    {
        throw new UnsupportedOperationException("Cannot change " + HAC_DEFAULT_LOCALE
                        .getLanguage() + " locale, use a different locale resolution strategy");
    }
}
