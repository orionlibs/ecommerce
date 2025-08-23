package de.hybris.y2ysync.model;

import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Required;

public class Y2YStreamConfigurationContainerRemoveInterceptor implements RemoveInterceptor<Y2YStreamConfigurationContainerModel>
{
    private FlexibleSearchService flexibleSearchService;
    private L10NService l10nService;


    public void onRemove(Y2YStreamConfigurationContainerModel container, InterceptorContext ctx) throws InterceptorException
    {
        if(jobExistsForContainer(container))
        {
            throw new InterceptorException(this.l10nService.getLocalizedString("error.container.contains.corresponding.jobs"));
        }
    }


    private boolean jobExistsForContainer(Y2YStreamConfigurationContainerModel container)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Y2YSyncJob} WHERE {streamConfigurationContainer}=?container");
        fQuery.addQueryParameter("container", container);
        SearchResult<Object> result = this.flexibleSearchService.search(fQuery);
        return (result.getCount() > 0);
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
    }
}
