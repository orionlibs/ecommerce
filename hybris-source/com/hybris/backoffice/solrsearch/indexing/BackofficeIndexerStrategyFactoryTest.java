package com.hybris.backoffice.solrsearch.indexing;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.indexer.impl.BackofficeIndexerStrategy;
import com.hybris.backoffice.solrsearch.indexer.impl.BackofficeIndexerStrategyFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.DefaultIndexerStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.DefaultIndexerStrategyFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

public class BackofficeIndexerStrategyFactoryTest
{
    private static final String BACKOFFICE_CONFIG_NAME = "BackofficeFacetSearchConfig1";
    private static final String OTHER_CONFIG_NAME = "someOtherConfig";
    private static final String INDEXER_STRATEGY_BEAN_NAME = "indexerStrategy";
    private static final String BACKOFFICE_INDEXER_STRATEGY = "backofficeIndexerStrategy";
    @InjectMocks
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private FacetSearchConfig facetSearchConfig;
    @Mock
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    private BackofficeIndexerStrategyFactory backofficeIndexerStrategyFactory;
    private DefaultIndexerStrategyFactory defaultIndexerStrategyFactory;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.backofficeIndexerStrategyFactory = new BackofficeIndexerStrategyFactory();
        this.defaultIndexerStrategyFactory = new DefaultIndexerStrategyFactory();
        this.defaultIndexerStrategyFactory.setIndexerStrategyBeanId("indexerStrategy");
        this.backofficeIndexerStrategyFactory.setApplicationContext(this.applicationContext);
        this.backofficeIndexerStrategyFactory.setBackofficeFacetSearchConfigService(this.facetSearchConfigService);
        this.defaultIndexerStrategyFactory.setApplicationContext(this.applicationContext);
        this.backofficeIndexerStrategyFactory.setDefaultIndexerStrategyFactory(this.defaultIndexerStrategyFactory);
    }


    @Test
    public void shouldCreateBackofficeIndexerStrategy() throws Exception
    {
        DefaultIndexerStrategy expectedIndexerStrategy = (DefaultIndexerStrategy)Mockito.mock(BackofficeIndexerStrategy.class);
        this.backofficeIndexerStrategyFactory.setIndexerStrategyBeanName("backofficeIndexerStrategy");
        Mockito.when(this.facetSearchConfig.getName()).thenReturn("BackofficeFacetSearchConfig1");
        Mockito.when(this.applicationContext.getBean("backofficeIndexerStrategy", IndexerStrategy.class)).thenReturn(expectedIndexerStrategy);
        Mockito.when(Boolean.valueOf(this.facetSearchConfigService.isValidSearchConfiguredForName("BackofficeFacetSearchConfig1")))
                        .thenReturn(Boolean.TRUE);
        IndexerStrategy indexerStrategy = this.backofficeIndexerStrategyFactory.createIndexerStrategy(this.facetSearchConfig);
        Assert.assertSame(expectedIndexerStrategy, indexerStrategy);
    }


    @Test
    public void shouldCreateDefaultIndexerStrategy() throws Exception
    {
        DefaultIndexerStrategy expectedIndexerStrategy = (DefaultIndexerStrategy)Mockito.mock(DefaultIndexerStrategy.class);
        this.backofficeIndexerStrategyFactory.setIndexerStrategyBeanName("backofficeIndexerStrategy");
        Mockito.when(this.facetSearchConfig.getName()).thenReturn("someOtherConfig");
        Mockito.when(this.applicationContext.getBean("indexerStrategy", IndexerStrategy.class)).thenReturn(expectedIndexerStrategy);
        Mockito.when(Boolean.valueOf(this.facetSearchConfigService.isValidSearchConfiguredForName("someOtherConfig")))
                        .thenReturn(Boolean.FALSE);
        Mockito.when(this.facetSearchConfig.getIndexConfig()).thenReturn(new IndexConfig());
        IndexerStrategy indexerStrategy = this.backofficeIndexerStrategyFactory.createIndexerStrategy(this.facetSearchConfig);
        Assert.assertSame(expectedIndexerStrategy, indexerStrategy);
    }
}
