package de.hybris.platform.personalizationcms.voters.impl;

import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.occ.voters.CxOccVoter;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.personalizationservices.voters.impl.AbstractVoter;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;

public class PreviewVoter extends AbstractVoter implements CxOccVoter
{
    public static final int PREVIEW_PRECEDENCE = 1;
    private SessionService sessionService;


    public PreviewVoter()
    {
        super(1);
    }


    public Vote getVote(HttpServletRequest request, HttpServletResponse response)
    {
        Vote result = getDefaultVote();
        if(isEnabled())
        {
            result.setConclusive(true);
            result.setRecalculateActions(Collections.singleton(RecalculateAction.RECALCULATE));
        }
        return result;
    }


    protected boolean isEnabled()
    {
        return (getSessionService().getAttribute("cmsTicketId") != null);
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    public Vote getVote(HttpServletRequest request)
    {
        return getVote(request, null);
    }
}
