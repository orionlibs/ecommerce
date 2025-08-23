package de.hybris.platform.personalizationservices.voters.impl;

import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.voters.Vote;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;

public class NewSessionVoter extends AbstractVoter
{
    public static final int PRECEDENCE = 9;
    private CxConfigurationService cxConfigurationService;


    public NewSessionVoter()
    {
        super(9);
    }


    public Vote getVote(HttpServletRequest request, HttpServletResponse response)
    {
        Vote result = getDefaultVote();
        if(request.getSession().isNew())
        {
            result.setRecalculateActions(getActionsForNewSession());
        }
        return result;
    }


    protected Set<RecalculateAction> getActionsForNewSession()
    {
        return this.cxConfigurationService.getDefaultActionsForAnonymous();
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }
}
