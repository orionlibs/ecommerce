package com.hybris.backoffice.searchservices.dataaccess.facades;

import com.hybris.backoffice.search.dataaccess.facades.DefaultFieldSearchFacadeStrategy;
import com.hybris.backoffice.searchservices.constants.BackofficesearchservicesConstants;
import com.hybris.backoffice.searchservices.populators.SnSearchQueryConverterData;
import com.hybris.backoffice.searchservices.populators.SnSearchResultConverterData;
import com.hybris.backoffice.searchservices.populators.SnSearchResultSourceData;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchService;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;
import de.hybris.platform.searchservices.suggest.service.SnSuggestService;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class DefaultSearchServicesFieldSearchFacadeStrategy<T extends ItemModel> extends DefaultFieldSearchFacadeStrategy<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSearchServicesFieldSearchFacadeStrategy.class);
    private SnSearchService snSearchService;
    private SnSuggestService snSuggestService;
    private Converter<SnSearchQueryConverterData, SnSearchQuery> searchQueryConverter;
    private Converter<SnSearchResultSourceData, SnSearchResultConverterData> searchResultConverter;


    public Pageable<T> search(SearchQueryData queryData)
    {
        return (queryData == null) ? (Pageable<T>)new PageableList(Collections.emptyList(), 1) : (Pageable<T>)new BackofficeSearchservicesPageable(this, queryData);
    }


    public Map<String, Collection<String>> getAutosuggestionsForQuery(AutosuggestionQueryData queryData, Context context)
    {
        SnSuggestQuery snSuggestQuery = createSnSuggestQuery(queryData);
        try
        {
            String indexTypeId = ((SnIndexTypeModel)getFacetSearchConfigService().getIndexedTypeModel(queryData.getSearchType())).getId();
            SnSuggestRequest snSuggestRequest = getSnSuggestService().createSuggestRequest(indexTypeId, snSuggestQuery);
            SnSuggestResponse snSuggestResponse = getSnSuggestService().suggest(snSuggestRequest);
            return createAutosuggestionsResult(queryData.getQueryText(), snSuggestResponse);
        }
        catch(SnException e)
        {
            LOG.warn("Couldn't retrieve auto suggestions for query: {}, and type: {}", new Object[] {queryData.getQueryText(), queryData
                            .getSearchType(), e});
            return Collections.emptyMap();
        }
    }


    public String getStrategyName()
    {
        return "searchservices";
    }


    protected SnSearchService getSnSearchService()
    {
        return this.snSearchService;
    }


    public void setSnSearchService(SnSearchService snSearchService)
    {
        this.snSearchService = snSearchService;
    }


    protected SnSuggestService getSnSuggestService()
    {
        return this.snSuggestService;
    }


    public void setSnSuggestService(SnSuggestService snSuggestService)
    {
        this.snSuggestService = snSuggestService;
    }


    private SnSuggestQuery createSnSuggestQuery(AutosuggestionQueryData queryData)
    {
        SnSuggestQuery snSuggestQuery = new SnSuggestQuery();
        snSuggestQuery.setQuery(queryData.getQueryText());
        snSuggestQuery.setLimit(Integer.valueOf(Config.getInt("backoffice.search.services.suggest.query.limit", BackofficesearchservicesConstants.PROPERTY_SUGGEST_QUERY_LIMIT_VALUE
                        .intValue())));
        return snSuggestQuery;
    }


    private Map<String, Collection<String>> createAutosuggestionsResult(String queryText, SnSuggestResponse snSuggestResponse)
    {
        SnSuggestResult snSuggestResult = snSuggestResponse.getSuggestResult();
        if(null != snSuggestResult)
        {
            List<String> suggestValues = (List<String>)snSuggestResult.getSuggestHits().stream().filter(Objects::nonNull).map(snSuggestHit -> snSuggestHit.getQuery()).collect(Collectors.toList());
            if(Objects.nonNull(suggestValues) && !suggestValues.isEmpty())
            {
                Map<String, Collection<String>> result = new HashMap<>(1);
                result.put(StringUtils.lowerCase(queryText.trim()), suggestValues);
                return result;
            }
        }
        return Collections.emptyMap();
    }


    public Converter<SnSearchQueryConverterData, SnSearchQuery> getSearchQueryConverter()
    {
        return this.searchQueryConverter;
    }


    public void setSearchQueryConverter(Converter<SnSearchQueryConverterData, SnSearchQuery> searchQueryConverter)
    {
        this.searchQueryConverter = searchQueryConverter;
    }


    public Converter<SnSearchResultSourceData, SnSearchResultConverterData> getSearchResultConverter()
    {
        return this.searchResultConverter;
    }


    public void setSearchResultConverter(Converter<SnSearchResultSourceData, SnSearchResultConverterData> searchResultConverter)
    {
        this.searchResultConverter = searchResultConverter;
    }
}
