package de.hybris.platform.solrfacetsearch.handler.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.handler.KeywordRedirectHandler;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DefaultStartsWithKeywordRedirectHandler implements KeywordRedirectHandler
{
    private I18NService i18NService;


    public boolean keywordMatches(String theQuery, String keyword, boolean ignoreCase)
    {
        if(ignoreCase)
        {
            Locale currentLocale = this.i18NService.getCurrentLocale();
            return theQuery.toLowerCase(currentLocale).startsWith(keyword.toLowerCase(currentLocale));
        }
        return theQuery.startsWith(keyword);
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }
}
