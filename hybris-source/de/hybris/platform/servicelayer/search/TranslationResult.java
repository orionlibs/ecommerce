package de.hybris.platform.servicelayer.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranslationResult
{
    private final String SQLQuery;
    private final List<Object> SQLQueryParameters;


    public TranslationResult(String sqlQuery, List<Object> valuesForPreparedStatement)
    {
        this.SQLQuery = sqlQuery;
        this.SQLQueryParameters = Collections.unmodifiableList(new ArrayList(valuesForPreparedStatement));
    }


    public List<Object> getSQLQueryParameters()
    {
        return this.SQLQueryParameters;
    }


    public String getSQLQuery()
    {
        return this.SQLQuery;
    }
}
