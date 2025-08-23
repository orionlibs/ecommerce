package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.BoostField;
import de.hybris.platform.solrfacetsearch.search.FacetSearchQueryOperatorTranslator;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.solr.IndexedPropertyTypeInfo;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexedPropertyTypeRegistry;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrValueFormatUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractFacetSearchQueryPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    private SearchQuery.Operator defaultOperator = SearchQuery.Operator.OR;
    private FieldNameTranslator fieldNameTranslator;
    private FacetSearchQueryOperatorTranslator facetSearchQueryOperatorTranslator;
    private SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry;


    protected String convertQueryField(SearchQuery searchQuery, QueryField queryField)
    {
        String convertedField = this.fieldNameTranslator.translate(searchQuery, queryField.getField(), FieldNameProvider.FieldType.INDEX);
        StringBuilder query = new StringBuilder();
        query.append(ClientUtils.escapeQueryChars(convertedField));
        query.append(':');
        if(queryField.getValues().size() == 1)
        {
            String value = queryField.getValues().iterator().next();
            String escapedValue = formatAndEscapeValue(searchQuery.getIndexedType(), queryField.getField(), value);
            String convertedValue = this.facetSearchQueryOperatorTranslator.translate(escapedValue, queryField.getQueryOperator());
            query.append(convertedValue);
        }
        else
        {
            List<String> convertedValues = new ArrayList<>(queryField.getValues().size());
            for(String value : queryField.getValues())
            {
                String escapedValue = formatAndEscapeValue(searchQuery.getIndexedType(), queryField.getField(), value);
                String str1 = this.facetSearchQueryOperatorTranslator.translate(escapedValue, queryField
                                .getQueryOperator());
                convertedValues.add(str1);
            }
            SearchQuery.Operator operator = resolveQueryFieldOperator(searchQuery, queryField);
            String separator = " " + operator + " ";
            String convertedValue = StringUtils.join(convertedValues, separator);
            query.append('(');
            query.append(convertedValue);
            query.append(')');
        }
        return query.toString();
    }


    protected String convertBoostField(SearchQuery searchQuery, BoostField boostField)
    {
        String convertedField = this.fieldNameTranslator.translate(searchQuery, boostField.getField(), FieldNameProvider.FieldType.INDEX);
        StringBuilder query = new StringBuilder();
        query.append(ClientUtils.escapeQueryChars(convertedField));
        query.append(':');
        String value = String.valueOf(boostField.getValue());
        String escapedValue = formatAndEscapeValue(searchQuery.getIndexedType(), boostField.getField(), value);
        String convertedValue = this.facetSearchQueryOperatorTranslator.translate(escapedValue, boostField.getQueryOperator());
        query.append(convertedValue);
        query.append("^=");
        query.append(boostField.getBoostValue());
        return query.toString();
    }


    protected String convertRawQuery(SearchQuery searchQuery, RawQuery rawQuery)
    {
        StringBuilder query = new StringBuilder();
        query.append('(');
        if(rawQuery.getField() != null)
        {
            String convertedField = this.fieldNameTranslator.translate(searchQuery, rawQuery.getField(), FieldNameProvider.FieldType.INDEX);
            query.append(convertedField);
            query.append(":(");
        }
        query.append(rawQuery.getQuery());
        if(rawQuery.getField() != null)
        {
            query.append(')');
        }
        query.append(')');
        return query.toString();
    }


    protected SearchQuery.Operator resolveQueryFieldOperator(SearchQuery searchQuery, QueryField queryField)
    {
        if(queryField.getOperator() != null)
        {
            return queryField.getOperator();
        }
        return resolveOperator(searchQuery);
    }


    protected SearchQuery.Operator resolveOperator(SearchQuery searchQuery)
    {
        if(searchQuery.getDefaultOperator() != null)
        {
            return searchQuery.getDefaultOperator();
        }
        return getDefaultOperator();
    }


    protected String formatAndEscapeValue(IndexedType indexedType, String field, String value)
    {
        IndexedProperty indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(field);
        if(indexedProperty != null && MapUtils.isEmpty(indexedProperty.getValueRangeSets()))
        {
            IndexedPropertyTypeInfo indexedPropertyTypeInfo = this.solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(indexedProperty.getType());
            if(indexedPropertyTypeInfo != null)
            {
                String formattedValue = SolrValueFormatUtils.format(value, indexedPropertyTypeInfo.getJavaType());
                return escapeValue(formattedValue);
            }
        }
        return ClientUtils.escapeQueryChars(value);
    }


    protected String escapeValue(String value)
    {
        return ("AND".equals(value) || "OR".equals(value) || "NOT".equals(value)) ? ("\\" + value) :
                        ClientUtils.escapeQueryChars(value);
    }


    public SearchQuery.Operator getDefaultOperator()
    {
        return this.defaultOperator;
    }


    @Required
    public void setDefaultOperator(SearchQuery.Operator defaultOperator)
    {
        this.defaultOperator = defaultOperator;
    }


    public FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    public FacetSearchQueryOperatorTranslator getFacetSearchQueryOperatorTranslator()
    {
        return this.facetSearchQueryOperatorTranslator;
    }


    @Required
    public void setFacetSearchQueryOperatorTranslator(FacetSearchQueryOperatorTranslator facetSearchQueryOperatorTranslator)
    {
        this.facetSearchQueryOperatorTranslator = facetSearchQueryOperatorTranslator;
    }


    public SolrIndexedPropertyTypeRegistry getSolrIndexedPropertyTypeRegistry()
    {
        return this.solrIndexedPropertyTypeRegistry;
    }


    @Required
    public void setSolrIndexedPropertyTypeRegistry(SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry)
    {
        this.solrIndexedPropertyTypeRegistry = solrIndexedPropertyTypeRegistry;
    }
}
