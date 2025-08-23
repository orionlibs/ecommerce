package com.hybris.backoffice.searchservices.dataaccess.facades;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class SearchServicesStrategy implements FullTextSearchStrategy
{
    private BackofficeFacetSearchConfigService<Object, Object, SnIndexTypeModel, Object> backofficeFacetSearchConfigService;
    private Map<String, String> typeMappings;
    private Map<String, Set<String>> operatorConfig;


    protected SnFieldModel getSnFieldModel(String typeCode, String name)
    {
        SnIndexTypeModel indexTypeModel = (SnIndexTypeModel)getBackofficeFacetSearchConfigService().getIndexedTypeModel(typeCode);
        if(indexTypeModel != null)
        {
            SnFieldModel snFieldModel = indexTypeModel.getFields().stream().filter(field -> StringUtils.equals(field.getId(), name)).findFirst().orElse(null);
            return snFieldModel;
        }
        return null;
    }


    public String getFieldType(String typeCode, String fieldName)
    {
        SnFieldModel snFieldModel = getSnFieldModel(typeCode, fieldName);
        if(snFieldModel != null)
        {
            return getTypeMappings().get(snFieldModel.getFieldType().getCode());
        }
        return null;
    }


    public boolean isLocalized(String typeCode, String fieldName)
    {
        SnFieldModel snFieldModel = getSnFieldModel(typeCode, fieldName);
        return (snFieldModel != null && snFieldModel.getLocalized().booleanValue());
    }


    public Collection<String> getAvailableLanguages(String typeCode)
    {
        SnIndexConfigurationModel indexConfigurationModel = (SnIndexConfigurationModel)getBackofficeFacetSearchConfigService().getFacetSearchConfigModel(typeCode);
        if(Objects.nonNull(indexConfigurationModel))
        {
            return (Collection<String>)indexConfigurationModel.getLanguages().stream().map(C2LItemModel::getIsocode).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService<Object, Object, SnIndexTypeModel, Object> backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public Collection<ValueComparisonOperator> getAvailableOperators(String typeCode, String fieldName)
    {
        String fieldType = getFieldType(typeCode, fieldName);
        Set<String> operators = getOperatorConfig().getOrDefault(fieldType, getOperatorConfig().get("default"));
        return (Collection<ValueComparisonOperator>)operators.stream().map(ValueComparisonOperator::valueOf).collect(Collectors.toList());
    }


    protected Map<String, String> getTypeMappings()
    {
        return this.typeMappings;
    }


    public void setTypeMappings(Map<String, String> typeMappings)
    {
        this.typeMappings = new LinkedHashMap<>(typeMappings);
    }


    public String getStrategyName()
    {
        return "searchservices";
    }


    public Map<String, Set<String>> getOperatorConfig()
    {
        return this.operatorConfig;
    }


    public void setOperatorConfig(Map<String, Set<String>> operatorConfig)
    {
        this.operatorConfig = operatorConfig;
    }
}
