package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.solr.IndexedPropertyTypeInfo;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexedPropertyTypeRegistry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultSolrIndexedPropertyTypeRegistry implements SolrIndexedPropertyTypeRegistry, ApplicationContextAware, InitializingBean
{
    private static final Logger LOG = Logger.getLogger(DefaultSolrIndexedPropertyTypeRegistry.class);
    private ApplicationContext applicationContext = null;
    private final Map<String, Set<SearchQuery.QueryOperator>> operatorsMap = new HashMap<>();
    private Map<String, String> indexPropertyTypeMapping;
    private List<String> unallowedFacetTypes;
    private List<String> unallowedGroupTypes;


    public void afterPropertiesSet() throws Exception
    {
        Map<String, IndexPropertyTypeOperatorsMapping> mappingBeans = this.applicationContext.getBeansOfType(IndexPropertyTypeOperatorsMapping.class);
        for(Map.Entry<String, IndexPropertyTypeOperatorsMapping> entry : mappingBeans.entrySet())
        {
            for(String type : ((IndexPropertyTypeOperatorsMapping)entry.getValue()).getPropertyTypes())
            {
                this.operatorsMap.put(type, ((IndexPropertyTypeOperatorsMapping)entry.getValue()).getOperators());
            }
        }
    }


    public IndexedPropertyTypeInfo getIndexPropertyTypeInfo(String propertyType)
    {
        IndexedPropertyTypeInfo indexedPropertyType = new IndexedPropertyTypeInfo();
        Set<SearchQuery.QueryOperator> supportedQueryOperators = this.operatorsMap.get(propertyType);
        indexedPropertyType.setSupportedQueryOperators(
                        (supportedQueryOperators != null) ? supportedQueryOperators : Collections.emptySet());
        indexedPropertyType.setAllowFacet(!this.unallowedFacetTypes.contains(propertyType));
        indexedPropertyType.setAllowGroup(!this.unallowedGroupTypes.contains(propertyType));
        String clazz = this.indexPropertyTypeMapping.get(propertyType);
        try
        {
            indexedPropertyType.setJavaType(Class.forName(clazz));
        }
        catch(ClassNotFoundException e)
        {
            LOG.error(e);
            indexedPropertyType.setJavaType(null);
        }
        return indexedPropertyType;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public Map<String, String> getIndexPropertyTypeMapping()
    {
        return this.indexPropertyTypeMapping;
    }


    @Required
    public void setIndexPropertyTypeMapping(Map<String, String> indexPropertyTypeMapping)
    {
        this.indexPropertyTypeMapping = indexPropertyTypeMapping;
    }


    public List<String> getUnallowedFacetTypes()
    {
        return this.unallowedFacetTypes;
    }


    @Required
    public void setUnallowedFacetTypes(List<String> unallowedFacetTypes)
    {
        this.unallowedFacetTypes = unallowedFacetTypes;
    }


    public List<String> getUnallowedGroupTypes()
    {
        return this.unallowedGroupTypes;
    }


    @Required
    public void setUnallowedGroupTypes(List<String> unallowedGroupTypes)
    {
        this.unallowedGroupTypes = unallowedGroupTypes;
    }
}
