package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSortField;
import de.hybris.platform.solrfacetsearch.model.SolrSortFieldModel;

public class DefaultIndexedTypeSortFieldPopulator implements Populator<SolrSortFieldModel, IndexedTypeSortField>
{
    public void populate(SolrSortFieldModel source, IndexedTypeSortField target)
    {
        target.setFieldName(source.getFieldName());
        target.setAscending(source.isAscending());
    }
}
