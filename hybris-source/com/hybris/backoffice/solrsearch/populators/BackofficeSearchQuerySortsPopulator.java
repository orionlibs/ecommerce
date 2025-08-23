package com.hybris.backoffice.solrsearch.populators;

import com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeSearchQuerySortsPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    private FieldNamePostProcessor fieldNamePostProcessor;
    private FieldNameTranslator fieldNameTranslator;


    public void populate(SearchQueryConverterData source, SolrQuery target) throws ConversionException
    {
        if(!(source.getSearchQuery() instanceof BackofficeSearchQuery))
        {
            return;
        }
        BackofficeSearchQuery searchQuery = (BackofficeSearchQuery)source.getSearchQuery();
        for(Iterator<OrderField> iterator = searchQuery.getSorts().iterator(); iterator.hasNext(); )
        {
            OrderField sort = iterator.next();
            if(isFieldLocalized((SearchQuery)searchQuery, sort.getField()))
            {
                List<SolrSearchCondition> conditions = searchQuery.getFieldConditions(sort.getField());
                if(CollectionUtils.isNotEmpty(conditions))
                {
                    conditions.forEach(cdt -> addSortField(target, translate(sort.getField(), (SearchQuery)searchQuery, cdt), sort.isAscending()));
                    continue;
                }
                addSortField(target, translate(sort.getField(), (SearchQuery)searchQuery), sort.isAscending());
                continue;
            }
            addSortField(target, translate(sort.getField(), (SearchQuery)searchQuery), sort.isAscending());
        }
    }


    protected String translate(String field, SearchQuery searchQuery, SolrSearchCondition condition)
    {
        String fieldName = translate(field, searchQuery);
        return "score".equals(field) ? fieldName :
                        this.fieldNamePostProcessor.process(searchQuery, condition.getLanguage(), fieldName);
    }


    protected String translate(String field, SearchQuery searchQuery)
    {
        return "score".equals(field) ? field :
                        this.fieldNameTranslator.translate(searchQuery, field, FieldNameProvider.FieldType.SORT);
    }


    protected void addSortField(SolrQuery target, String fieldName, boolean isAscending)
    {
        target.addSort(fieldName, isAscending ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
    }


    protected boolean isFieldLocalized(SearchQuery searchQuery, String field)
    {
        IndexedProperty indexedProperty = (IndexedProperty)searchQuery.getIndexedType().getIndexedProperties().get(field);
        return (indexedProperty != null && indexedProperty.isLocalized());
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    @Required
    public void setFieldNamePostProcessor(FieldNamePostProcessor fieldNamePostProcessor)
    {
        this.fieldNamePostProcessor = fieldNamePostProcessor;
    }
}
