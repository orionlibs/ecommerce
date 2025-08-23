package de.hybris.platform.servicelayer.i18n.query.preprocessor;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LocaleQueryPreprocessor implements QueryPreprocessor
{
    private static final Logger LOG = Logger.getLogger(LocaleQueryPreprocessor.class);
    private I18NService i18nService;
    private CommonI18NService commonI18nService;


    public void process(FlexibleSearchQuery query)
    {
        Locale localeToSet = getPossibleLocale(query);
        if(localeToSet != null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Storing locale from query object: " + query.getLocale() + " into user session.");
            }
            this.i18nService.setCurrentLocale(localeToSet);
        }
    }


    @Required
    public void setCommonI18nService(CommonI18NService commonI18nService)
    {
        this.commonI18nService = commonI18nService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    private Locale getPossibleLocale(FlexibleSearchQuery query)
    {
        if(query.getLocale() == null)
        {
            return (query.getLanguage() != null) ? this.commonI18nService.getLocaleForLanguage(query.getLanguage()) : null;
        }
        return query.getLocale();
    }
}
