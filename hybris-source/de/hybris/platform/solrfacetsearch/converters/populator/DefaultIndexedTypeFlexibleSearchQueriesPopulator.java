package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexerQueryModel;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexedTypeFlexibleSearchQueriesPopulator implements Populator<SolrIndexedTypeModel, IndexedType>
{
    private Converter<SolrIndexerQueryModel, IndexedTypeFlexibleSearchQuery> indexedTypeFlexibleSearchQueryConverter;


    public void populate(SolrIndexedTypeModel source, IndexedType target)
    {
        Map<IndexOperation, IndexedTypeFlexibleSearchQuery> queries = new EnumMap<>(IndexOperation.class);
        for(SolrIndexerQueryModel queryModel : source.getSolrIndexerQueries())
        {
            IndexedTypeFlexibleSearchQuery queryData = (IndexedTypeFlexibleSearchQuery)this.indexedTypeFlexibleSearchQueryConverter.convert(queryModel);
            queries.put(IndexOperation.valueOf(queryModel.getType().toString()), queryData);
        }
        target.setFlexibleSearchQueries(queries);
    }


    @Required
    public void setIndexedTypeFlexibleSearchQueryConverter(Converter<SolrIndexerQueryModel, IndexedTypeFlexibleSearchQuery> indexedTypeFlexibleSearchQueryConverter)
    {
        this.indexedTypeFlexibleSearchQueryConverter = indexedTypeFlexibleSearchQueryConverter;
    }
}
