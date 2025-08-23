package com.hybris.backoffice.solrsearch.dataaccess;

import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Locale;
import java.util.Set;

public class BackofficeQueryField extends QueryField
{
    private final Locale locale;


    public BackofficeQueryField(String field, Locale locale, String... values)
    {
        super(field, values);
        this.locale = locale;
    }


    public BackofficeQueryField(String field, SearchQuery.Operator operator, Locale locale, String... values)
    {
        super(field, operator, values);
        this.locale = locale;
    }


    public BackofficeQueryField(String field, SearchQuery.Operator operator, Locale locale, Set<String> values)
    {
        super(field, operator, values);
        this.locale = locale;
    }


    public BackofficeQueryField(String field, SearchQuery.Operator operator, SearchQuery.QueryOperator queryOperator, Locale locale, String... values)
    {
        super(field, operator, queryOperator, values);
        this.locale = locale;
    }


    public BackofficeQueryField(String field, SearchQuery.Operator operator, SearchQuery.QueryOperator queryOperator, Locale locale, Set<String> values)
    {
        super(field, operator, queryOperator, values);
        this.locale = locale;
    }


    public Locale getLocale()
    {
        return this.locale;
    }
}
