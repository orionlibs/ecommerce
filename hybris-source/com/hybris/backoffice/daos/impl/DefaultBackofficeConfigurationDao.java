package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeConfigurationDao;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeConfigurationDao implements BackofficeConfigurationDao
{
    private FlexibleSearchService flexibleSearchService;
    private static final String QUERY_MEDIAS_FOR_CODE = "SELECT {PK} FROM {Media} WHERE {code} IN (?codes)";


    public List<MediaModel> findMedias(String code)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {PK} FROM {Media} WHERE {code} IN (?codes)");
        fsq.addQueryParameter("codes", code);
        fsq.setResultClassList(Collections.singletonList(MediaModel.class));
        SearchResult<MediaModel> resultSet = this.flexibleSearchService.search(fsq);
        return resultSet.getResult();
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
