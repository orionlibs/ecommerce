package de.hybris.platform.processengine.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBusinessProcessServiceDao implements BusinessProcessServiceDao
{
    private FlexibleSearchService flexibleSearchService;
    private static final String PROCESS_BY_NAME_QUERY = "SELECT {pk} FROM {BusinessProcess} WHERE {code}=?code";
    private static final String QUERY_PROCESS_TASK_ACTION = "SELECT {action} FROM {ProcessTask} WHERE {process}=?process";


    public BusinessProcessModel getProcess(@Nonnull String processName)
    {
        return findProcessByName(processName);
    }


    public BusinessProcessModel findProcessByName(@Nonnull String processName)
    {
        Objects.requireNonNull(processName, "processName is required");
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {BusinessProcess} WHERE {code}=?code");
        query.addQueryParameter("code", processName);
        SearchResult<BusinessProcessModel> result = this.flexibleSearchService.search(query);
        return (result.getCount() == 1) ? result.getResult().get(0) : null;
    }


    @Nonnull
    public List<String> findBusinessProcessTaskActions(@Nonnull PK businessProcessPk)
    {
        Objects.requireNonNull(businessProcessPk, "businessProcessPk is required");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {action} FROM {ProcessTask} WHERE {process}=?process");
        fQuery.addQueryParameter("process", businessProcessPk);
        fQuery.setResultClassList(Lists.newArrayList((Object[])new Class[] {String.class}));
        fQuery.setDisableCaching(true);
        SearchResult<String> searchResult = this.flexibleSearchService.search(fQuery);
        return searchResult.getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
