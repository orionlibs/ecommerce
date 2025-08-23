package com.hybris.backoffice.solrsearch.indexing;

import com.hybris.backoffice.solrsearch.daos.SolrModifiedItemDAO;
import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.indexer.cron.BackofficeSolrIndexerUpdateJob;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackofficeSolrIndexerUpdateJobTest
{
    private static final String MODIFIED_TYPE_CODE = "Product";
    private static final Long MODIFIED_PK = Long.valueOf(1L);
    private static final String SEARCH_CONFIG_NAME = "Product Index";
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSolrIndexerUpdateJobTest.class);
    @Mock
    private ModelService modelService;
    @Mock
    private SolrModifiedItemDAO solrModifiedItemDAO;
    @Mock
    private FacetSearchConfigService facetSearchConfigService;
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private IndexerService indexerService;
    @InjectMocks
    private BackofficeSolrIndexerUpdateJob indexerUpdateJob;
    private SolrModifiedItemModel updatedItem;
    private SolrFacetSearchConfigModel searchConfig;
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;


    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        this.updatedItem = new SolrModifiedItemModel();
        this.updatedItem.setModifiedTypeCode("Product");
        this.updatedItem.setModifiedPk(MODIFIED_PK);
        this.updatedItem.setModificationType(SolrItemModificationType.UPDATE);
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
    }


    @Test
    public void shouldUpdateSingleItemInSolrIndex() throws IndexerException
    {
        List<SolrModifiedItemModel> modifiedItems = Collections.singletonList(this.updatedItem);
        Mockito.when(this.solrModifiedItemDAO.findByModificationType(SolrItemModificationType.UPDATE)).thenReturn(modifiedItems);
        Mockito.when(this.modelService.get(PK.fromLong(this.updatedItem.getModifiedPk().longValue()))).thenReturn(this.updatedItem);
        try
        {
            Mockito.when(this.backofficeFacetSearchConfigService.getSolrFacetSearchConfigModel("Product")).thenReturn(this.searchConfig);
            Mockito.when(this.facetSearchConfigService.getConfiguration("Product Index")).thenReturn(this.facetSearchConfig);
        }
        catch(FacetConfigServiceException e)
        {
            LOG.error("Facet configuration error", (Throwable)e);
            Fail.fail("Facet configuration error");
        }
        this.indexerUpdateJob.performIndexingJob(new CronJobModel());
        List<PK> pks = (List<PK>)Stream.<SolrModifiedItemModel>of(this.updatedItem).map(i -> PK.fromLong(i.getModifiedPk().longValue())).collect(Collectors.toList());
        ((IndexerService)Mockito.verify(this.indexerService)).updateTypeIndex(this.facetSearchConfig, this.indexedType, pks);
        ((ModelService)Mockito.verify(this.modelService)).removeAll(modifiedItems);
    }
}
