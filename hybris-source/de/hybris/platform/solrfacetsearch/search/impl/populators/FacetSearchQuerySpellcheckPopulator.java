package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

public class FacetSearchQuerySpellcheckPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    public static final String SPELLCHECK_PARAM = "spellcheck";
    public static final String SPELLCHECK_QUERY_PARAM = "spellcheck.q";
    public static final String SPELLCHECK_DICTIONARY_PARAM = "spellcheck.dictionary";
    public static final String SPELLCHECK_COLLATE_PARAM = "spellcheck.collate";


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        if(searchQuery.isEnableSpellcheck() && StringUtils.isNotBlank(searchQuery.getUserQuery()))
        {
            target.add("spellcheck", new String[] {"true"});
            target.add("spellcheck.q", new String[] {searchQuery.getUserQuery()});
            target.add("spellcheck.dictionary", new String[] {searchQuery.getLanguage()});
            target.add("spellcheck.collate", new String[] {Boolean.TRUE.toString()});
        }
    }
}
