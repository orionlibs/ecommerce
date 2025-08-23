package com.hybris.backoffice.solrsearch.services.impl;

import com.google.common.collect.Iterators;
import com.hybris.backoffice.search.services.impl.AbstractBackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
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

public class BackofficeSolrFacetSearchConfigService extends AbstractBackofficeFacetSearchConfigService<FacetSearchConfig, SolrFacetSearchConfigModel, SolrIndexedTypeModel, IndexedType>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BackofficeSolrFacetSearchConfigService.class);
    private FacetSearchConfigService facetSearchConfigService;


    public FacetSearchConfig getFacetSearchConfig(String typeCode)
    {
        FacetSearchConfig facetSearchConfig = null;
        SolrFacetSearchConfigModel searchConfigModel = findFacetSearchConfigModelInternal(typeCode);
        if(searchConfigModel != null)
        {
            try
            {
                facetSearchConfig = this.facetSearchConfigService.getConfiguration(searchConfigModel.getName());
            }
            catch(FacetConfigServiceException ex)
            {
                LOGGER.error(ex.getMessage());
            }
        }
        return facetSearchConfig;
    }


    public Collection<FacetSearchConfig> getAllMappedFacetSearchConfigs()
    {
        Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigMappings = this.facetSearchConfigDAO.findAllSearchConfigs();
        if(searchConfigMappings != null)
        {
            return (Collection<FacetSearchConfig>)searchConfigMappings.stream().map(this::getFacetSearchConfigFromMapping).filter(Objects::nonNull)
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public Collection<ComposedTypeModel> getAllMappedTypes()
    {
        Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigMappings = this.facetSearchConfigDAO.findAllSearchConfigs();
        if(searchConfigMappings != null)
        {
            return (Collection<ComposedTypeModel>)searchConfigMappings.stream().map(config -> config.getIndexedType()).filter(Objects::nonNull)
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public SolrIndexedTypeModel getIndexedTypeModel(String typeCode)
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


    public boolean isValidSearchConfiguredForType(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        return (findIndexedTypeInConfig(searchConfig) != null);
    }


    public boolean isValidSearchConfiguredForName(String facetSearchConfigName)
    {
        Optional<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfig = this.facetSearchConfigDAO.findSearchConfigurationsForName(facetSearchConfigName).stream().findFirst();
        return searchConfig.isPresent();
    }


    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    protected SolrFacetSearchConfigModel findFacetSearchConfigModelInternal(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        if(searchConfig != null)
        {
            return searchConfig.getSolrFacetSearchConfig();
        }
        return null;
    }


    private BackofficeIndexedTypeToSolrFacetSearchConfigModel findSearchConfigForTypeCode(String typeCode)
    {
        BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig = null;
        if(this.backofficeFacetSearchConfigCache.containsSearchConfigForTypeCode(typeCode))
        {
            searchConfig = (BackofficeIndexedTypeToSolrFacetSearchConfigModel)this.backofficeFacetSearchConfigCache.getSearchConfigForTypeCode(typeCode);
        }
        else
        {
            ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(typeCode);
            List<ComposedTypeModel> typeCodes = getWithSuperTypeCodes(composedType);
            Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> searchConfigs = this.facetSearchConfigDAO.findSearchConfigurationsForTypes(typeCodes);
            searchConfig = findMatchingConfig(composedType, searchConfigs);
            this.backofficeFacetSearchConfigCache.putSearchConfigForTypeCode(typeCode, searchConfig);
        }
        return searchConfig;
    }


    private BackofficeIndexedTypeToSolrFacetSearchConfigModel findMatchingConfig(ComposedTypeModel composedType, Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> configs)
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


    private BackofficeIndexedTypeToSolrFacetSearchConfigModel findMatchingConfigRecursively(ComposedTypeModel composedType, Map<String, BackofficeIndexedTypeToSolrFacetSearchConfigModel> configMap)
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


    private FacetSearchConfig getFacetSearchConfigFromMapping(BackofficeIndexedTypeToSolrFacetSearchConfigModel mapping)
    {
        try
        {
            return this.facetSearchConfigService.getConfiguration(mapping.getSolrFacetSearchConfig().getName());
        }
        catch(FacetConfigServiceException e)
        {
            LOGGER.warn("No facet search configuration found for name {} mapped from type {}", new Object[] {mapping
                            .getSolrFacetSearchConfig().getName(), mapping.getIndexedType().getCode(), e});
            return null;
        }
    }


    private SolrIndexedTypeModel findIndexedTypeInConfig(BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig)
    {
        Optional<SolrIndexedTypeModel> indexedTypeModel = Optional.empty();
        if(searchConfig != null && CollectionUtils.isNotEmpty(searchConfig.getSolrFacetSearchConfig().getSolrIndexedTypes()))
        {
            ComposedTypeModel indexedType = searchConfig.getIndexedType();
            indexedTypeModel = searchConfig.getSolrFacetSearchConfig().getSolrIndexedTypes().stream().filter(idxType -> Objects.equals(idxType.getType(), indexedType)).findFirst();
        }
        return indexedTypeModel.orElse(null);
    }


    private IndexedType findMatchingIndexedTypeRecursively(ComposedTypeModel composedType, Map<String, IndexedType> typesMap)
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
}
