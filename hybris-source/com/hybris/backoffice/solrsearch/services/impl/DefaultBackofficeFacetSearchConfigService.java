package com.hybris.backoffice.solrsearch.services.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.hybris.backoffice.solrsearch.cache.BackofficeFacetSearchConfigCache;
import com.hybris.backoffice.solrsearch.daos.SolrFacetSearchConfigDAO;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class DefaultBackofficeFacetSearchConfigService implements BackofficeFacetSearchConfigService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeFacetSearchConfigService.class);
    private SolrFacetSearchConfigDAO solrFacetSearchConfigDAO;
    private FacetSearchConfigService facetSearchConfigService;
    private TypeService typeService;
    private BackofficeFacetSearchConfigCache configCache;


    public FacetSearchConfig getFacetSearchConfig(String typeCode) throws FacetConfigServiceException
    {
        SolrFacetSearchConfigModel searchConfigModel = findSolrFacetSearchConfigModelInternal(typeCode);
        if(searchConfigModel != null)
        {
            return this.facetSearchConfigService.getConfiguration(searchConfigModel.getName());
        }
        return null;
    }


    public Collection<FacetSearchConfig> getAllMappedFacetSearchConfigs()
    {
        Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigMappings = this.solrFacetSearchConfigDAO.findAllSearchConfigs();
        if(searchConfigMappings != null)
        {
            return (Collection<FacetSearchConfig>)searchConfigMappings.stream().map(this::getFacetSearchConfigFromMapping).filter(Objects::nonNull)
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public Collection<ComposedTypeModel> getAllMappedTypes()
    {
        Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigMappings = this.solrFacetSearchConfigDAO.findAllSearchConfigs();
        if(searchConfigMappings != null)
        {
            return (Collection<ComposedTypeModel>)searchConfigMappings.stream().map(config -> config.getIndexedType()).filter(Objects::nonNull)
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected FacetSearchConfig getFacetSearchConfigFromMapping(BackofficeIndexedTypeToSolrFacetSearchConfigModel mapping)
    {
        try
        {
            return this.facetSearchConfigService.getConfiguration(mapping.getSolrFacetSearchConfig().getName());
        }
        catch(FacetConfigServiceException e)
        {
            LOG.warn("No facet search configuration found for name {} mapped from type {}", new Object[] {mapping
                            .getSolrFacetSearchConfig().getName(), mapping.getIndexedType().getCode(), e});
            return null;
        }
    }


    public SolrFacetSearchConfigModel getSolrFacetSearchConfigModel(String typeCode) throws FacetConfigServiceException
    {
        return findSolrFacetSearchConfigModelInternal(typeCode);
    }


    public SolrIndexedTypeModel getSolrIndexedType(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel matchingConfig = findSearchConfigForTypeCode(typeCode);
        return findIndexedTypeInConfig(matchingConfig);
    }


    public IndexedType getIndexedType(FacetSearchConfig config, String typeCode)
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(typeCode);
        Map<String, IndexedType> indexedTypeMap = (Map<String, IndexedType>)config.getIndexConfig().getIndexedTypes().values().stream().collect(Collectors.toMap(IndexedType::getCode, it -> it));
        return findMatchingIndexedTypeRecursively(composedType, indexedTypeMap);
    }


    public boolean isSolrSearchConfiguredForType(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        return (findIndexedTypeInConfig(searchConfig) != null);
    }


    public boolean isBackofficeSolrSearchConfiguredForName(String facetSearchConfigName)
    {
        Optional<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfig = this.solrFacetSearchConfigDAO.findSearchConfigurationsForName(facetSearchConfigName).stream().findFirst();
        return searchConfig.isPresent();
    }


    protected IndexedType findMatchingIndexedTypeRecursively(ComposedTypeModel composedType, Map<String, IndexedType> typesMap)
    {
        if("Item".equals(composedType.getCode()))
        {
            return null;
        }
        if(typesMap.containsKey(composedType.getCode()))
        {
            return typesMap.get(composedType.getCode());
        }
        return findMatchingIndexedTypeRecursively(composedType.getSuperType(), typesMap);
    }


    protected SolrIndexedTypeModel findIndexedTypeInConfig(BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig)
    {
        Optional<SolrIndexedTypeModel> indexedTypeModel = Optional.empty();
        if(searchConfig != null && CollectionUtils.isNotEmpty(searchConfig.getSolrFacetSearchConfig().getSolrIndexedTypes()))
        {
            ComposedTypeModel indexedType = searchConfig.getIndexedType();
            indexedTypeModel = searchConfig.getSolrFacetSearchConfig().getSolrIndexedTypes().stream().filter(idxType -> Objects.equals(idxType.getType(), indexedType)).findFirst();
        }
        return indexedTypeModel.orElse(null);
    }


    protected BackofficeIndexedTypeToSolrFacetSearchConfigModel findSearchConfigForTypeCode(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig;
        if(this.configCache.containsSearchConfigForTypeCode(typeCode))
        {
            searchConfig = this.configCache.getSearchConfigForTypeCode(typeCode);
        }
        else
        {
            ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(typeCode);
            List<ComposedTypeModel> typeCodes = getWithSuperTypeCodes(composedType);
            Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigs = this.solrFacetSearchConfigDAO.findSearchConfigurationsForTypes(typeCodes);
            searchConfig = findMatchingConfig(composedType, searchConfigs);
            this.configCache.putSearchConfigForTypeCode(typeCode, searchConfig);
        }
        return searchConfig;
    }


    protected BackofficeIndexedTypeToSolrFacetSearchConfigModel findMatchingConfig(ComposedTypeModel composedType, Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> configs)
    {
        if(configs.size() == 1)
        {
            return (BackofficeIndexedTypeToSolrFacetSearchConfigModel)Iterators.getOnlyElement(configs.iterator());
        }
        if(configs.size() > 1)
        {
            Map<String, BackofficeIndexedTypeToSolrFacetSearchConfigModel> configMap = (Map<String, BackofficeIndexedTypeToSolrFacetSearchConfigModel>)configs.stream().collect(Collectors.toMap(c -> c.getIndexedType().getCode(), c -> c));
            return findMatchingConfigRecursively(composedType, configMap);
        }
        return null;
    }


    protected BackofficeIndexedTypeToSolrFacetSearchConfigModel findMatchingConfigRecursively(ComposedTypeModel composedType, Map<String, BackofficeIndexedTypeToSolrFacetSearchConfigModel> configMap)
    {
        if("Item".equals(composedType.getCode()))
        {
            return null;
        }
        if(configMap.containsKey(composedType.getCode()))
        {
            return configMap.get(composedType.getCode());
        }
        return findMatchingConfigRecursively(composedType.getSuperType(), configMap);
    }


    protected List<ComposedTypeModel> getWithSuperTypeCodes(ComposedTypeModel composedType)
    {
        List<ComposedTypeModel> typeCodes = Lists.newArrayList();
        typeCodes.add(composedType);
        typeCodes.addAll(composedType.getAllSuperTypes());
        return typeCodes;
    }


    protected SolrFacetSearchConfigModel findSolrFacetSearchConfigModelInternal(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        if(searchConfig != null)
        {
            return searchConfig.getSolrFacetSearchConfig();
        }
        return null;
    }


    @Required
    public void setSolrFacetSearchConfigDAO(SolrFacetSearchConfigDAO solrFacetSearchConfigDAO)
    {
        this.solrFacetSearchConfigDAO = solrFacetSearchConfigDAO;
    }


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setConfigCache(BackofficeFacetSearchConfigCache configCache)
    {
        this.configCache = configCache;
    }
}
