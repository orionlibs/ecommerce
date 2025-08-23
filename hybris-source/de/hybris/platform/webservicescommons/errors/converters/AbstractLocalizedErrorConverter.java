package de.hybris.platform.webservicescommons.errors.converters;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

public abstract class AbstractLocalizedErrorConverter extends AbstractErrorConverter
{
    private MessageSource messageSource;


    protected String getMessage(String code, Locale locale)
    {
        return this.messageSource.getMessage(code, new Object[0], code, locale);
    }


    protected String getMessage(String code, Locale locale, String defaultMessage)
    {
        return this.messageSource.getMessage(code, new Object[0], (defaultMessage != null) ? defaultMessage : code, locale);
    }


    protected String getMessage(String code, Object[] args, Locale locale)
    {
        return this.messageSource.getMessage(code, args, code, locale);
    }


    protected String getMessage(String code, Object[] args, Locale locale, String defaultMessage)
    {
        return this.messageSource.getMessage(code, args, (defaultMessage != null) ? defaultMessage : code, locale);
    }


    @Required
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
}
