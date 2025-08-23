package com.hybris.backoffice.solrsearch.dataaccess;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SolrSearchStrategy implements FullTextSearchStrategy
{
    public static final String PREFERRED_STRATEGY_NAME = "solr";
    private static final String DEFAULT_TYPE = "default";
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearchStrategy.class);
    private BackofficeFacetSearchConfigService<FacetSearchConfig, SolrFacetSearchConfigModel, SolrIndexedTypeModel, IndexedType> backofficeFacetSearchConfigService;
    private Map<String, String> typeMappings;
    private Map<String, Set<String>> operatorConfig;


    protected IndexedProperty getIndexedProperty(String typeCode, String name)
    {
        try
        {
            FacetSearchConfig facetSearchConfig = (FacetSearchConfig)getBackofficeFacetSearchConfigService().getFacetSearchConfig(typeCode);
            IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().stream().filter(index -> StringUtils.equals(index.getComposedType().getCode(), typeCode)).findFirst().orElse(null);
            if(indexedType != null)
            {
                return (IndexedProperty)indexedType.getIndexedProperties().get(name);
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        return null;
    }


    public String getFieldType(String typeCode, String fieldName)
    {
        IndexedProperty indexedProperty = getIndexedProperty(typeCode, fieldName);
        if(indexedProperty != null)
        {
            return getTypeMappings().get(indexedProperty.getType());
        }
        return null;
    }


    public boolean isLocalized(String typeCode, String fieldName)
    {
        IndexedProperty indexedProperty = getIndexedProperty(typeCode, fieldName);
        return (indexedProperty != null && indexedProperty.isLocalized());
    }


    public Collection<String> getAvailableLanguages(String typeCode)
    {
        try
        {
            SolrFacetSearchConfigModel solrFacetSearchConfigModel = (SolrFacetSearchConfigModel)getBackofficeFacetSearchConfigService().getFacetSearchConfigModel(typeCode);
            return (Collection<String>)solrFacetSearchConfigModel.getLanguages().stream()
                            .map(C2LItemModel::getIsocode).collect(Collectors.toList());
        }
        catch(Exception e)
        {
            LOGGER.error(String.format("Error loading facet search configuration for type [%s]", new Object[] {typeCode}), e);
            return Collections.emptyList();
        }
    }


    protected BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    @Required
    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService<FacetSearchConfig, SolrFacetSearchConfigModel, SolrIndexedTypeModel, IndexedType> backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public Collection<ValueComparisonOperator> getAvailableOperators(String typeCode, String fieldName)
    {
        return getAvailableOperators(getFieldType(typeCode, fieldName));
    }


    protected Collection<ValueComparisonOperator> getAvailableOperators(String fieldType)
    {
        Set<String> operators = getOperatorConfig().getOrDefault(fieldType, getOperatorConfig().get("default"));
        return (Collection<ValueComparisonOperator>)operators.stream().map(ValueComparisonOperator::valueOf).collect(Collectors.toList());
    }


    protected Map<String, String> getTypeMappings()
    {
        return this.typeMappings;
    }


    @Required
    public void setTypeMappings(Map<String, String> typeMappings)
    {
        this.typeMappings = new LinkedHashMap<>(typeMappings);
    }


    protected Map<String, Set<String>> getOperatorConfig()
    {
        return this.operatorConfig;
    }


    @Required
    public void setOperatorConfig(Map<String, Set<String>> operatorConfig)
    {
        this.operatorConfig = operatorConfig;
    }


    public String getStrategyName()
    {
        return "solr";
    }
}
