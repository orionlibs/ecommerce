package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.config.SearchQueryTemplate;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigUnknownBeanException;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryTemplateModel;
import de.hybris.platform.solrfacetsearch.model.SolrSortModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexedTypePopulator implements Populator<SolrIndexedTypeModel, IndexedType>, BeanFactoryAware
{
    public static final int DEFAULT_GROUP_LIMIT = -1;
    private Converter<SolrIndexedPropertyModel, IndexedProperty> indexedPropertyConverter;
    private Converter<SolrSearchQueryTemplateModel, SearchQueryTemplate> solrSearchQueryTemplateConverter;
    private Converter<SolrSortModel, IndexedTypeSort> indexedTypeSortConverter;
    private BeanFactory beanFactory;
    private String defaultIdentityProvider;
    private String defaultModelLoader;


    public void populate(SolrIndexedTypeModel source, IndexedType target)
    {
        try
        {
            populateBasic(source, target);
            populateGroupProperties(source, target);
            target.setFtsQueryBuilder(source.getFtsQueryBuilder());
            target.setFtsQueryBuilderParameters(
                            Collections.unmodifiableMap(new HashMap<>(source.getFtsQueryBuilderParameters())));
            target.setListeners(Collections.unmodifiableCollection(source.getListeners()));
            target.setSearchQueryTemplates((Map)Converters.convertAll(source.getSearchQueryTemplates(), this.solrSearchQueryTemplateConverter)
                            .stream().collect(Collectors.toMap(SearchQueryTemplate::getName, Function.identity())));
            target.setAdditionalParameters(
                            Collections.unmodifiableMap(new HashMap<>(source.getAdditionalParameters())));
        }
        catch(Exception e)
        {
            throw new ConversionException("Error when converting indexed type", e);
        }
    }


    protected void populateBasic(SolrIndexedTypeModel source, IndexedType target) throws FacetConfigServiceException
    {
        IndexedTypeFieldsValuesProvider modelFieldsValuesProvider = getFieldsValuesProvider(source.getValuesProvider(), source
                        .getIdentifier());
        Collection<IndexedProperty> indexedProperties = getIndexedPropertiesFromItems(source);
        Set<String> facets = getAllFacets(indexedProperties, modelFieldsValuesProvider);
        target.setIdentifier(source.getIdentifier());
        target.setComposedType(source.getType());
        target.setVariant(source.isVariant());
        target.setIndexedProperties(new HashMap<>());
        for(IndexedProperty indexedProperty : indexedProperties)
        {
            target.getIndexedProperties().put(indexedProperty.getName(), indexedProperty);
        }
        target.setIdentityProvider((source.getIdentityProvider() == null) ? this.defaultIdentityProvider : source.getIdentityProvider());
        target.setModelLoader((source.getModelLoader() == null) ? this.defaultModelLoader : source.getModelLoader());
        target.setTypeFacets(facets);
        target.setDefaultFieldValueProvider(source.getDefaultFieldValueProvider());
        target.setFieldsValuesProvider(source.getValuesProvider());
        target.setIndexName(source.getIndexName());
        target.setIndexNameFromConfig(
                        (source.getSolrFacetSearchConfig() != null) ? source.getSolrFacetSearchConfig().getIndexNamePrefix() : null);
        target.setStaged(false);
        target.setCode((target.getComposedType() != null) ? target.getComposedType().getCode() : null);
        target.setSolrResultConverter(source.getSolrResultConverter());
        target.setUniqueIndexedTypeCode(
                        (target.getIndexName() != null) ? target.getCode().concat("_").concat(target.getIndexName()) : target.getCode());
        target.setConfigSet(source.getConfigSet());
        target.setSorts(getIndexedTypeSortConverter().convertAll(source.getSorts()));
        target.setSortsByCode(buildSortsByCode(target));
    }


    protected Map<String, IndexedTypeSort> buildSortsByCode(IndexedType source)
    {
        return (Map<String, IndexedTypeSort>)source.getSorts().stream().collect(Collectors.toMap(IndexedTypeSort::getCode, Function.identity()));
    }


    protected void populateGroupProperties(SolrIndexedTypeModel source, IndexedType target)
    {
        target.setGroup(source.isGroup());
        target.setGroupFieldName(source.getGroupFieldName());
        target.setGroupLimit((source.getGroupLimit() != null) ? source.getGroupLimit().intValue() : -1);
        target.setGroupFacets(source.isGroupFacets());
    }


    protected Collection<IndexedProperty> getIndexedPropertiesFromItems(SolrIndexedTypeModel itemTypeModel)
    {
        Collection<IndexedProperty> result = new ArrayList<>();
        for(SolrIndexedPropertyModel property : itemTypeModel.getSolrIndexedProperties())
        {
            IndexedProperty indexedProperty = getIndexedPropertyFromItem(property);
            result.add(indexedProperty);
        }
        return result;
    }


    protected IndexedProperty getIndexedPropertyFromItem(SolrIndexedPropertyModel property)
    {
        return (IndexedProperty)this.indexedPropertyConverter.convert(property);
    }


    protected Set<String> getAllFacets(Collection<IndexedProperty> properties, IndexedTypeFieldsValuesProvider modelFieldsValuesProvider)
    {
        Set<String> facets = new HashSet<>();
        for(IndexedProperty property : properties)
        {
            if(property.isFacet())
            {
                facets.add(property.getName());
            }
        }
        if(modelFieldsValuesProvider != null)
        {
            facets.addAll(modelFieldsValuesProvider.getFacets());
        }
        return facets.isEmpty() ? Collections.<String>emptySet() : facets;
    }


    protected IndexedTypeFieldsValuesProvider getFieldsValuesProvider(String name, String typeName) throws FacetConfigUnknownBeanException
    {
        if(name == null || "".equals(name))
        {
            return null;
        }
        try
        {
            return (IndexedTypeFieldsValuesProvider)this.beanFactory.getBean(name);
        }
        catch(BeansException e)
        {
            throw new FacetConfigUnknownBeanException("Cannot obtain IndexedTypeFieldsValuesProvider [" + name + "] for IndexedType [" + typeName + "]", e);
        }
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public void setIndexedPropertyConverter(Converter<SolrIndexedPropertyModel, IndexedProperty> indexedPropertyConverter)
    {
        this.indexedPropertyConverter = indexedPropertyConverter;
    }


    @Required
    public void setSolrSearchQueryTemplateConverter(Converter<SolrSearchQueryTemplateModel, SearchQueryTemplate> solrSearchQueryTemplateConverter)
    {
        this.solrSearchQueryTemplateConverter = solrSearchQueryTemplateConverter;
    }


    @Required
    public void setIndexedTypeSortConverter(Converter<SolrSortModel, IndexedTypeSort> indexedTypeSortConverter)
    {
        this.indexedTypeSortConverter = indexedTypeSortConverter;
    }


    protected Converter<SolrSortModel, IndexedTypeSort> getIndexedTypeSortConverter()
    {
        return this.indexedTypeSortConverter;
    }


    public void setDefaultIdentityProvider(String defaultIdentityProvider)
    {
        this.defaultIdentityProvider = defaultIdentityProvider;
    }


    public void setDefaultModelLoader(String defaultModelLoader)
    {
        this.defaultModelLoader = defaultModelLoader;
    }
}
