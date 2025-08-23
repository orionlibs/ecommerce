package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.FreeTextFuzzyQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextPhraseQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextWildcardQueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class MultiFieldFreeTextQueryBuilder extends AbstractFreeTextQueryBuilder
{
    private static final Logger LOG = Logger.getLogger(MultiFieldFreeTextQueryBuilder.class);
    private FieldNameTranslator fieldNameTranslator;


    public String buildQuery(SearchQuery searchQuery)
    {
        if(StringUtils.isBlank(searchQuery.getUserQuery()))
        {
            return "";
        }
        Map<String, List<String>> queryFields = new LinkedHashMap<>();
        List<AbstractFreeTextQueryBuilder.QueryValue> terms = prepareTerms(searchQuery);
        List<AbstractFreeTextQueryBuilder.QueryValue> phraseQueries = preparePhraseQueries(searchQuery);
        addFreeTextQuery(searchQuery, terms, queryFields);
        addFreeTextFuzzyQuery(searchQuery, terms, queryFields);
        addFreeTextWildCardQuery(searchQuery, terms, queryFields);
        addFreeTextPhraseQuery(searchQuery, phraseQueries, queryFields);
        String query = buildQuery(searchQuery, queryFields);
        LOG.debug(query);
        return query;
    }


    protected void addFreeTextQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> terms, Map<String, List<String>> queryFields)
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
                    addQueryField(field.getField(), term.getEscapedValue() + term.getEscapedValue(), queryFields);
                }
            }
        }
    }


    protected void addFreeTextFuzzyQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> terms, Map<String, List<String>> queryFields)
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
                    addQueryField(field.getField(), term
                                    .getEscapedValue() + "~" + term.getEscapedValue() + ((field.getFuzziness() == null) ? "" : (String)field.getFuzziness()), queryFields);
                }
            }
        }
    }


    protected void addFreeTextWildCardQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> terms, Map<String, List<String>> queryFields)
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
                    addQueryField(field.getField(), value + value, queryFields);
                }
            }
        }
    }


    protected void addFreeTextPhraseQuery(SearchQuery searchQuery, List<AbstractFreeTextQueryBuilder.QueryValue> phraseQueries, Map<String, List<String>> queryFields)
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
                addQueryField(field.getField(), phraseQuery.getEscapedValue() + phraseQuery.getEscapedValue() + slopString, queryFields);
            }
        }
    }


    protected void addQueryField(String fieldName, String fieldValue, Map<String, List<String>> queryFields)
    {
        List<String> fieldValues = queryFields.get(fieldName);
        if(fieldValues == null)
        {
            fieldValues = new ArrayList<>();
            queryFields.put(fieldName, fieldValues);
        }
        fieldValues.add(fieldValue);
    }


    protected String buildQuery(SearchQuery searchQuery, Map<String, List<String>> queryFields)
    {
        List<String> joinedQueries = new ArrayList<>();
        for(Map.Entry<String, List<String>> entry : queryFields.entrySet())
        {
            String field = translateField(entry.getKey(), searchQuery);
            List<String> values = entry.getValue();
            if(values.size() == 1)
            {
                joinedQueries.add("(" + field + ":" + (String)values.iterator().next() + ")");
                continue;
            }
            joinedQueries.add("(" + field + ":(" + StringUtils.join(values, SearchQuery.Operator.OR.getName()) + "))");
        }
        return StringUtils.join(joinedQueries, SearchQuery.Operator.OR.getName());
    }


    protected String translateField(String field, SearchQuery searchQuery)
    {
        return escape(this.fieldNameTranslator.translate(searchQuery, field, FieldNameProvider.FieldType.INDEX));
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
