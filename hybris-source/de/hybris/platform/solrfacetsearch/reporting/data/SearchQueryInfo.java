package de.hybris.platform.solrfacetsearch.reporting.data;

import java.util.Date;

public class SearchQueryInfo
{
    public final String query;
    public final long count;
    public final String indexConfiguration;
    public final String language;
    public final Date date;


    public SearchQueryInfo(String query, long count, String indexConfiguration, String language, Date date)
    {
        this.query = query;
        this.count = count;
        this.indexConfiguration = indexConfiguration;
        this.language = language;
        this.date = date;
    }
}
