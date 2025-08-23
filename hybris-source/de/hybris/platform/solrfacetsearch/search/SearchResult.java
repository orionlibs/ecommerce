package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.enums.ConverterType;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface SearchResult
{
    int getOffset();


    int getPageSize();


    boolean hasNext();


    boolean hasPrevious();


    long getNumberOfPages();


    long getNumberOfResults();


    List<String> getIdentifiers();


    List<PK> getResultPKs() throws FacetSearchException;


    List<String> getResultCodes() throws FacetSearchException;


    List<? extends ItemModel> getResults() throws FacetSearchException;


    <T> List<T> getResultData(ConverterType paramConverterType);


    List<Document> getDocuments();


    void setGroupCommandResult(SearchResultGroupCommand paramSearchResultGroupCommand);


    SearchResultGroupCommand getGroupCommandResult();


    @Deprecated(since = "2105", forRemoval = true)
    List<SearchResultGroupCommand> getGroupCommands();


    Set<String> getFacetNames();


    boolean containsFacet(String paramString);


    Facet getFacet(String paramString);


    List<Facet> getFacets();


    String getSpellingSuggestion();


    List<KeywordRedirectValue> getKeywordRedirects();


    List<IndexedTypeSort> getAvailableNamedSorts();


    IndexedTypeSort getCurrentNamedSort();


    List<Breadcrumb> getBreadcrumbs();


    SearchQueryInfo getQueryInfo();


    QueryResponse getSolrObject();


    Map<String, Object> getAttributes();
}
