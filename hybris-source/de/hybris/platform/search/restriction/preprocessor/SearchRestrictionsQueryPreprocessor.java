package de.hybris.platform.search.restriction.preprocessor;

import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class SearchRestrictionsQueryPreprocessor implements QueryPreprocessor
{
    private static final Logger LOG = Logger.getLogger(SearchRestrictionsQueryPreprocessor.class);
    private SearchRestrictionService searchRestrictionService;


    public void process(FlexibleSearchQuery query)
    {
        if(query.isDisableSearchRestrictions())
        {
            this.searchRestrictionService.disableSearchRestrictions();
            if(query.getSessionSearchRestrictions() != null && !query.getSessionSearchRestrictions().isEmpty())
            {
                LOG.warn("FlexibleSearchQuery object contains session search restrictions but disableSearchRestrictions flag is set, any restrictions will be ignored!");
            }
        }
        if(query.getSessionSearchRestrictions() != null && !query.getSessionSearchRestrictions().isEmpty())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Storing session search restrictions from query object: " + query.getSessionSearchRestrictions() + " into user session.");
            }
            this.searchRestrictionService.addSessionSearchRestrictions(query.getSessionSearchRestrictions());
        }
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }
}
