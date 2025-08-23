package com.hybris.backoffice.solrsearch.indexing;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.events.DirectSolrIndexSynchronizationStrategy;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DirectSolrIndexSynchronizationStrategyTest
{
    public static final String PRODUCT_TYPECODE = "Product";
    public static final long PK_ = 1L;
    public static final String CONFIG_NAME = "backoffice_product";
    @Mock
    private ComposedTypeModel typeModel;
    @Mock
    private BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig;
    @Mock
    private SolrFacetSearchConfigModel solrFacetSearchConfigModel;
    @Mock
    private FacetSearchConfigService facetSearchConfigService;
    @Mock
    private IndexerService indexerService;
    @Mock
    private SolrIndexService solrIndexService;
    @Mock
    private SolrIndexModel solrIndexModel;
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private TypeService typeService;
    @Mock
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType = new IndexedType();
    @InjectMocks
    private DirectSolrIndexSynchronizationStrategy strategy = new DirectSolrIndexSynchronizationStrategy();


    @Before
    public void init() throws FacetConfigServiceException
    {
        MockitoAnnotations.initMocks(this);
        SolrConfig config = new SolrConfig();
        config.setMode(SolrServerMode.EMBEDDED);
        this.indexedType.setCode("Product");
        Map<String, IndexedType> indexedTypes = new HashMap<>();
        indexedTypes.put("bo_product", this.indexedType);
        IndexConfig indexConfig = new IndexConfig();
        indexConfig.setIndexedTypes(indexedTypes);
        Mockito.when(this.searchConfig.getSolrFacetSearchConfig()).thenReturn(this.solrFacetSearchConfigModel);
        Mockito.when(this.searchConfig.getSolrFacetSearchConfig().getName()).thenReturn("backoffice_product");
        Mockito.when(this.typeService.getTypeForCode("Product")).thenReturn(this.typeModel);
        Mockito.when(this.backofficeFacetSearchConfigService.getFacetSearchConfigModel("Product")).thenReturn(this.solrFacetSearchConfigModel);
        Mockito.when(this.facetSearchConfigService.getConfiguration("backoffice_product")).thenReturn(this.facetSearchConfig);
        Mockito.when(this.facetSearchConfig.getSolrConfig()).thenReturn(config);
        Mockito.when(this.facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
    }


    @Test
    public void testRemoveItemWhenIndexInitialized() throws FacetConfigServiceException, IndexerException, SolrServiceException
    {
        Mockito.when(this.solrIndexService.getActiveIndex((String)Matchers.any(), (String)Matchers.any())).thenReturn(this.solrIndexModel);
        this.strategy.removeItem("Product", 1L);
        ((FacetSearchConfigService)Mockito.verify(this.facetSearchConfigService)).getConfiguration("backoffice_product");
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((IndexerService)Mockito.verify(this.indexerService)).deleteTypeIndex((FacetSearchConfig)Matchers.eq(this.facetSearchConfig), (IndexedType)Matchers.eq(this.indexedType), (List)pkList.capture());
        Assertions.assertThat((List)pkList.getValue()).hasSize(1);
        Assertions.assertThat((List)pkList.getValue()).containsExactly(new Object[] {PK.fromLong(1L)});
    }


    @Test
    public void testRemoveItemWhenIndexNotInitialized() throws FacetConfigServiceException, IndexerException, SolrServiceException
    {
        Mockito.when(this.solrIndexService.getActiveIndex((String)Matchers.any(), (String)Matchers.any())).thenThrow(new Throwable[] {(Throwable)new SolrServiceException("Test Error occurred")});
        this.strategy.removeItem("Product", 1L);
        ((FacetSearchConfigService)Mockito.verify(this.facetSearchConfigService)).getConfiguration("backoffice_product");
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((IndexerService)Mockito.verify(this.indexerService, Mockito.never())).deleteTypeIndex((FacetSearchConfig)Matchers.eq(this.facetSearchConfig), (IndexedType)Matchers.eq(this.indexedType), (List)pkList.capture());
    }


    @Test
    public void testUpdateItemWhenIndexInitialized() throws FacetConfigServiceException, IndexerException, SolrServiceException
    {
        Mockito.when(this.solrIndexService.getActiveIndex((String)Matchers.any(), (String)Matchers.any())).thenReturn(this.solrIndexModel);
        this.strategy.updateItem("Product", 1L);
        ((FacetSearchConfigService)Mockito.verify(this.facetSearchConfigService)).getConfiguration("backoffice_product");
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((IndexerService)Mockito.verify(this.indexerService)).updateTypeIndex(
                        (FacetSearchConfig)Matchers.eq(this.facetSearchConfig),
                        (IndexedType)Matchers.eq(this.indexedType), (List)pkList
                                        .capture());
        Assertions.assertThat((List)pkList.getValue()).hasSize(1);
        Assertions.assertThat((List)pkList.getValue()).containsExactly(new Object[] {PK.fromLong(1L)});
    }


    @Test
    public void testUpdateItemWhenIndexNotInitialized() throws FacetConfigServiceException, IndexerException, SolrServiceException
    {
        Mockito.when(this.solrIndexService.getActiveIndex((String)Matchers.any(), (String)Matchers.any())).thenThrow(new Throwable[] {(Throwable)new SolrServiceException("Test Error occurred")});
        this.strategy.updateItem("Product", 1L);
        ((FacetSearchConfigService)Mockito.verify(this.facetSearchConfigService)).getConfiguration("backoffice_product");
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((IndexerService)Mockito.verify(this.indexerService, Mockito.never())).updateTypeIndex(
                        (FacetSearchConfig)Matchers.eq(this.facetSearchConfig),
                        (IndexedType)Matchers.eq(this.indexedType), (List)pkList
                                        .capture());
    }
}
