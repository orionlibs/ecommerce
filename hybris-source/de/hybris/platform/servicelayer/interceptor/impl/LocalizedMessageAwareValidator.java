package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.servicelayer.i18n.L10NService;
import org.springframework.beans.factory.annotation.Required;

public abstract class LocalizedMessageAwareValidator
{
    private static final String RESOURCE_PREFIX = "exception.";
    private L10NService l10nService;


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
    }


    protected String getLocalizedMessage(String key, Object... arguments)
    {
        return this.l10nService.getLocalizedString(buildKey(key), arguments);
    }


    private String buildKey(String key)
    {
        StringBuilder message = new StringBuilder("exception.");
        message.append(getClass().getSimpleName().toLowerCase(LocaleHelper.getPersistenceLocale()));
        message.append(".");
        message.append(key);
        return message.toString();
    }
}
