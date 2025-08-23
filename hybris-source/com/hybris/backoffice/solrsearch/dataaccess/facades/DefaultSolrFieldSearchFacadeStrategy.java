package com.hybris.backoffice.solrsearch.dataaccess.facades;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.converters.FullTextSearchDataConverter;
import com.hybris.backoffice.solrsearch.daos.SolrFieldSearchDAO;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchService;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.backoffice.widgets.advancedsearch.engine.PageableWithFullTextDataCallback;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.search.AutosuggestionSupport;
import com.hybris.cockpitng.dataaccess.facades.search.OrderedFieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class DefaultSolrFieldSearchFacadeStrategy<T extends ItemModel> implements AutosuggestionSupport, OrderedFieldSearchFacadeStrategy<T>
{
    public static final String STRATEGY_NAME = "solr";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSolrFieldSearchFacadeStrategy.class);
    private BackofficeFacetSearchService facetSearchService;
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    private CommonI18NService commonI18NService;
    private SolrFieldSearchDAO solrFieldSearchDAO;
    private SolrAutoSuggestService solrAutoSuggestService;
    private FullTextSearchDataConverter fullTextSearchDataConverter;
    private int strategyLoadOrder;


    public boolean canHandle(String typeCode)
    {
        return canHandle(typeCode, (Context)new DefaultContext());
    }


    public boolean canHandle(String typeCode, Context context)
    {
        if(context != null)
        {
            if(context.getAttribute("originalQuery") != null)
            {
                Object query = context.getAttribute("originalQuery");
                if(query instanceof AdvancedSearchQueryData && ((AdvancedSearchQueryData)query)
                                .getAdvancedSearchMode() != AdvancedSearchMode.SIMPLE)
                {
                    return false;
                }
            }
            Object modelPageable = context.getAttribute("pageable");
            if(modelPageable != null)
            {
                boolean isSolrPageable = (modelPageable instanceof PageableWithFullTextDataCallback && ((PageableWithFullTextDataCallback)modelPageable).getPageable() instanceof BackofficeSolrPageable);
                return (isSolrPageable && this.facetSearchConfigService.isValidSearchConfiguredForType(typeCode));
            }
        }
        return this.facetSearchConfigService.isValidSearchConfiguredForType(typeCode);
    }


    public boolean isSortable(DataType type, String attributeQualifier, Context context)
    {
        return false;
    }


    public Pageable<T> search(SearchQueryData queryData)
    {
        return (queryData != null) ? (Pageable<T>)new BackofficeSolrPageable(this, queryData) : (Pageable<T>)new PageableList(Collections.emptyList(), 1);
    }


    public Map<String, Collection<String>> getAutosuggestionsForQuery(AutosuggestionQueryData queryData)
    {
        return getAutosuggestionsForQuery(queryData, (Context)new DefaultContext());
    }


    public Map<String, Collection<String>> getAutosuggestionsForQuery(AutosuggestionQueryData queryData, Context context)
    {
        SolrIndexedTypeModel indexedType = (SolrIndexedTypeModel)this.facetSearchConfigService.getIndexedTypeModel(queryData.getSearchType());
        if(indexedType != null)
        {
            try
            {
                SolrSuggestion solrSuggestion = this.solrAutoSuggestService.getAutoSuggestionsForQuery(this.commonI18NService
                                .getCurrentLanguage(), indexedType, queryData.getQueryText());
                return solrSuggestion.getSuggestions();
            }
            catch(SolrAutoSuggestException e)
            {
                LOG.warn("Couldn't retrieve auto suggestions for query: {}, and type: {}", new Object[] {queryData.getQueryText(), queryData
                                .getSearchType(), e});
            }
        }
        return Collections.emptyMap();
    }


    protected List<ItemModel> getFilteredResults(List<ItemModel> itemModels)
    {
        return itemModels;
    }


    @Required
    public void setFacetSearchService(BackofficeFacetSearchService facetSearchService)
    {
        this.facetSearchService = facetSearchService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setFacetSearchConfigService(BackofficeFacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    @Required
    public void setSolrFieldSearchDAO(SolrFieldSearchDAO solrFieldSearchDAO)
    {
        this.solrFieldSearchDAO = solrFieldSearchDAO;
    }


    @Required
    public void setSolrAutoSuggestService(SolrAutoSuggestService solrAutoSuggestService)
    {
        this.solrAutoSuggestService = solrAutoSuggestService;
    }


    @Required
    public void setFullTextSearchDataConverter(FullTextSearchDataConverter fullTextSearchDataConverter)
    {
        this.fullTextSearchDataConverter = fullTextSearchDataConverter;
    }


    public int getOrder()
    {
        return this.strategyLoadOrder;
    }


    public void setOrder(int order)
    {
        this.strategyLoadOrder = order;
    }


    public String getStrategyName()
    {
        return "solr";
    }
}
