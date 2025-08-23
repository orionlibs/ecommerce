package com.hybris.backoffice.solrsearch.services.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.solrsearch.cache.impl.BackofficeSolrFacetSearchConfigCache;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultBackofficeFacetSearchConfigServiceIntegrationTest extends ServicelayerTransactionalTest
{
    public static final String TEST_CONFIG = "testConfig";
    public static final String PRODUCT_INDEXED_TYPE = "ProductIndexedType";
    public static final String CATALOG_INDEXED_TYPE = "CatalogIndexedType";
    public static final String USER_INDEXED_TYPE = "UserIndexedType";
    public static final String PRODUCT = "Product";
    public static final String CATALOG = "Catalog";
    public static final String USER = "User";
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private BackofficeSolrFacetSearchConfigService backofficeSolrFacetSearchConfigService;
    @Resource
    private BackofficeSolrFacetSearchConfigCache backofficeSolrFacetSearchConfigCache;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultCatalog();
    }


    @Test
    public void testGetFacetSearchConfig() throws Exception
    {
        SolrFacetSearchConfigModel config = createConfig("testConfig");
        SolrIndexedTypeModel productType = createIndexedType("Product", "ProductIndexedType");
        SolrIndexedTypeModel catalogType = createIndexedType("Catalog", "CatalogIndexedType");
        SolrIndexedTypeModel userType = createIndexedType("User", "UserIndexedType");
        productType.setSolrFacetSearchConfig(config);
        catalogType.setSolrFacetSearchConfig(config);
        userType.setSolrFacetSearchConfig(config);
        config.setSolrIndexedTypes(Lists.newArrayList((Object[])new SolrIndexedTypeModel[] {productType, catalogType, userType}));
        createBackofficeIndexedConfig("Product", config);
        createBackofficeIndexedConfig("Catalog", config);
        createBackofficeIndexedConfig("User", config);
        this.modelService.saveAll();
        FacetSearchConfig cfgForProduct = this.backofficeSolrFacetSearchConfigService.getFacetSearchConfig("Product");
        FacetSearchConfig cfgForCatalog = this.backofficeSolrFacetSearchConfigService.getFacetSearchConfig("Catalog");
        FacetSearchConfig cfgForUser = this.backofficeSolrFacetSearchConfigService.getFacetSearchConfig("User");
        Assert.assertEquals("testConfig", cfgForProduct.getName());
        Assert.assertEquals("testConfig", cfgForCatalog.getName());
        Assert.assertEquals("testConfig", cfgForUser.getName());
    }


    @Test
    public void testGetAllMappedFacetSearchConfigs()
    {
        SolrFacetSearchConfigModel config = createConfig("testConfig");
        SolrIndexedTypeModel productType = createIndexedType("Product", "ProductIndexedType");
        SolrIndexedTypeModel catalogType = createIndexedType("Catalog", "CatalogIndexedType");
        SolrIndexedTypeModel userType = createIndexedType("User", "UserIndexedType");
        productType.setSolrFacetSearchConfig(config);
        catalogType.setSolrFacetSearchConfig(config);
        userType.setSolrFacetSearchConfig(config);
        config.setSolrIndexedTypes(Lists.newArrayList((Object[])new SolrIndexedTypeModel[] {productType, catalogType, userType}));
        this.modelService.saveAll();
        Collection<FacetSearchConfig> allMappedConfigs = this.backofficeSolrFacetSearchConfigService.getAllMappedFacetSearchConfigs();
        Assert.assertNotNull(allMappedConfigs);
        Assert.assertEquals(0L, allMappedConfigs.size());
        createBackofficeIndexedConfig("Product", config);
        createBackofficeIndexedConfig("Catalog", config);
        createBackofficeIndexedConfig("User", config);
        this.modelService.saveAll();
        allMappedConfigs = this.backofficeSolrFacetSearchConfigService.getAllMappedFacetSearchConfigs();
        Assert.assertNotNull(allMappedConfigs);
        Assert.assertEquals(3L, allMappedConfigs.size());
    }


    @Test
    public void testGetSolrIndexedType() throws Exception
    {
        SolrFacetSearchConfigModel config = createConfig("testConfig");
        SolrIndexedTypeModel catalogType = createIndexedType("Catalog", "CatalogIndexedType");
        catalogType.setSolrFacetSearchConfig(config);
        config.setSolrIndexedTypes(Lists.newArrayList((Object[])new SolrIndexedTypeModel[] {catalogType}));
        createBackofficeIndexedConfig("Catalog", config);
        this.modelService.saveAll();
        SolrIndexedTypeModel catalogIdxType = this.backofficeSolrFacetSearchConfigService.getIndexedTypeModel("Catalog");
        Assert.assertNotNull(catalogIdxType);
        Assert.assertEquals("CatalogIndexedType", catalogIdxType.getIdentifier());
        SolrIndexedTypeModel productIdxType = this.backofficeSolrFacetSearchConfigService.getIndexedTypeModel("Product");
        Assert.assertNull(productIdxType);
        productIdxType = createIndexedType("Product", "ProductIndexedType");
        productIdxType.setSolrFacetSearchConfig(config);
        ArrayList<SolrIndexedTypeModel> configuredTypes = Lists.newArrayList(config.getSolrIndexedTypes());
        configuredTypes.add(productIdxType);
        config.setSolrIndexedTypes(configuredTypes);
        this.modelService.saveAll();
        productIdxType = this.backofficeSolrFacetSearchConfigService.getIndexedTypeModel("Product");
        Assert.assertNull(productIdxType);
        createBackofficeIndexedConfig("Product", config);
        this.modelService.saveAll();
        this.backofficeSolrFacetSearchConfigCache.invalidateCache();
        productIdxType = this.backofficeSolrFacetSearchConfigService.getIndexedTypeModel("Product");
        Assert.assertNotNull(productIdxType);
        Assert.assertEquals("ProductIndexedType", productIdxType.getIdentifier());
    }


    @Test
    public void testIsSolrSearchConfiguredForType() throws Exception
    {
        SolrFacetSearchConfigModel config = createConfig("testConfig");
        SolrIndexedTypeModel productType = createIndexedType("Product", "ProductIndexedType");
        SolrIndexedTypeModel catalogType = createIndexedType("Catalog", "CatalogIndexedType");
        SolrIndexedTypeModel userType = createIndexedType("User", "UserIndexedType");
        productType.setSolrFacetSearchConfig(config);
        catalogType.setSolrFacetSearchConfig(config);
        userType.setSolrFacetSearchConfig(config);
        config.setSolrIndexedTypes(Lists.newArrayList((Object[])new SolrIndexedTypeModel[] {productType, catalogType, userType}));
        this.modelService.saveAll();
        boolean isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("Product");
        Assert.assertFalse(isConfigured);
        BackofficeIndexedTypeToSolrFacetSearchConfigModel backIdxCfg = createBackofficeIndexedConfig("Product", config);
        this.modelService.save(backIdxCfg);
        this.backofficeSolrFacetSearchConfigCache.invalidateCache();
        isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("Product");
        Assert.assertTrue(isConfigured);
        isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("Catalog");
        Assert.assertFalse(isConfigured);
        backIdxCfg = createBackofficeIndexedConfig("Catalog", config);
        this.modelService.save(backIdxCfg);
        this.backofficeSolrFacetSearchConfigCache.invalidateCache();
        isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("Catalog");
        Assert.assertTrue(isConfigured);
    }


    @Test
    public void testConfigForParentType()
    {
        SolrFacetSearchConfigModel config = createConfig("testConfig");
        SolrIndexedTypeModel productType = createIndexedType("Product", "ProductIndexedType");
        productType.setSolrFacetSearchConfig(config);
        config.setSolrIndexedTypes(Lists.newArrayList((Object[])new SolrIndexedTypeModel[] {productType}));
        this.modelService.saveAll();
        boolean isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("Product");
        Assert.assertFalse(isConfigured);
        isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("VariantProduct");
        Assert.assertFalse(isConfigured);
        BackofficeIndexedTypeToSolrFacetSearchConfigModel backIdxCfg = createBackofficeIndexedConfig("Product", config);
        this.modelService.save(backIdxCfg);
        this.backofficeSolrFacetSearchConfigCache.invalidateCache();
        isConfigured = this.backofficeSolrFacetSearchConfigService.isValidSearchConfiguredForType("Product");
        Assert.assertTrue(isConfigured);
    }


    private SolrFacetSearchConfigModel createConfig(String name)
    {
        SolrSearchConfigModel solrConfig = (SolrSearchConfigModel)this.modelService.create(SolrSearchConfigModel.class);
        solrConfig.setPageSize(Integer.valueOf(100));
        solrConfig.setLegacyMode(false);
        SolrServerConfigModel serverConfigModel = (SolrServerConfigModel)this.modelService.create(SolrServerConfigModel.class);
        serverConfigModel.setName(name);
        serverConfigModel.setMode(SolrServerModes.STANDALONE);
        SolrIndexConfigModel indexConfigModel = (SolrIndexConfigModel)this.modelService.create(SolrIndexConfigModel.class);
        indexConfigModel.setName(name);
        SolrFacetSearchConfigModel cfgModel = (SolrFacetSearchConfigModel)this.modelService.create(SolrFacetSearchConfigModel.class);
        cfgModel.setName(name);
        cfgModel.setEnabledLanguageFallbackMechanism(true);
        cfgModel.setSolrSearchConfig(solrConfig);
        cfgModel.setSolrServerConfig(serverConfigModel);
        cfgModel.setSolrIndexConfig(indexConfigModel);
        return cfgModel;
    }


    private SolrIndexedTypeModel createIndexedType(String typeCode, String name)
    {
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(typeCode);
        SolrIndexedTypeModel indexedType = (SolrIndexedTypeModel)this.modelService.create(SolrIndexedTypeModel.class);
        indexedType.setIdentifier(name);
        indexedType.setType(composedTypeForCode);
        return indexedType;
    }


    private BackofficeIndexedTypeToSolrFacetSearchConfigModel createBackofficeIndexedConfig(String typeCode, SolrFacetSearchConfigModel cfgModel)
    {
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(typeCode);
        BackofficeIndexedTypeToSolrFacetSearchConfigModel model = (BackofficeIndexedTypeToSolrFacetSearchConfigModel)this.modelService.create(BackofficeIndexedTypeToSolrFacetSearchConfigModel.class);
        model.setIndexedType(composedTypeForCode);
        model.setSolrFacetSearchConfig(cfgModel);
        return model;
    }
}
