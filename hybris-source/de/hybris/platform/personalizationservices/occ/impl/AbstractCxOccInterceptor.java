package de.hybris.platform.personalizationservices.occ.impl;

import de.hybris.platform.personalizationservices.occ.CxOccInterceptor;
import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractCxOccInterceptor implements CxOccInterceptor
{
    private int order;


    public AbstractCxOccInterceptor(int order)
    {
        this.order = order;
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public CxOccInterceptorVote getDefaultVote(HttpServletRequest request)
    {
        return shouldPersonalizeRequestVote(request);
    }
}
