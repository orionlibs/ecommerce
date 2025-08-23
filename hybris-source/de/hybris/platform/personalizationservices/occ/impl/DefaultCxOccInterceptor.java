package de.hybris.platform.personalizationservices.occ.impl;

import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.servicelayer.user.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxOccInterceptor extends AbstractCxOccInterceptor
{
    public static final int PRECEDENCE = 10;
    private CxConsentService cxConsentService;
    private UserService userService;
    private CxOccAttributesStrategy cxOccAttributesStrategy;


    public DefaultCxOccInterceptor()
    {
        super(10);
    }


    @Deprecated(since = "1905", forRemoval = true)
    public boolean shouldPersonalizeRequest(HttpServletRequest request)
    {
        return shouldPersonalizeRequestVote(request).isVote();
    }


    public CxOccInterceptorVote shouldPersonalizeRequestVote(HttpServletRequest request)
    {
        boolean vote = (this.cxOccAttributesStrategy.readPersonalizationId(request).isPresent() && this.cxConsentService.userHasActiveConsent(this.userService.getCurrentUser()));
        CxOccInterceptorVote result = new CxOccInterceptorVote();
        result.setVote(vote);
        result.setConclusive(false);
        return result;
    }


    public void afterVoteExecution(HttpServletRequest request, HttpServletResponse response, Vote vote)
    {
        boolean isRecalculated = vote.getRecalculateActions().stream().anyMatch(this::isRecalculationAction);
        if(isRecalculated)
        {
            this.cxOccAttributesStrategy.setPersonalizationCalculationTime(request, response);
        }
    }


    protected boolean isRecalculationAction(RecalculateAction a)
    {
        return (RecalculateAction.ASYNC_PROCESS == a || RecalculateAction.RECALCULATE == a || (a != null && a
                        .name().startsWith(RecalculateAction.ASYNC_PROCESS.name())));
    }


    protected CxConsentService getCxConsentService()
    {
        return this.cxConsentService;
    }


    @Required
    public void setCxConsentService(CxConsentService cxConsentService)
    {
        this.cxConsentService = cxConsentService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CxOccAttributesStrategy getCxOccAttributesStrategy()
    {
        return this.cxOccAttributesStrategy;
    }


    @Required
    public void setCxOccAttributesStrategy(CxOccAttributesStrategy cxOccAttributesStrategy)
    {
        this.cxOccAttributesStrategy = cxOccAttributesStrategy;
    }
}
