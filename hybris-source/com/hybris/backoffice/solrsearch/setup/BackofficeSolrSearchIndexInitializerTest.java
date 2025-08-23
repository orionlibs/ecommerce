package com.hybris.backoffice.solrsearch.setup;

import com.hybris.backoffice.search.events.AfterInitializationEndBackofficeSearchListener;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeSolrSearchIndexInitializerTest
{
    @InjectMocks
    private final BackofficeSolrSearchIndexInitializer initializer = (BackofficeSolrSearchIndexInitializer)new Object(this);
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private SolrIndexService solrIndexService;
    @Mock
    private IndexerService indexerService;
    @Mock
    private AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeListener;
    @Mock
    private FacetSearchConfig searchConfig;
    @Mock
    private IndexConfig indexConfig;
    @Mock
    private IndexedType indexedType;
    private boolean indexAutoinitEnabled;
    private static final String INDEXED_TYPE_IDENTIFIER = "INDEXED_TYPE";
    private static final String SEARCH_CONFIG_NAME = "SEARCH_CONFIG";


    private boolean isIndexAutoinitEnabled()
    {
        return this.indexAutoinitEnabled;
    }


    @Before
    public void setUp()
    {
        Mockito.when(this.indexedType.getIdentifier()).thenReturn("INDEXED_TYPE");
        Mockito.when(this.searchConfig.getName()).thenReturn("SEARCH_CONFIG");
    }


    @Test
    public void shouldInitializeIndexIfNotYetInitialized() throws SolrServiceException, IndexerException
    {
        this.indexAutoinitEnabled = true;
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedFacetSearchConfigs()).thenReturn(
                        Collections.singletonList(this.searchConfig));
        Mockito.when(this.searchConfig.getIndexConfig()).thenReturn(this.indexConfig);
        Mockito.when(this.indexConfig.getIndexedTypes()).thenReturn(Collections.singletonMap("INDEXED_TYPE", this.indexedType));
        Mockito.when(this.solrIndexService.getActiveIndex("SEARCH_CONFIG", this.indexedType.getIdentifier()))
                        .thenThrow(new Throwable[] {(Throwable)new SolrServiceException()});
        this.initializer.initialize();
        ((IndexerService)Mockito.verify(this.indexerService)).performFullIndex(this.searchConfig);
    }


    @Test
    public void shouldNotInitializeIndexIfAlreadyInitialized() throws SolrServiceException
    {
        this.indexAutoinitEnabled = true;
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedFacetSearchConfigs()).thenReturn(
                        Collections.singletonList(this.searchConfig));
        Mockito.when(this.searchConfig.getIndexConfig()).thenReturn(this.indexConfig);
        Mockito.when(this.indexConfig.getIndexedTypes()).thenReturn(Collections.singletonMap("INDEXED_TYPE", this.indexedType));
        Mockito.when(this.solrIndexService.getActiveIndex("SEARCH_CONFIG", this.indexedType.getIdentifier())).thenReturn(
                        Mockito.mock(SolrIndexModel.class));
        this.initializer.initialize();
        Mockito.verifyZeroInteractions(new Object[] {this.indexerService});
    }


    @Test
    public void shouldNotInitializeWhenDisabled()
    {
        this.indexAutoinitEnabled = false;
        this.initializer.initialize();
        Mockito.verifyZeroInteractions(new Object[] {this.backofficeFacetSearchConfigService});
    }
}
