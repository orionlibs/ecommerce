package com.hybris.backoffice.solrsearch.populators;

import com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQueryFilterQueriesPopulator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeFacetSearchQueryFilterQueriesPopulator extends FacetSearchQueryFilterQueriesPopulator
{
    public static final String QUOTE = "\"";
    public static final String WILDCARD_ANY_STRING = "*";
    public static final String FQ_VALUE_GROUP_PREFIX = "(";
    public static final String FQ_VALUE_GROUP_SUFFIX = ")";
    public static final String FQ_FIELD_VALUE_SEPARATOR = ":";
    public static final String FQ_CONDITION_NOT = "!";
    public static final String FQ_CONDITION_GREATER = " TO *";
    public static final String FQ_CONDITION_LESS = "* TO ";
    public static final String FQ_CONDITION_AND = " AND ";
    public static final String FQ_CONDITION_BETWEEN_INCLUSIVE_PREFIX = "[";
    public static final String FQ_CONDITION_BETWEEN_INCLUSIVE_SUFFIX = "]";
    public static final String FQ_CONDITION_BETWEEN_EXCLUSIVE_PREFIX = "{";
    public static final String FQ_CONDITION_BETWEEN_EXCLUSIVE_SUFFIX = "}";
    public static final String FQ_DELIMITER = ",";
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeFacetSearchQueryFilterQueriesPopulator.class);
    private FieldNamePostProcessor fieldNamePostProcessor;
    private Map<String, Function<Serializable, String>> conditionValueConverterMap;


    protected void addRawQueries(SearchQuery searchQuery, List<String> queries)
    {
        super.addRawQueries(searchQuery, queries);
        if(searchQuery instanceof BackofficeSearchQuery)
        {
            queries.addAll(buildFilterQueries(searchQuery, ((BackofficeSearchQuery)searchQuery).getFilterQueryConditions()));
        }
    }


    protected List<String> buildFilterQueries(SearchQuery searchQuery, List<SolrSearchCondition> conditions)
    {
        return (List<String>)conditions.stream().map(condition -> convertSearchConditionToFilterQuery(searchQuery, condition))
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }


    protected String convertSearchConditionToFilterQuery(SearchQuery searchQuery, SolrSearchCondition condition)
    {
        String fieldName = convertAttributeNameToFieldName(searchQuery, condition);
        String flatFQValue = convertSearchConditionValuesToFilterQueryValue(condition);
        return fieldName.concat(":").concat(flatFQValue);
    }


    protected String convertAttributeNameToFieldName(SearchQuery searchQuery, SolrSearchCondition condition)
    {
        String translatedFieldName = getFieldNameTranslator().translate(searchQuery, condition.getAttributeName(), FieldNameProvider.FieldType.INDEX);
        String resultName = getFieldNamePostProcessor().process(searchQuery, condition.getLanguage(), translatedFieldName);
        List<SolrSearchCondition.ConditionValue> conditionValues = condition.getConditionValues();
        if(!condition.isMultiValue() && !conditionValues.isEmpty() && ((SolrSearchCondition.ConditionValue)conditionValues
                        .iterator().next()).getComparisonOperator() == ValueComparisonOperator.IS_EMPTY)
        {
            return "-" + resultName;
        }
        return resultName;
    }


    protected String convertSearchConditionValuesToFilterQueryValue(SolrSearchCondition condition)
    {
        return condition.getConditionValues().stream().map(this::convertConditionValueToString).filter(StringUtils::isNotEmpty)
                        .collect(Collectors.joining(condition.getOperator().getName(), "(", ")"));
    }


    protected String convertConditionValueToString(SolrSearchCondition.ConditionValue conditionValue)
    {
        Serializable value = conditionValue.getValue();
        if(conditionValue.getComparisonOperator().isUnary())
        {
            switch(null.$SwitchMap$com$hybris$cockpitng$search$data$ValueComparisonOperator[conditionValue.getComparisonOperator().ordinal()])
            {
                case 1:
                case 2:
                    return "[\"\" TO *]";
            }
            LOG.warn("Could not find relevant translation for operator: {}", conditionValue.getComparisonOperator());
        }
        if(value == null)
        {
            return "";
        }
        String convertedValue = getConditionValueConverter(value.getClass().getName()).apply(value);
        switch(null.$SwitchMap$com$hybris$cockpitng$search$data$ValueComparisonOperator[conditionValue.getComparisonOperator().ordinal()])
        {
            case 3:
                return encloseString(convertedValue, "\"");
            case 4:
                return Arrays.<String>stream(convertedValue.split(" ")).map(token -> encloseString(token, "*"))
                                .collect(Collectors.joining(" AND "));
            case 5:
                return "{" + convertedValue + " TO *]";
            case 6:
                return "[" + convertedValue + " TO *]";
            case 7:
                return "[* TO " + convertedValue + "}";
            case 8:
                return "[* TO " + convertedValue + "]";
            case 9:
                return Arrays.<String>stream(convertCollectionValue(value, convertedValue).split(",")).map(token -> encloseString(token, "*"))
                                .collect(Collectors.joining(" "));
            case 10:
                return Arrays.<String>stream(convertCollectionValue(value, convertedValue).split(",")).map(token -> "!" + encloseString(token, "*"))
                                .collect(Collectors.joining(" "));
        }
        return convertedValue;
    }


    protected String encloseString(String value, String enclosingString)
    {
        Validate.notNull("enclosingString cannot be null", new Object[] {enclosingString});
        if(enclosingString.equals("\""))
        {
            value = value.replace("\\", "\\\\");
        }
        return enclosingString.concat(value).concat(enclosingString);
    }


    protected String convertCollectionValue(Serializable value, String convertedValue)
    {
        if(value instanceof java.util.Collection)
        {
            convertedValue = convertedValue.replace("[", "").replace("]", "").replace(" ", "");
        }
        return convertedValue;
    }


    protected Function<Serializable, String> getConditionValueConverter(String type)
    {
        return getConditionValueConverterMap().getOrDefault(type, serializable -> Objects.toString(serializable, ""));
    }


    protected FieldNamePostProcessor getFieldNamePostProcessor()
    {
        return this.fieldNamePostProcessor;
    }


    @Required
    public void setFieldNamePostProcessor(FieldNamePostProcessor fieldNamePostProcessor)
    {
        this.fieldNamePostProcessor = fieldNamePostProcessor;
    }


    protected Map<String, Function<Serializable, String>> getConditionValueConverterMap()
    {
        return this.conditionValueConverterMap;
    }


    @Required
    public void setConditionValueConverterMap(Map<String, Function<Serializable, String>> conditionValueConverterMap)
    {
        this.conditionValueConverterMap = conditionValueConverterMap;
    }
}
