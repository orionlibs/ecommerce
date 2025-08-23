package com.hybris.backoffice.solrsearch.indexing;

import com.hybris.backoffice.solrsearch.daos.SolrModifiedItemDAO;
import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.indexer.cron.BackofficeSolrIndexerDeleteJob;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.utils.SolrPlatformUtils;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexOperationIdGenerator;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

public class BackofficeSolrIndexerDeleteJobTest
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSolrIndexerDeleteJobTest.class);
    private static final String MODIFIED_TYPE_CODE = "Product";
    private static final Long DELETED_PK = Long.valueOf(1L);
    private static final String SEARCH_CONFIG_NAME = "Product Index";
    private static final SolrServerMode SOLR_SERVER_MODE = SolrServerMode.EMBEDDED;
    @Mock
    private ModelService modelService;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private SolrModifiedItemDAO solrModifiedItemDAO;
    @Mock
    private FacetSearchConfigService facetSearchConfigService;
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private Exporter exporterService;
    @Mock
    private SolrIndexService solrIndexService;
    @Mock
    private SolrSearchProviderFactory solrSearchProviderFactory;
    @Mock
    private IndexOperationIdGenerator indexOperationIdGenerator;
    @Mock
    private IndexerBatchContextFactory indexerBatchContextFactory;
    @InjectMocks
    private BackofficeSolrIndexerDeleteJob indexerDeleteJob;
    private SolrModifiedItemModel deletedItem;
    private SolrFacetSearchConfigModel searchConfig;
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;


    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        this.deletedItem = new SolrModifiedItemModel();
        this.deletedItem.setModifiedTypeCode("Product");
        this.deletedItem.setModifiedPk(DELETED_PK);
        this.deletedItem.setModificationType(SolrItemModificationType.DELETE);
        SolrServerConfigModel serverConfig = new SolrServerConfigModel();
        this.searchConfig = new SolrFacetSearchConfigModel();
        this.searchConfig.setName("Product Index");
        this.searchConfig.setSolrServerConfig(serverConfig);
        Map<String, IndexedType> indexedTypes = new HashMap<>();
        this.indexedType = new IndexedType();
        this.indexedType.setCode("Product");
        indexedTypes.put("Product", this.indexedType);
        IndexConfig indexConfig = new IndexConfig();
        indexConfig.setIndexedTypes(indexedTypes);
        SolrConfig solrConfig = new SolrConfig();
        solrConfig.setMode(SolrServerMode.EMBEDDED);
        this.facetSearchConfig = new FacetSearchConfig();
        this.facetSearchConfig.setIndexConfig(indexConfig);
        this.facetSearchConfig.setSolrConfig(solrConfig);
        Mockito.when(this.beanFactory.getBean(SolrPlatformUtils.createSolrExporterBeanName(SOLR_SERVER_MODE), Exporter.class))
                        .thenReturn(this.exporterService);
        Mockito.when(Long.valueOf(this.indexOperationIdGenerator.generate((FacetSearchConfig)Matchers.any(), (IndexedType)Matchers.any(), (Index)Matchers.any()))).thenReturn(Long.valueOf(1L));
        Mockito.when(this.indexerBatchContextFactory.createContext(Matchers.anyLong(), (IndexOperation)Matchers.any(), Matchers.anyBoolean(), (FacetSearchConfig)Matchers.any(), (IndexedType)Matchers.any(), (Collection)Matchers.any()))
                        .thenReturn(Mockito.mock(IndexerBatchContext.class));
    }


    @Test
    public void shouldDeleteSingleItemFromSolrIndex() throws ExporterException
    {
        List<SolrModifiedItemModel> modifiedItems = Collections.singletonList(this.deletedItem);
        Mockito.when(this.solrModifiedItemDAO.findByModificationType(SolrItemModificationType.DELETE)).thenReturn(modifiedItems);
        try
        {
            Mockito.when(this.backofficeFacetSearchConfigService.getSolrFacetSearchConfigModel("Product")).thenReturn(this.searchConfig);
            Mockito.when(this.facetSearchConfigService.getConfiguration("Product Index")).thenReturn(this.facetSearchConfig);
        }
        catch(FacetConfigServiceException e)
        {
            LOG.error("Facet configuration error", (Throwable)e);
            Fail.fail(e.getMessage());
        }
        SolrSearchProvider solrSearchProvider = (SolrSearchProvider)Mockito.mock(SolrSearchProvider.class);
        SolrIndexModel activeIndex = (SolrIndexModel)Mockito.mock(SolrIndexModel.class);
        Mockito.when(activeIndex.getQualifier()).thenReturn("qualifier");
        try
        {
            Mockito.when(this.solrIndexService.getActiveIndex(this.facetSearchConfig.getName(), this.indexedType.getIdentifier())).thenReturn(activeIndex);
            Mockito.when(this.solrSearchProviderFactory.getSearchProvider(this.facetSearchConfig, this.indexedType)).thenReturn(solrSearchProvider);
        }
        catch(SolrServiceException e)
        {
            LOG.error("Facet configuration error", (Throwable)e);
            Fail.fail(e.getMessage());
        }
        this.indexerDeleteJob.performIndexingJob(new CronJobModel());
        List<String> pks = (List<String>)Stream.<SolrModifiedItemModel>of(this.deletedItem).map(i -> i.getModifiedPk().toString()).collect(Collectors.toList());
        ((Exporter)Mockito.verify(this.exporterService)).exportToDeleteFromIndex(pks, this.facetSearchConfig, this.indexedType);
        ((ModelService)Mockito.verify(this.modelService)).removeAll(modifiedItems);
    }
}
