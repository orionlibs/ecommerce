package de.hybris.platform.solrfacetsearch.reporting.impl;

import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import de.hybris.platform.solrfacetsearch.reporting.ReportingRuntimeException;
import de.hybris.platform.solrfacetsearch.reporting.SolrQueryStatisticsAggregator;
import de.hybris.platform.solrfacetsearch.reporting.StatisticsCollector;
import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;

public class Log4jSolrQueryStatisticsAggregator implements SolrQueryStatisticsAggregator
{
    private static final Logger LOG = Logger.getLogger(Log4jSolrQueryStatisticsAggregator.class);
    private String statisticFilesFolder;
    private String filePrefix;
    private SimpleDateFormat formatter;
    private StatisticsCollector statisticsCollector;


    public List<AggregatedSearchQueryInfo> aggregate()
    {
        File[] files = getLogFilesToProcess();
        for(File file : files)
        {
            try
            {
                List<SearchQueryInfo> r = processFile(file);
                tryToDeleteFile(file);
                for(SearchQueryInfo res : r)
                {
                    this.statisticsCollector.addStatistic(res);
                }
            }
            catch(IOException e)
            {
                throw new ReportingRuntimeException(e);
            }
        }
        List<AggregatedSearchQueryInfo> aggregatedStatistics = this.statisticsCollector.getAggregatedStatistics();
        this.statisticsCollector.clear();
        return aggregatedStatistics;
    }


    private void tryToDeleteFile(File file)
    {
        try
        {
            Files.delete(file.toPath());
        }
        catch(IOException e)
        {
            LOG.warn("Could not delete " + file, e);
        }
    }


    protected File[] getLogFilesToProcess()
    {
        File[] files = (new File(this.statisticFilesFolder)).listFiles((FilenameFilter)new Object(this));
        return (files != null) ? files : new File[0];
    }


    protected List<SearchQueryInfo> processFile(File file) throws IOException
    {
        List<SearchQueryInfo> r = (List<SearchQueryInfo>)Files.readLines(file, Charset.defaultCharset(), (LineProcessor)new LogEntryProcessor(this));
        return r;
    }


    public void setStatisticFilesFolder(String statisticFilesFolder)
    {
        this.statisticFilesFolder = statisticFilesFolder;
    }


    public void setFilePrefix(String filePrefix)
    {
        this.filePrefix = filePrefix;
    }


    public void setFormatter(SimpleDateFormat formatter)
    {
        this.formatter = formatter;
    }


    public void setStatisticsCollector(StatisticsCollector statisticsCollector)
    {
        this.statisticsCollector = statisticsCollector;
    }
}
