package de.hybris.platform.servicelayer.internal.dao;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class AbstractItemDao implements Dao
{
    @Deprecated(since = "ages", forRemoval = true)
    protected FlexibleSearchService flexibleSearchService;
    @Deprecated(since = "ages", forRemoval = true)
    protected ModelService modelService;


    protected <T> T load(Object source)
    {
        return (T)this.modelService.get(source);
    }


    protected <T extends Collection> T loadAll(Collection<? extends Object> source, T result)
    {
        return (T)this.modelService.getAll(source, (Collection)result);
    }


    protected <T> T getSource(Object model)
    {
        return (T)this.modelService.getSource(model);
    }


    protected <T extends Collection> T getAllSources(Collection<? extends Object> models, T result)
    {
        return (T)this.modelService.getAllSources(models, (Collection)result);
    }


    protected <T> SearchResult<T> search(String query, Map queryParams)
    {
        return this.flexibleSearchService.search(query, queryParams);
    }


    protected <T> SearchResult<T> search(FlexibleSearchQuery searchQuery)
    {
        return this.flexibleSearchService.search(searchQuery);
    }


    protected <T> T searchUnique(FlexibleSearchQuery searchQuery)
    {
        return (T)this.flexibleSearchService.searchUnique(searchQuery);
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearch)
    {
        this.flexibleSearchService = flexibleSearch;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
