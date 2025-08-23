package de.hybris.platform.servicelayer.search.preprocessor.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;

public class DisableCachingQueryPreprocessor implements QueryPreprocessor
{
    private SessionService sessionService;


    public void process(FlexibleSearchQuery query)
    {
        if(query.isDisableCaching())
        {
            this.sessionService.setAttribute("disableCache", Boolean.valueOf(query.isDisableCaching()));
        }
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
