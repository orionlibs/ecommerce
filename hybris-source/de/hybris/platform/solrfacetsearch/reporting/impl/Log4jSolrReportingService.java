package de.hybris.platform.solrfacetsearch.reporting.impl;

import de.hybris.platform.solrfacetsearch.reporting.SolrReportingService;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class Log4jSolrReportingService implements SolrReportingService
{
    private static final Logger LOG = Logger.getLogger("solrStatisticLogger");
    private SimpleDateFormat formatter;


    public void saveQueryResult(SearchQueryInfo reportingQueryResult)
    {
        LOG.info(StringUtils.join(getArray(reportingQueryResult), '|'));
    }


    protected Object[] getArray(SearchQueryInfo reportingQueryResult)
    {
        return new Object[] {this.formatter
                        .format(reportingQueryResult.date), reportingQueryResult.query, reportingQueryResult.indexConfiguration, reportingQueryResult.language,
                        Long.valueOf(reportingQueryResult.count)};
    }


    @Required
    public void setFormatter(SimpleDateFormat formatter)
    {
        this.formatter = formatter;
    }
}
