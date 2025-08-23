package de.hybris.platform.solrfacetsearch.integration;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.Test;
import org.springframework.util.StopWatch;

@PerformanceTest
public class SearchQueryFacetsPerfSpec extends AbstractSearchQueryPerfTest
{
    private static final Logger LOG = Logger.getLogger(SearchQueryFacetsPerfSpec.class);
    protected static final int BATCH_SIZE = 1000;
    protected static final int PRODUCT_COUNT = 100000;
    protected static final String SINGLE_VALUED_FACET_PROPERTY_PREFIX = "singleValuedFacetProperty";
    protected static final int SINGLE_VALUED_FACET_COUNT = 10;
    protected static final String MULTI_VALUED_FACET_PROPERTY_PREFIX = "multiValuedFacetProperty";
    protected static final int MULTI_VALUED_FACET_COUNT = 10;
    protected static final int MULTI_VALUED_FACET_VALUE_COUNT = 25;
    protected static final int FACET_DISTINCT_VALUE_COUNT = 100;
    protected static final String GROUP_PROPERTY_PREFIX = "groupProperty";
    protected static final boolean GROUP = true;
    protected static final boolean GROUP_FACETS = true;
    protected static final int GROUP_DISTINCT_VALUE_COUNT = Math.max(1, 2000);
    protected static final int SEARCH_DURATION_MILLIS = 60000;


    @Test
    public void searchQueryFacetsPerfTest() throws Exception
    {
        StopWatch setupTimer = new StopWatch("setup");
        setupTimer.start();
        createProperties();
        createProducts(100000, 1000);
        indexProducts();
        setupTimer.stop();
        List<FacetField> facets = buildSearchQueryFacets();
        SearchResult searchResult = executeSearchQuery(facets);
        StopWatch queryTimer = new StopWatch("search");
        queryTimer.start();
        int searchQueryCount = 0;
        for(long startTime = System.currentTimeMillis(); System.currentTimeMillis() - startTime < 60000L;
                        searchQueryCount++)
        {
            executeSearchQuery(facets);
        }
        queryTimer.stop();
        float totalSetupTimeSeconds = (float)setupTimer.getTotalTimeMillis() / 1000.0F;
        float totalSearchTimeSeconds = (float)queryTimer.getTotalTimeMillis() / 1000.0F;
        float searchQueriesPerSecond = searchQueryCount / (float)queryTimer.getTotalTimeMillis() / 1000.0F;
        float avgTimePerSearchQuery = (searchQueryCount == 0) ? 0.0F : ((float)queryTimer.getTotalTimeMillis() / searchQueryCount);
        LOG.info("**************************************************");
        LOG.info("productCount: 100000");
        LOG.info("");
        LOG.info("singleValuedFacetCount: 10");
        LOG.info("multiValuedFacetCount: 10");
        LOG.info("multiValuedFacetValueCount: 25");
        LOG.info("facetDistinctValueCount: 100");
        LOG.info("");
        LOG.info("group: true");
        LOG.info("groupFacets: true");
        LOG.info("distinctGroupCount: " + GROUP_DISTINCT_VALUE_COUNT);
        LOG.info("");
        LOG.info("totalSetupTimeSeconds(s): " + totalSetupTimeSeconds);
        LOG.info("totalSearchTimeSeconds(s): " + totalSearchTimeSeconds);
        LOG.info("searchQueryCount: " + searchQueryCount);
        LOG.info("searchQueries/s: " + searchQueriesPerSecond);
        LOG.info("avgTimePerSearchQuery(ms): " + avgTimePerSearchQuery);
        LOG.info("**************************************************");
        Assertions.assertThat(searchResult.getNumberOfResults()).isCloseTo(GROUP_DISTINCT_VALUE_COUNT, Percentage.withPercentage(1.0D));
        Assertions.assertThat(searchResult.getFacets()).hasSize(20);
    }


    protected void createProperties() throws Exception
    {
        updateIndexType("singleValuedFacetProperty", 10, indexedProperty -> {
            indexedProperty.setType(SolrPropertiesTypes.STRING);
            indexedProperty.setMultiValue(false);
            indexedProperty.setFieldValueProvider("randomStringValueResolver");
            indexedProperty.setValueProviderParameters(Map.ofEntries((Map.Entry<?, ?>[])new Map.Entry[] {(Map.Entry)Assertions.entry("valueCount", String.valueOf(1)), (Map.Entry)Assertions.entry("distinctValueCount", String.valueOf(100))}));
        });
        updateIndexType("multiValuedFacetProperty", 10, indexedProperty -> {
            indexedProperty.setType(SolrPropertiesTypes.STRING);
            indexedProperty.setMultiValue(true);
            indexedProperty.setFieldValueProvider("randomStringValueResolver");
            indexedProperty.setValueProviderParameters(Map.ofEntries((Map.Entry<?, ?>[])new Map.Entry[] {(Map.Entry)Assertions.entry("valueCount", String.valueOf(25)), (Map.Entry)Assertions.entry("distinctValueCount", String.valueOf(100))}));
        });
        updateIndexType("groupProperty", 1, indexedProperty -> {
            indexedProperty.setType(SolrPropertiesTypes.STRING);
            indexedProperty.setMultiValue(false);
            indexedProperty.setFieldValueProvider("randomStringValueResolver");
            indexedProperty.setValueProviderParameters(Map.ofEntries((Map.Entry<?, ?>[])new Map.Entry[] {(Map.Entry)Assertions.entry("valueCount", String.valueOf(1)), (Map.Entry)Assertions.entry("distinctValueCount", String.valueOf(GROUP_DISTINCT_VALUE_COUNT))}));
        });
    }


    protected List<FacetField> buildSearchQueryFacets()
    {
        List<FacetField> facets = new ArrayList<>();
        int facetIndex;
        for(facetIndex = 0; facetIndex < 10; facetIndex++)
        {
            facets.add(new FacetField("singleValuedFacetProperty" + facetIndex));
        }
        for(facetIndex = 0; facetIndex < 10; facetIndex++)
        {
            facets.add(new FacetField("multiValuedFacetProperty" + facetIndex));
        }
        return facets;
    }


    protected SearchResult executeSearchQuery(List<FacetField> facets) throws Exception
    {
        return executeSearchQuery(searchQuery -> {
            searchQuery.getFacets().addAll(facets);
            searchQuery.addGroupCommand("groupProperty0");
            searchQuery.setGroupFacets(true);
        });
    }
}
