package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.util.CollectionUtils;

public class DefaultFieldNameProvider implements FieldNameProvider
{
    private static final String USED_SEPARATOR = Config.getString("solr.indexedproperty.forbidden.char", "_");
    private Converter<SolrIndexedPropertyModel, IndexedProperty> indexedPropertyConverter;


    public Converter<SolrIndexedPropertyModel, IndexedProperty> getIndexedPropertyConverter()
    {
        return this.indexedPropertyConverter;
    }


    public void setIndexedPropertyConverter(Converter<SolrIndexedPropertyModel, IndexedProperty> indexedPropertyConverter)
    {
        this.indexedPropertyConverter = indexedPropertyConverter;
    }


    public Collection<String> getFieldNames(IndexedProperty indexedProperty, String qualifier)
    {
        Set<String> fields = new HashSet<>((FieldNameProvider.FieldType.values()).length);
        fields.add(getFieldNameForIndexing(indexedProperty, qualifier));
        fields.add(getFieldNameForSorting(indexedProperty, qualifier));
        if(indexedProperty.isAutoSuggest())
        {
            if(qualifier != null)
            {
                fields.add("autosuggest_" + qualifier
                                .toLowerCase(Locale.ROOT));
            }
            else
            {
                fields.add("autosuggest");
            }
        }
        if(indexedProperty.isSpellCheck())
        {
            if(qualifier != null)
            {
                fields.add("spellcheck_" + qualifier
                                .toLowerCase(Locale.ROOT));
            }
            else
            {
                fields.add("spellcheck");
            }
        }
        return fields;
    }


    public String getFieldName(IndexedProperty indexedProperty, String qualifier, FieldNameProvider.FieldType fieldType)
    {
        if(fieldType == FieldNameProvider.FieldType.INDEX)
        {
            return getFieldNameForIndexing(indexedProperty, qualifier);
        }
        if(fieldType == FieldNameProvider.FieldType.SORT)
        {
            return getFieldNameForSorting(indexedProperty, qualifier);
        }
        throw new IllegalArgumentException("Invalid field type: " + fieldType);
    }


    protected String getFieldNameForIndexing(IndexedProperty indexedProperty, String specifier)
    {
        String exportID = indexedProperty.getExportId();
        String type = indexedProperty.getType();
        ServicesUtil.validateParameterNotNull(exportID, "ExportID or Name of IndexedProperty must not be null");
        ServicesUtil.validateParameterNotNull(type, "type of IndexedProperty must not be null");
        return getFieldName(indexedProperty, exportID, type, specifier);
    }


    protected String getFieldNameForSorting(IndexedProperty indexedProperty, String specifier)
    {
        if(indexedProperty.getSortableType() == null)
        {
            return getFieldNameForIndexing(indexedProperty, specifier);
        }
        String exportID = indexedProperty.getExportId();
        ServicesUtil.validateParameterNotNull(exportID, "ExportID or Name of IndexedProperty must not be null");
        exportID = exportID + exportID + "sortable";
        String type = indexedProperty.getSortableType();
        if(type == null)
        {
            type = indexedProperty.getType();
        }
        ServicesUtil.validateParameterNotNull(type, "type of IndexedProperty must not be null");
        return getFieldName(indexedProperty, exportID, type, specifier);
    }


    protected String getFieldName(IndexedProperty indexedProperty, String name, String type, String specifier)
    {
        String rangeType = type;
        String separator = USED_SEPARATOR;
        if(isRanged(indexedProperty))
        {
            rangeType = "string";
        }
        rangeType = rangeType.toLowerCase(Locale.ROOT);
        StringBuilder fieldName = new StringBuilder();
        if(specifier == null)
        {
            fieldName.append(name).append(separator).append(rangeType);
        }
        else if(rangeType.equals("text"))
        {
            fieldName.append(name).append(separator).append("text").append(separator)
                            .append(specifier.toLowerCase(Locale.ROOT));
        }
        else
        {
            fieldName.append(name).append(separator).append(specifier.toLowerCase(Locale.ROOT)).append(separator).append(rangeType);
        }
        if(indexedProperty.isMultiValue())
        {
            fieldName.append(separator).append("mv");
        }
        return fieldName.toString();
    }


    protected boolean isRanged(IndexedProperty property)
    {
        return !CollectionUtils.isEmpty(property.getValueRangeSets());
    }


    public String getPropertyName(String fieldName)
    {
        int i = fieldName.indexOf(USED_SEPARATOR.charAt(0));
        if(i > 0)
        {
            return fieldName.substring(0, i);
        }
        return fieldName;
    }


    public String getFieldName(SolrIndexedPropertyModel prop, String qualifier, FieldNameProvider.FieldType fieldType) throws FacetConfigServiceException
    {
        return getFieldName((IndexedProperty)this.indexedPropertyConverter.convert(prop), qualifier, FieldNameProvider.FieldType.INDEX);
    }
}
