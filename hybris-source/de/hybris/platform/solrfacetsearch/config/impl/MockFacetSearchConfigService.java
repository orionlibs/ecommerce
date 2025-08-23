package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigs;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfigs;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypes;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSets;
import de.hybris.platform.solrfacetsearch.config.ValueRanges;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class MockFacetSearchConfigService implements FacetSearchConfigService
{
    private Map<String, FacetSearchConfig> facetSearchConfigs;
    @Resource
    private CommonI18NService commonI18NService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private TypeService typeService;
    @Resource
    private FieldValueProvider productPriceValueProvider;
    @Resource
    private FieldValueProvider modelPropertyFieldValueProvider;
    @Resource
    private IdentityProvider productIdentityProvider;


    public FacetSearchConfig getConfiguration(String name)
    {
        if(this.facetSearchConfigs == null)
        {
            this.facetSearchConfigs = createConfigs();
        }
        return this.facetSearchConfigs.get(name);
    }


    public FacetSearchConfig getConfiguration(CatalogVersionModel catalogVersion)
    {
        throw new UnsupportedOperationException("not supported for mock service");
    }


    public IndexedType resolveIndexedType(FacetSearchConfig facetSearchConfig, String indexedTypeName) throws FacetConfigServiceException
    {
        throw new UnsupportedOperationException("not supported for mock service");
    }


    public List<IndexedProperty> resolveIndexedProperties(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<String> indexedPropertiesIds) throws FacetConfigServiceException
    {
        throw new UnsupportedOperationException("not supported for mock service");
    }


    protected Map<String, FacetSearchConfig> createConfigs()
    {
        Map<String, FacetSearchConfig> configs = new HashMap<>(1);
        configs.put("productSearch", createPoductSearchConfig());
        return configs;
    }


    protected FacetSearchConfig createPoductSearchConfig()
    {
        String name = "productSearch";
        String description = "Product Search";
        IndexConfig indexConfig = createProductIndexConfig();
        return FacetSearchConfigs.createFacetSearchConfig("productSearch", "Product Search", indexConfig, null, null);
    }


    protected IndexConfig createProductIndexConfig()
    {
        Collection<IndexedType> indexedTypes = createIndexedTypes();
        Collection<CatalogVersionModel> catalogVersions = getCatalogVersions();
        Collection<LanguageModel> languages = getLanguages();
        Collection<CurrencyModel> currencies = getCurrencies();
        return IndexConfigs.createIndexConfig(indexedTypes, catalogVersions, languages, currencies, null, 100, 1, false);
    }


    protected Collection<CurrencyModel> getCurrencies()
    {
        return Arrays.asList(new CurrencyModel[] {this.commonI18NService.getCurrency("EUR"), this.commonI18NService.getCurrency("USD")});
    }


    protected Collection<LanguageModel> getLanguages()
    {
        return Arrays.asList(new LanguageModel[] {this.commonI18NService.getLanguage("de"), this.commonI18NService.getLanguage("en")});
    }


    protected Collection<CatalogVersionModel> getCatalogVersions()
    {
        return Collections.singleton(this.catalogVersionService.getCatalogVersion("hwcatalog", "Online"));
    }


    protected Collection<IndexedType> createIndexedTypes()
    {
        return Collections.singleton(createProductIndexType());
    }


    protected IndexedType createProductIndexType()
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForCode("Product");
        return IndexedTypes.createIndexedType(composedType, false, createIndexedPropertiesForProduct(), null, "productIdentityProvider", null, null, null, "Product", null);
    }


    protected Collection<IndexedProperty> createIndexedPropertiesForProduct()
    {
        List<IndexedProperty> properties = new ArrayList<>();
        properties.add(createIndexProperty("name", "string", false, null, true, this.modelPropertyFieldValueProvider));
        properties.add(createIndexProperty("onlineDate", "date", false, null, false, this.modelPropertyFieldValueProvider));
        List<ValueRange> valueRangesEUR = new ArrayList<>();
        valueRangesEUR.add(ValueRanges.createValueRange("1-1000", Double.valueOf(1.0D), Double.valueOf(1000.0D)));
        valueRangesEUR.add(ValueRanges.createValueRange("1001-INF", Double.valueOf(1001.0D), null));
        ValueRangeSet valueRangeSetEUR = ValueRangeSets.createValueRangeSet("EUR", valueRangesEUR);
        List<ValueRange> valueRangesUSD = new ArrayList<>();
        valueRangesUSD.add(ValueRanges.createValueRange("1-2000", Double.valueOf(1.0D), Double.valueOf(2000.0D)));
        valueRangesUSD.add(ValueRanges.createValueRange("2001-INF", Double.valueOf(2001.0D), null));
        ValueRangeSet valueRangeSetUSD = ValueRangeSets.createValueRangeSet("USD", valueRangesUSD);
        properties.add(createIndexProperty("price", "double", true, Arrays.asList(new ValueRangeSet[] {valueRangeSetEUR, valueRangeSetUSD}, ), false, this.productPriceValueProvider));
        return properties;
    }


    protected IndexedProperty createIndexProperty(String propertyName, String type, boolean facet, List<ValueRangeSet> valueRangeSets, boolean localized, FieldValueProvider fieldValueProvider)
    {
        IndexedProperty property = new IndexedProperty();
        property.setName(propertyName);
        property.setType(type);
        property.setFacet(facet);
        property.setExportId(propertyName);
        Map<Object, Object> valueRangeSets2 = new HashMap<>();
        if(valueRangeSets != null)
        {
            for(ValueRangeSet valueRangeSet : valueRangeSets)
            {
                String qualifier = valueRangeSet.getQualifier();
                if(qualifier == null)
                {
                    qualifier = "default";
                }
                valueRangeSets2.put(qualifier, valueRangeSet);
            }
        }
        property.setValueRangeSets(valueRangeSets2);
        property.setLocalized(localized);
        property.setFieldValueProvider("productPriceValueProvider");
        return property;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setProductPriceValueProvider(FieldValueProvider productPriceValueProvider)
    {
        this.productPriceValueProvider = productPriceValueProvider;
    }


    public void setModelPropertyFieldValueProvider(FieldValueProvider modelPropertyFieldValueProvider)
    {
        this.modelPropertyFieldValueProvider = modelPropertyFieldValueProvider;
    }


    public void setProductIdentityProvider(IdentityProvider productIdentityProvider)
    {
        this.productIdentityProvider = productIdentityProvider;
    }
}
