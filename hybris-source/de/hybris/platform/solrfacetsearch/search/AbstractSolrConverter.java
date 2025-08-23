package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.impl.SolrResult;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractSolrConverter<T> implements Converter<SolrResult, T>
{
    private FieldNameTranslator fieldNameTranslator;


    public T convert(SolrResult source)
    {
        return (T)convert(source, createDataObject());
    }


    protected <TYPE> TYPE getValue(SolrResult solrResult, String propertyName)
    {
        IndexedProperty indexedProperty = (IndexedProperty)solrResult.getQuery().getIndexedType().getIndexedProperties().get(propertyName);
        if(indexedProperty == null)
        {
            return (TYPE)solrResult.getDocument().getFirstValue(propertyName);
        }
        return getValue(solrResult, indexedProperty);
    }


    protected <TYPE> TYPE getValue(SolrResult solrResult, IndexedProperty property)
    {
        String fieldName = translateFieldName(solrResult.getQuery(), property);
        if(property.isMultiValue())
        {
            return (TYPE)solrResult.getDocument().getFieldValues(fieldName);
        }
        return (TYPE)solrResult.getDocument().getFirstValue(fieldName);
    }


    protected String translateFieldName(SearchQuery searchQuery, IndexedProperty property)
    {
        return getFieldNameTranslator().translate(searchQuery, property.getName(), FieldNameProvider.FieldType.INDEX);
    }


    protected FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    protected abstract T createDataObject();
}
