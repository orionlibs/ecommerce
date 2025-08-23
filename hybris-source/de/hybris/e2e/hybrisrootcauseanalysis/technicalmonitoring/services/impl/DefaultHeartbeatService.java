package de.hybris.e2e.hybrisrootcauseanalysis.technicalmonitoring.services.impl;

import de.hybris.e2e.hybrisrootcauseanalysis.technicalmonitoring.services.HeartbeatService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class DefaultHeartbeatService implements HeartbeatService
{
    private static final Logger LOG = Logger.getLogger(DefaultHeartbeatService.class);
    @Resource
    private FlexibleSearchService flexibleSearchService;
    private static final String FLEX_QUERY1 = "SELECT * FROM {Language} WHERE {isocode} = 'en'";
    private static final String FLEX_QUERY2 = "SELECT * FROM {User} WHERE {uid} = 'admin'";


    public boolean isFlexibleSearchOk()
    {
        if(this.flexibleSearchService == null)
        {
            LOG.error("Flexible search service is NULL");
            return false;
        }
        SearchResult<LanguageModel> queryResult = this.flexibleSearchService.search("SELECT * FROM {Language} WHERE {isocode} = 'en'");
        if(queryResult.getResult().size() != 1)
        {
            LOG.error("Flexible search 'en' language count != 1");
            return false;
        }
        queryResult = this.flexibleSearchService.search("SELECT * FROM {User} WHERE {uid} = 'admin'");
        if(queryResult.getResult().size() != 1)
        {
            LOG.error("Flexible search 'admin' user count != 1");
            return false;
        }
        return true;
    }
}
