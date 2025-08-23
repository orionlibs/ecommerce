package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import de.hybris.platform.solrfacetsearch.provider.QualifierProviderAware;
import de.hybris.platform.solrfacetsearch.provider.ValueProviderSelectionStrategy;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFieldNameTranslator implements FieldNameTranslator, BeanFactoryAware
{
    public static final String FIELD_INFOS_MAPPING_KEY = "solrfacetsearch.fieldInfos";
    private FieldNameProvider fieldNameProvider;
    private ValueProviderSelectionStrategy valueProviderSelectionStrategy;
    private BeanFactory beanFactory;


    public FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }


    public ValueProviderSelectionStrategy getValueProviderSelectionStrategy()
    {
        return this.valueProviderSelectionStrategy;
    }


    public void setValueProviderSelectionStrategy(ValueProviderSelectionStrategy valueProviderSelectionStrategy)
    {
        this.valueProviderSelectionStrategy = valueProviderSelectionStrategy;
    }


    public BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public String translate(SearchQuery searchQuery, String field, FieldNameProvider.FieldType fieldType)
    {
        IndexedType indexedType = searchQuery.getIndexedType();
        IndexedProperty indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(field);
        String translatedField = null;
        if(indexedProperty != null)
        {
            translatedField = translateFromProperty(searchQuery, indexedProperty, fieldType);
        }
        else
        {
            translatedField = translateFromType(searchQuery, field);
        }
        if(translatedField == null)
        {
            translatedField = field;
        }
        return translatedField;
    }


    public String translate(FacetSearchContext searchContext, String field, FieldNameProvider.FieldType fieldType)
    {
        return translate(searchContext.getSearchQuery(), field, fieldType);
    }


    public String translate(FacetSearchContext searchContext, String field)
    {
        String translatedField;
        FieldNameTranslator.FieldInfosMapping fieldInfosMapping = getFieldInfos(searchContext);
        FieldNameTranslator.FieldInfo fieldInfo = (FieldNameTranslator.FieldInfo)fieldInfosMapping.getFieldInfos().get(field);
        if(fieldInfo != null)
        {
            translatedField = fieldInfo.getTranslatedFieldName();
        }
        else
        {
            translatedField = field;
        }
        return translatedField;
    }


    public FieldNameTranslator.FieldInfosMapping getFieldInfos(FacetSearchContext searchContext)
    {
        DefaultFieldInfosMapping fieldInfosMapping = (DefaultFieldInfosMapping)searchContext.getAttributes().get("solrfacetsearch.fieldInfos");
        if(fieldInfosMapping == null)
        {
            Map<String, FieldNameTranslator.FieldInfo> fieldInfos = new HashMap<>();
            Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos = new HashMap<>();
            SearchQuery searchQuery = searchContext.getSearchQuery();
            IndexedType indexedType = searchContext.getIndexedType();
            for(IndexedProperty indexedProperty : indexedType.getIndexedProperties().values())
            {
                String fieldName = indexedProperty.getName();
                String translatedFieldName = translateFromProperty(searchQuery, indexedProperty, FieldNameProvider.FieldType.INDEX);
                DefaultFieldInfo fieldInfo = new DefaultFieldInfo();
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setTranslatedFieldName(translatedFieldName);
                fieldInfo.setIndexedProperty(indexedProperty);
                fieldInfos.put(fieldName, fieldInfo);
                invertedFieldInfos.put(translatedFieldName, fieldInfo);
            }
            Object typeValueProvider = getTypeValueProvider(indexedType);
            if(typeValueProvider instanceof IndexedTypeFieldsValuesProvider)
            {
                Map<String, String> fieldNamesMapping = ((IndexedTypeFieldsValuesProvider)typeValueProvider).getFieldNamesMapping();
                for(Map.Entry<String, String> entry : fieldNamesMapping.entrySet())
                {
                    String fieldName = entry.getKey();
                    String translatedFieldName = entry.getValue();
                    DefaultFieldInfo fieldInfo = new DefaultFieldInfo();
                    fieldInfo.setFieldName(fieldName);
                    fieldInfo.setTranslatedFieldName(translatedFieldName);
                    fieldInfos.put(fieldName, fieldInfo);
                    invertedFieldInfos.put(translatedFieldName, fieldInfo);
                }
            }
            fieldInfosMapping = new DefaultFieldInfosMapping();
            fieldInfosMapping.setFieldInfos(fieldInfos);
            fieldInfosMapping.setInvertedFieldInfos(invertedFieldInfos);
            searchContext.getAttributes().put("solrfacetsearch.fieldInfos", fieldInfosMapping);
        }
        return (FieldNameTranslator.FieldInfosMapping)fieldInfosMapping;
    }


    protected String translateFromProperty(SearchQuery searchQuery, IndexedProperty indexedProperty, FieldNameProvider.FieldType fieldType)
    {
        IndexedType indexedType = searchQuery.getIndexedType();
        String fieldQualifier = null;
        String valueProviderId = this.valueProviderSelectionStrategy.resolveValueProvider(indexedType, indexedProperty);
        Object valueProvider = this.valueProviderSelectionStrategy.getValueProvider(valueProviderId);
        QualifierProvider qualifierProvider = (valueProvider instanceof QualifierProviderAware) ? ((QualifierProviderAware)valueProvider).getQualifierProvider() : null;
        if(qualifierProvider != null && qualifierProvider.canApply(indexedProperty))
        {
            Qualifier qualifier = qualifierProvider.getCurrentQualifier();
            fieldQualifier = (qualifier != null) ? qualifier.toFieldQualifier() : null;
        }
        else if(indexedProperty.isLocalized())
        {
            fieldQualifier = searchQuery.getLanguage();
        }
        else if(indexedProperty.isCurrency())
        {
            fieldQualifier = searchQuery.getCurrency();
        }
        return this.fieldNameProvider.getFieldName(indexedProperty, fieldQualifier, fieldType);
    }


    protected String translateFromType(SearchQuery searchQuery, String field)
    {
        IndexedType indexedType = searchQuery.getIndexedType();
        Object typeValueProvider = getTypeValueProvider(indexedType);
        if(typeValueProvider instanceof IndexedTypeFieldsValuesProvider)
        {
            Map<String, String> fieldNamesMapping = ((IndexedTypeFieldsValuesProvider)typeValueProvider).getFieldNamesMapping();
            return fieldNamesMapping.get(field);
        }
        return null;
    }


    protected Object getTypeValueProvider(IndexedType indexedType)
    {
        String fieldsValuesProvider = indexedType.getFieldsValuesProvider();
        if(fieldsValuesProvider != null)
        {
            return this.beanFactory.getBean(fieldsValuesProvider);
        }
        return null;
    }
}
