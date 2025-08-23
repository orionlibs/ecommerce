package de.hybris.platform.solrfacetsearch.handler.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.handler.KeywordRedirectHandler;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRegexKeywordRedirectHandler implements KeywordRedirectHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultRegexKeywordRedirectHandler.class);
    private I18NService i18NService;
    private final Map<String, Pattern> precompiledPatterns = new ConcurrentHashMap<>();


    public boolean keywordMatches(String theQuery, String keyword, boolean ignoreCase)
    {
        try
        {
            Locale currentLocale = this.i18NService.getCurrentLocale();
            return prepareRegexMatcher(ignoreCase ? theQuery.toLowerCase(currentLocale) : theQuery, keyword).matches();
        }
        catch(PatternSyntaxException e)
        {
            LOG.warn("Illegal pattern provided: " + keyword, e);
            return false;
        }
    }


    protected Matcher prepareRegexMatcher(String theQuery, String keyToCompile)
    {
        Pattern pattern = this.precompiledPatterns.get(keyToCompile);
        if(pattern == null)
        {
            pattern = Pattern.compile(keyToCompile);
            this.precompiledPatterns.put(keyToCompile, pattern);
        }
        return pattern.matcher(theQuery);
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
