package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeJobsDao;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeJobsDao implements BackofficeJobsDao
{
    private static final String CODES_PARAM = "codes";
    private static final String QUERY_STRING = String.format("SELECT {%s} FROM {%s} WHERE {%s} IN (?%s)", new Object[] {"pk", "CronJob", "code", "codes"});
    private FlexibleSearchService flexibleSearchService;


    public List<CronJobModel> findAllJobs(Collection<String> codes)
    {
        if(CollectionUtils.isNotEmpty(codes))
        {
            FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_STRING);
            query.addQueryParameter("codes", codes);
            query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {CronJobModel.class}));
            SearchResult<CronJobModel> search = this.flexibleSearchService.search(query);
            return new ArrayList<>(search.getResult());
        }
        return Collections.emptyList();
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
}
