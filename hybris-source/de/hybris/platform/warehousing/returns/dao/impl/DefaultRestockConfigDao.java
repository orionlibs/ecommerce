package de.hybris.platform.warehousing.returns.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.model.RestockConfigModel;
import de.hybris.platform.warehousing.returns.RestockException;
import de.hybris.platform.warehousing.returns.dao.RestockConfigDao;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRestockConfigDao implements RestockConfigDao
{
    private FlexibleSearchService flexibleSearchService;


    public RestockConfigModel getRestockConfig() throws RestockException
    {
        String query = "SELECT {pk} FROM {RestockConfig}";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {pk} FROM {RestockConfig}");
        Collection<RestockConfigModel> results = getRestockConfig(fsQuery);
        if(results.isEmpty())
        {
            return null;
        }
        if(results.size() == 1)
        {
            return results.iterator().next();
        }
        throw new RestockException("Only one restockConfig record allowed");
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected <T extends RestockConfigModel> Collection<T> getRestockConfig(FlexibleSearchQuery query)
    {
        SearchResult<T> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }
}
