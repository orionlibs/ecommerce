package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchQueryFieldsPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    private FieldNameTranslator fieldNameTranslator;


    public FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        List<String> fields = searchQuery.getFields();
        if(CollectionUtils.isNotEmpty(fields))
        {
            target.addField("score");
            target.addField("id");
            target.addField("pk");
            for(String field : fields)
            {
                target.addField(this.fieldNameTranslator.translate(searchQuery, field, FieldNameProvider.FieldType.INDEX));
            }
        }
        else
        {
            target.addField("score");
            target.addField("*");
        }
    }
}
