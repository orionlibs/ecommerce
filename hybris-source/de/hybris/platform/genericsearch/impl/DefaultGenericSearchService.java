package de.hybris.platform.genericsearch.impl;

import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultGenericSearchService extends AbstractService implements GenericSearchService
{
    private static final Logger LOG = Logger.getLogger(DefaultGenericSearchService.class);
    @Deprecated(since = "2205", forRemoval = false)
    public static final String GENERIC_SEARCH_READ_REPLICA_TYPE_CODES_EXCLUDE = "generic.search.read-replica.type.codes.exclude";
    @Deprecated(since = "2205", forRemoval = false)
    public static final String GENERIC_SEARCH_READ_REPLICA_ENABLED = "generic.search.read-replica.enabled";
    private transient SessionService sessionService;
    private transient FlexibleSearchService flexibleSearchService;
    private transient TypeService typeService;
    private transient CommonI18NService commonI18NService;
    private transient ConfigurationService configurationService;


    public SearchResult search(GenericQuery query)
    {
        GenericSearchQuery gsquery = new GenericSearchQuery(query);
        gsquery.setLocale(this.commonI18NService.getLocaleForLanguage(this.commonI18NService.getCurrentLanguage()));
        return search(gsquery);
    }


    public SearchResult search(GenericSearchQuery genericSearchQuery)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("genericSearchQuery", genericSearchQuery);
        ServicesUtil.validateParameterNotNullStandardMessage("genericSearchQuery.getQuery()", genericSearchQuery.getQuery());
        return (SearchResult)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, genericSearchQuery));
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
