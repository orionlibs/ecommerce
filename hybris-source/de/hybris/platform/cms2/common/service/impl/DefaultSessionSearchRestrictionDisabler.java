package de.hybris.platform.cms2.common.service.impl;

import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSessionSearchRestrictionDisabler implements SessionSearchRestrictionsDisabler
{
    private SearchRestrictionService searchRestrictionService;
    private SessionService sessionService;


    public <T> T execute(Supplier<T> supplier)
    {
        return (T)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, supplier));
    }


    protected SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
