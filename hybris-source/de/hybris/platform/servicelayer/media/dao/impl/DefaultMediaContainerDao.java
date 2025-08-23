package de.hybris.platform.servicelayer.media.dao.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.servicelayer.media.dao.MediaContainerDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaContainerDao implements MediaContainerDao
{
    private FlexibleSearchService flexibleSearchService;


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public List<MediaContainerModel> findMediaContainersByQualifier(String qualifier)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("qualifier", qualifier);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("MediaContainer").append("} ");
        builder.append("WHERE {").append("qualifier").append("}=?qualifier ");
        builder.append("ORDER BY {").append("pk").append("} ASC");
        SearchResult<MediaContainerModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }


    public List<MediaContextModel> findMediaContextByQualifier(String qualifier)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("qualifier", qualifier);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("MediaContext").append("} ");
        builder.append("WHERE {").append("qualifier").append("}=?qualifier ");
        builder.append("ORDER BY {").append("pk").append("} ASC");
        SearchResult<MediaContextModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }
}
