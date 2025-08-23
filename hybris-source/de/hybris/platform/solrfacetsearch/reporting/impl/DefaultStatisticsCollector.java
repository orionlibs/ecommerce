package de.hybris.platform.solrfacetsearch.reporting.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.solrfacetsearch.reporting.StatisticsCollector;
import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultStatisticsCollector implements StatisticsCollector
{
    private final Map<String, Map<String, Map<String, Map<String, AggregatedSearchQueryInfo>>>> dateIndexMap = Maps.newHashMap();
    private SimpleDateFormat formatter;


    public void addStatistic(SearchQueryInfo searchResult)
    {
        String formattedDate = this.formatter.format(searchResult.date);
        if(this.dateIndexMap.containsKey(formattedDate))
        {
            updateDateIndexEntry(searchResult, formattedDate);
        }
        else
        {
            createDateIndexEntry(searchResult, formattedDate);
        }
    }


    protected void updateDateIndexEntry(SearchQueryInfo searchResult, String formattedDate)
    {
        Map<String, Map<String, Map<String, AggregatedSearchQueryInfo>>> indexLanguageMap = this.dateIndexMap.get(formattedDate);
        if(indexLanguageMap.containsKey(searchResult.indexConfiguration))
        {
            Map<String, Map<String, AggregatedSearchQueryInfo>> languageQueryMap = indexLanguageMap.get(searchResult.indexConfiguration);
            if(languageQueryMap.containsKey(searchResult.language))
            {
                Map<String, AggregatedSearchQueryInfo> queryReportingMap = languageQueryMap.get(searchResult.language);
                if(queryReportingMap.containsKey(searchResult.query))
                {
                    ((AggregatedSearchQueryInfo)queryReportingMap.get(searchResult.query)).addNumberOfResults(searchResult.count);
                }
                else
                {
                    queryReportingMap.put(searchResult.query, new AggregatedSearchQueryInfo(searchResult.indexConfiguration, searchResult.query, searchResult.language, searchResult.count, searchResult.date));
                }
            }
            else
            {
                languageQueryMap.put(searchResult.language, Maps.newHashMap());
                addStatistic(searchResult);
            }
        }
        else
        {
            indexLanguageMap.put(searchResult.indexConfiguration,
                            Maps.newHashMap());
            addStatistic(searchResult);
        }
    }


    protected void createDateIndexEntry(SearchQueryInfo searchResult, String formattedDate)
    {
        this.dateIndexMap.put(formattedDate, Maps.newHashMap());
        addStatistic(searchResult);
    }


    public List<AggregatedSearchQueryInfo> getAggregatedStatistics()
    {
        List<AggregatedSearchQueryInfo> results = new ArrayList<>();
        for(Map<String, Map<String, Map<String, AggregatedSearchQueryInfo>>> m : this.dateIndexMap.values())
        {
            for(Map<String, Map<String, AggregatedSearchQueryInfo>> m2 : m.values())
            {
                for(Map<String, AggregatedSearchQueryInfo> m3 : m2.values())
                {
                    results.addAll(m3.values());
                }
            }
        }
        return results;
    }


    public void clear()
    {
        this.dateIndexMap.clear();
    }


    public void setFormatter(SimpleDateFormat formatter)
    {
        this.formatter = formatter;
    }
}
