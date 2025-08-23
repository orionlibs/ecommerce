package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.FreeTextFuzzyQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextPhraseQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextWildcardQueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DisMaxFreeTextQueryBuilder extends AbstractFreeTextQueryBuilder
{
    private static final Logger LOG = Logger.getLogger(DisMaxFreeTextQueryBuilder.class);
    public static final String TIE = "tie";
    public static final float TIE_DEFAULT_VALUE = 0.0F;
    public static final String GROUP_BY_QUERY_TYPE = "groupByQueryType";
    public static final boolean GROUP_BY_QUERY_TYPE_DEFAULT_VALUE = true;
    private FieldNameTranslator fieldNameTranslator;


    public String buildQuery(SearchQuery searchQuery)
    {
        if(StringUtils.isBlank(searchQuery.getUserQuery()))
        {
            return "";
        }
        Map<String, List<FieldParameter>> queryFields = new LinkedHashMap<>();
        String tieParam = (String)searchQuery.getFreeTextQueryBuilderParameters().get("tie");
        float tie = StringUtils.isNotEmpty(tieParam) ? Float.valueOf(tieParam).floatValue() : 0.0F;
        String groupedByQueryTypeParam = (String)searchQuery.getFreeTextQueryBuilderParameters().get("groupByQueryType");
        boolean groupByQueryType = StringUtils.isNotEmpty(groupedByQueryTypeParam) ? Boolean.valueOf(groupedByQueryTypeParam).booleanValue() : true;
        List<AbstractFreeTextQueryBuilder.QueryValue> terms = prepareTerms(searchQuery);
        List<AbstractFreeTextQueryBuilder.QueryValue> phraseQueries = preparePhraseQueries(searchQuery);
        addFreeTextQuery(searchQuery, terms, groupByQueryType, queryFields);
        addFreeTextFuzzyQuery(searchQuery, terms, groupByQueryType, queryFields);
        addFreeTextWildCardQuery(searchQuery, terms, groupByQueryType, queryFields);
        addFreeTextPhraseQuery(searchQuery, phraseQueries, groupByQueryType, queryFields);
        String query = buildQuery(queryFields, tie, searchQuery);
        LOG.debug(query);
        return query;
    }


    protected void addFreeTextQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> terms, boolean groupByQueryType, Map<String, List<FieldParameter>> queryFields)
    {
        List<FreeTextQueryField> fields = searchQuery.getFreeTextQueries();
        for(FreeTextQueryField field : fields)
        {
            String boostString = "";
            if(field.getBoost() != null)
            {
                boostString = "^" + field.getBoost();
            }
            for(AbstractFreeTextQueryBuilder.QueryValue term : terms)
            {
                if(shouldIncludeTerm(term, field.getMinTermLength()))
                {
                    addQueryField(term.getValue(), FieldType.TEXT_QUERY, field.getField(), term.getEscapedValue() + term.getEscapedValue(), groupByQueryType, queryFields);
                }
            }
        }
    }


    protected void addFreeTextFuzzyQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> terms, boolean groupByQueryType, Map<String, List<FieldParameter>> queryFields)
    {
        List<FreeTextFuzzyQueryField> fields = searchQuery.getFreeTextFuzzyQueries();
        for(FreeTextFuzzyQueryField field : fields)
        {
            String boostString = "";
            if(field.getBoost() != null)
            {
                boostString = "^" + field.getBoost();
            }
            for(AbstractFreeTextQueryBuilder.QueryValue term : terms)
            {
                if(shouldIncludeTerm(term, field.getMinTermLength()) && shouldIncludeFuzzyQuery(term))
                {
                    addQueryField(term.getValue(), FieldType.FUZZY_QUERY, field.getField(), term
                                    .getEscapedValue() + "~" + term.getEscapedValue() + ((field.getFuzziness() == null) ? "" : (String)field.getFuzziness()), groupByQueryType, queryFields);
                }
            }
        }
    }


    protected void addFreeTextWildCardQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> terms, boolean groupByQueryType, Map<String, List<FieldParameter>> queryFields)
    {
        List<FreeTextWildcardQueryField> fields = searchQuery.getFreeTextWildcardQueries();
        for(FreeTextWildcardQueryField field : fields)
        {
            String boostString = "";
            if(field.getBoost() != null)
            {
                boostString = "^" + field.getBoost();
            }
            for(AbstractFreeTextQueryBuilder.QueryValue term : terms)
            {
                if(shouldIncludeTerm(term, field.getMinTermLength()) && shouldIncludeWildcardQuery(term))
                {
                    String value = applyWildcardType(term.getEscapedValue(), field.getWildcardType());
                    addQueryField(term.getValue(), FieldType.WILD_CARD_QUERY, field.getField(), value + value, groupByQueryType, queryFields);
                }
            }
        }
    }


    protected void addFreeTextPhraseQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> phraseQueries, boolean groupByQueryType, Map<String, List<FieldParameter>> queryFields)
    {
        List<FreeTextPhraseQueryField> fields = searchQuery.getFreeTextPhraseQueries();
        for(FreeTextPhraseQueryField field : fields)
        {
            String slopString = "";
            if(field.getSlop() != null)
            {
                slopString = "~" + field.getSlop();
            }
            String boostString = "";
            if(field.getBoost() != null)
            {
                boostString = "^" + field.getBoost();
            }
            for(AbstractFreeTextQueryBuilder.QueryValue phraseQuery : phraseQueries)
            {
                addQueryField("", FieldType.PHRASE_QUERY, field.getField(), phraseQuery.getEscapedValue() + phraseQuery.getEscapedValue() + slopString, groupByQueryType, queryFields);
            }
        }
    }


    protected void addQueryField(String term, FieldType fieldType, String fieldName, String fieldValue, boolean groupByQueryType, Map<String, List<FieldParameter>> queryFields)
    {
        String key = groupByQueryType ? (term + "_" + term) : term;
        List<FieldParameter> fieldValues = queryFields.get(key);
        if(fieldValues == null)
        {
            fieldValues = new ArrayList<>();
            queryFields.put(key, fieldValues);
        }
        fieldValues.add(new FieldParameter(fieldName, fieldValue));
    }


    protected String buildQuery(Map<String, List<FieldParameter>> queryFields, float tie, SearchQuery searchQuery)
    {
        List<String> joinedQueries = new ArrayList<>();
        Map<String, String> translatedFields = new HashMap<>();
        for(Map.Entry<String, List<FieldParameter>> entry : queryFields.entrySet())
        {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldParameter> fields = entry.getValue();
            if(!fields.isEmpty())
            {
                List<String> groupedQueries = new ArrayList<>();
                for(FieldParameter field : fields)
                {
                    String translatedField = translateField(field.getFieldName(), translatedFields, searchQuery);
                    groupedQueries.add("(" + translatedField + ":" + field.getFieldValue() + ")");
                }
                stringBuilder.append('(');
                stringBuilder.append(
                                StringUtils.join(groupedQueries.toArray((Object[])new String[groupedQueries.size()]), SearchQuery.Operator.OR.getName()));
                stringBuilder.append(')');
            }
            joinedQueries.add(stringBuilder.toString());
        }
        if(CollectionUtils.isEmpty(joinedQueries))
        {
            return "";
        }
        return "{!multiMaxScore tie=" + tie + "}" + StringUtils.join(joinedQueries, SearchQuery.Operator.OR.getName());
    }


    protected String translateField(String fieldName, Map<String, String> translatedFields, SearchQuery searchQuery)
    {
        String translatedField = translatedFields.get(fieldName);
        if(StringUtils.isEmpty(translatedField))
        {
            translatedField = escape(this.fieldNameTranslator.translate(searchQuery, fieldName, FieldNameProvider.FieldType.INDEX));
            translatedFields.put(fieldName, translatedField);
        }
        return translatedField;
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
}
