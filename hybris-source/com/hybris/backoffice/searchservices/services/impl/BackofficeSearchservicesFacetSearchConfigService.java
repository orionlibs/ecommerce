package com.hybris.backoffice.searchservices.services.impl;

import com.google.common.collect.Iterators;
import com.hybris.backoffice.search.services.impl.AbstractBackofficeFacetSearchConfigService;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BackofficeSearchservicesFacetSearchConfigService extends AbstractBackofficeFacetSearchConfigService<Object, Object, SnIndexTypeModel, Object>
{
    public Object getFacetSearchConfig(String typeCode)
    {
        return null;
    }


    public Collection getAllMappedFacetSearchConfigs()
    {
        return this.facetSearchConfigDAO.findAllSearchConfigs();
    }


    public Collection<ComposedTypeModel> getAllMappedTypes()
    {
        return Collections.emptyList();
    }


    public SnIndexTypeModel getIndexedTypeModel(String typeCode)
    {
        BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        return findIndexTypeInConfig(searchConfig).orElse(null);
    }


    public Object getIndexedType(Object config, String typeCode)
    {
        return null;
    }


    public boolean isValidSearchConfiguredForType(String typeCode)
    {
        BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        return findIndexTypeInConfig(searchConfig).isPresent();
    }


    public boolean isValidSearchConfiguredForName(String facetSearchConfigName)
    {
        Optional<BackofficeIndexedTypeToSearchservicesIndexConfigModel> searchConfig = this.facetSearchConfigDAO.findSearchConfigurationsForName(facetSearchConfigName).stream().findFirst();
        return searchConfig.isPresent();
    }


    protected SnIndexConfigurationModel findFacetSearchConfigModelInternal(String typeCode)
    {
        BackofficeIndexedTypeToSearchservicesIndexConfigModel mappingConfigModel = findSearchConfigForTypeCode(typeCode);
        if(Objects.nonNull(mappingConfigModel))
        {
            return mappingConfigModel.getSnIndexConfiguration();
        }
        return null;
    }


    public Optional<BackofficeIndexedTypeToSearchservicesIndexConfigModel> findSearchConfigForTypeCodeAndIndexTypeId(String typeCode, String indexTypeId)
    {
        BackofficeIndexedTypeToSearchservicesIndexConfigModel matchedSearchConfig = null;
        BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig = findSearchConfigForTypeCode(typeCode);
        if(searchConfig != null && indexTypeId.equals(searchConfig.getSnIndexType().getId()))
        {
            matchedSearchConfig = searchConfig;
        }
        return Optional.ofNullable(matchedSearchConfig);
    }


    public BackofficeIndexedTypeToSearchservicesIndexConfigModel findSearchConfigForTypeCode(String typeCode)
    {
        BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig = null;
        if(this.backofficeFacetSearchConfigCache.containsSearchConfigForTypeCode(typeCode))
        {
            searchConfig = (BackofficeIndexedTypeToSearchservicesIndexConfigModel)this.backofficeFacetSearchConfigCache.getSearchConfigForTypeCode(typeCode);
        }
        else
        {
            ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(typeCode);
            List<ComposedTypeModel> typeCodes = getWithSuperTypeCodes(composedType);
            Collection<BackofficeIndexedTypeToSearchservicesIndexConfigModel> searchConfigs = this.facetSearchConfigDAO.findSearchConfigurationsForTypes(typeCodes);
            searchConfig = findMatchingConfig(composedType, searchConfigs);
            this.backofficeFacetSearchConfigCache.putSearchConfigForTypeCode(typeCode, searchConfig);
        }
        return searchConfig;
    }


    private BackofficeIndexedTypeToSearchservicesIndexConfigModel findMatchingConfig(ComposedTypeModel composedType, Collection<BackofficeIndexedTypeToSearchservicesIndexConfigModel> configs)
    {
        if(configs == null)
        {
            return null;
        }
        if(configs.size() == 1)
        {
            return (BackofficeIndexedTypeToSearchservicesIndexConfigModel)Iterators.getOnlyElement(configs.iterator());
        }
        Map<String, BackofficeIndexedTypeToSearchservicesIndexConfigModel> configMap = (Map<String, BackofficeIndexedTypeToSearchservicesIndexConfigModel>)configs.stream().collect(Collectors.toMap(c -> c.getIndexedType().getCode(), c -> c));
        return findMatchingConfigRecursively(composedType, configMap);
    }


    private BackofficeIndexedTypeToSearchservicesIndexConfigModel findMatchingConfigRecursively(ComposedTypeModel composedType, Map<String, BackofficeIndexedTypeToSearchservicesIndexConfigModel> configMap)
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


    private Optional<SnIndexTypeModel> findIndexTypeInConfig(BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig)
    {
        Optional<SnIndexTypeModel> indexTypeModel = Optional.empty();
        if(null != searchConfig && null != searchConfig.getSnIndexType())
        {
            ComposedTypeModel indexedType = searchConfig.getIndexedType();
            ComposedTypeModel indexedType4SnIndexType = searchConfig.getSnIndexType().getItemComposedType();
            if(indexedType.getPk().equals(indexedType4SnIndexType.getPk()))
            {
                indexTypeModel = Optional.of(searchConfig.getSnIndexType());
            }
        }
        return indexTypeModel;
    }
}
