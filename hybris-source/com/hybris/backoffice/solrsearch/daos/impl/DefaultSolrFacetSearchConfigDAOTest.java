package com.hybris.backoffice.solrsearch.daos.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class DefaultSolrFacetSearchConfigDAOTest extends ServicelayerTransactionalTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private SolrFacetSearchConfigDAO solrFacetSearchConfigDAO;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultCatalog();
    }


    @Test
    public void testFindSearchConfigForType() throws Exception
    {
        ComposedTypeModel productCT = this.typeService.getComposedTypeForCode("Product");
        ComposedTypeModel variantCT = this.typeService.getComposedTypeForCode("VariantProduct");
        Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigForTypes = this.solrFacetSearchConfigDAO.findSearchConfigurationsForTypes(Lists.newArrayList((Object[])new ComposedTypeModel[] {productCT, variantCT}));
        Assertions.assertThat(searchConfigForTypes).isEmpty();
        createBackofficeIndexedTypeConfig("Product");
        searchConfigForTypes = this.solrFacetSearchConfigDAO.findSearchConfigurationsForTypes(Lists.newArrayList((Object[])new ComposedTypeModel[] {productCT, variantCT}));
        Assertions.assertThat(searchConfigForTypes).isNotEmpty();
        Assertions.assertThat(searchConfigForTypes.size()).isEqualTo(1);
        createBackofficeIndexedTypeConfig("VariantProduct");
        searchConfigForTypes = this.solrFacetSearchConfigDAO.findSearchConfigurationsForTypes(Lists.newArrayList((Object[])new ComposedTypeModel[] {productCT, variantCT}));
        Assertions.assertThat(searchConfigForTypes).isNotEmpty();
        Assertions.assertThat(searchConfigForTypes.size()).isEqualTo(2);
    }


    private String createBackofficeIndexedTypeConfig(String typeCode)
    {
        String configName = "testConfig" + System.currentTimeMillis();
        SolrFacetSearchConfigModel testSolrConfig = createFacetSearchConfig(configName, typeCode);
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(typeCode);
        BackofficeIndexedTypeToSolrFacetSearchConfigModel model = (BackofficeIndexedTypeToSolrFacetSearchConfigModel)this.modelService.create(BackofficeIndexedTypeToSolrFacetSearchConfigModel.class);
        model.setIndexedType(composedTypeForCode);
        model.setSolrFacetSearchConfig(testSolrConfig);
        this.modelService.save(model);
        return configName;
    }


    private SolrFacetSearchConfigModel createFacetSearchConfig(String configName, String typeCode)
    {
        SolrSearchConfigModel solrConfig = (SolrSearchConfigModel)this.modelService.create(SolrSearchConfigModel.class);
        solrConfig.setPageSize(Integer.valueOf(100));
        solrConfig.setLegacyMode(false);
        SolrServerConfigModel serverConfigModel = (SolrServerConfigModel)this.modelService.create(SolrServerConfigModel.class);
        serverConfigModel.setName(configName);
        serverConfigModel.setMode(SolrServerModes.STANDALONE);
        SolrIndexConfigModel indexConfigModel = (SolrIndexConfigModel)this.modelService.create(SolrIndexConfigModel.class);
        indexConfigModel.setName(configName);
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(typeCode);
        SolrIndexedTypeModel indexedType = (SolrIndexedTypeModel)this.modelService.create(SolrIndexedTypeModel.class);
        indexedType.setIdentifier(configName);
        indexedType.setType(composedTypeForCode);
        SolrFacetSearchConfigModel cfgModel = (SolrFacetSearchConfigModel)this.modelService.create(SolrFacetSearchConfigModel.class);
        cfgModel.setName(configName);
        cfgModel.setEnabledLanguageFallbackMechanism(true);
        cfgModel.setSolrSearchConfig(solrConfig);
        cfgModel.setSolrServerConfig(serverConfigModel);
        cfgModel.setSolrIndexedTypes(Collections.singletonList(indexedType));
        cfgModel.setSolrIndexConfig(indexConfigModel);
        this.modelService.saveAll();
        return cfgModel;
    }
}
