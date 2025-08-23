package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.config.WildcardType;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryBuilder;
import de.hybris.platform.solrfacetsearch.search.Keyword;
import de.hybris.platform.solrfacetsearch.search.KeywordModifier;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.util.ClientUtils;

public abstract class AbstractFreeTextQueryBuilder implements FreeTextQueryBuilder
{
    protected List<QueryValue> prepareTerms(SearchQuery searchQuery)
    {
        if(CollectionUtils.isEmpty(searchQuery.getKeywords()))
        {
            return Collections.emptyList();
        }
        List<QueryValue> queryValues = new ArrayList<>();
        for(Keyword keyword : searchQuery.getKeywords())
        {
            String escapedValue;
            Collection<KeywordModifier> modifiers = keyword.getModifiers();
            String value = keyword.getValue();
            if(modifiers.contains(KeywordModifier.EXACT_MATCH))
            {
                escapedValue = escapePhraseQuery(value);
            }
            else
            {
                escapedValue = escape(value);
            }
            queryValues.add(new QueryValue(value, escapedValue, keyword));
        }
        return queryValues;
    }


    protected List<QueryValue> preparePhraseQueries(SearchQuery searchQuery)
    {
        if(CollectionUtils.isEmpty(searchQuery.getKeywords()))
        {
            return Collections.emptyList();
        }
        StringJoiner phraseQuery = new StringJoiner(" ");
        for(Keyword keyword : searchQuery.getKeywords())
        {
            if(keyword.getModifiers().contains(KeywordModifier.EXACT_MATCH))
            {
                return Collections.emptyList();
            }
            phraseQuery.add(keyword.getValue());
        }
        String value = phraseQuery.toString();
        String escapedValue = escapePhraseQuery(value);
        return Collections.singletonList(new QueryValue(value, escapedValue));
    }


    protected String escape(String value)
    {
        if("AND".equals(value) || "OR".equals(value) || "NOT".equals(value))
        {
            return "\\" + value;
        }
        return ClientUtils.escapeQueryChars(value);
    }


    protected String escapePhraseQuery(String value)
    {
        return "\"" + escapeInnerPhraseQueryValue(value) + "\"";
    }


    protected String escapeInnerPhraseQueryValue(String value)
    {
        return value
                        .replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"))
                        .replaceAll("\"", "\\\\\"");
    }


    protected boolean shouldIncludeTerm(QueryValue term, Integer minTermLength)
    {
        return (minTermLength == null || term.getValue().length() >= minTermLength.intValue());
    }


    protected boolean shouldIncludeFuzzyQuery(QueryValue term)
    {
        Keyword keyword = term.getKeyword();
        return (keyword != null && !keyword.getModifiers().contains(KeywordModifier.EXACT_MATCH));
    }


    protected boolean shouldIncludeWildcardQuery(QueryValue term)
    {
        Keyword keyword = term.getKeyword();
        return (keyword != null && !keyword.getModifiers().contains(KeywordModifier.EXACT_MATCH));
    }


    protected String applyWildcardType(String term, WildcardType wildcardType)
    {
        String defaultValue = "*" + term + "*";
        if(wildcardType != null)
        {
            switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$WildcardType[wildcardType.ordinal()])
            {
                case 1:
                    return "*" + term;
                case 2:
                    return term + "*";
                case 3:
                    return "*" + term + "*";
            }
            return defaultValue;
        }
        return defaultValue;
    }
}
