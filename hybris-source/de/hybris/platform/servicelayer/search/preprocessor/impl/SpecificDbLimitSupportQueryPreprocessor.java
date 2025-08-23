package de.hybris.platform.servicelayer.search.preprocessor.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;

public class SpecificDbLimitSupportQueryPreprocessor implements QueryPreprocessor
{
    private SessionService sessionService;


    public void process(FlexibleSearchQuery query)
    {
        if(query.isDisableSpecificDbLimitSupport() != null)
        {
            this.sessionService.setAttribute("flexible.search.disable.specific.db.limit.support", query
                            .isDisableSpecificDbLimitSupport());
        }
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
