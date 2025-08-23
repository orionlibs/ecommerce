package de.hybris.platform.solrfacetsearch.search;

@Deprecated(since = "5.7")
public interface SolrResultPostProcessor
{
    SearchResult process(SearchResult paramSearchResult);
}
