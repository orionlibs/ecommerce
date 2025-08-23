package de.hybris.platform.solrfacetsearch.reporting.data;

import java.util.Date;

public class AggregatedSearchQueryInfo
{
    private final String indexName;
    private final String query;
    private final String language;
    private long count;
    private double averageNumberOfResults;
    private final Date date;


    public AggregatedSearchQueryInfo(String indexName, String query, String language, long numberOfResults, Date date)
    {
        this.indexName = indexName;
        this.query = query;
        this.language = language;
        this.date = date;
        this.count = 1L;
        this.averageNumberOfResults = numberOfResults;
    }


    public void addNumberOfResults(long numberOfResults)
    {
        this.averageNumberOfResults = (this.averageNumberOfResults * this.count + numberOfResults) / (this.count + 1L);
        this.count++;
    }


    public String getIndexName()
    {
        return this.indexName;
    }


    public String getQuery()
    {
        return this.query;
    }


    public String getLanguage()
    {
        return this.language;
    }


    public long getCount()
    {
        return this.count;
    }


    public double getAverageNumberOfResults()
    {
        return this.averageNumberOfResults;
    }


    public Date getDate()
    {
        return this.date;
    }
}
