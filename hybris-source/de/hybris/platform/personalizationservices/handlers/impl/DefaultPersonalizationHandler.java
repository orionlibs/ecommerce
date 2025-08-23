package de.hybris.platform.personalizationservices.handlers.impl;

import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.handlers.PersonalizationHandler;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.personalizationservices.voters.Voter;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPersonalizationHandler implements PersonalizationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPersonalizationHandler.class);
    private List<Voter> voters = Collections.emptyList();
    private SessionService sessionService;
    private CxRecalculationService cxRecalculationService;
    private CxConsentService cxConsentService;


    public void handlePersonalization(HttpServletRequest request, HttpServletResponse response)
    {
        activatePersonalization();
        executeVote(request, response);
    }


    protected void activatePersonalization()
    {
        this.sessionService.setAttribute("ACTIVE_PERSONALIZATION", Boolean.TRUE);
    }


    protected void executeVote(HttpServletRequest request, HttpServletResponse response)
    {
        Vote finalVote = getDefaultVote();
        for(Voter voter : this.voters)
        {
            try
            {
                Vote vote = voter.getVote(request, response);
                if(vote.isConclusive())
                {
                    finalVote = vote;
                    break;
                }
                finalVote = getCombinedVote(finalVote, vote);
            }
            catch(RuntimeException e)
            {
                LOG.warn("Voter {} failed", voter.getClass().getSimpleName(), e);
            }
        }
        try
        {
            executeFinalVote(finalVote);
        }
        catch(RuntimeException e)
        {
            LOG.warn("Failed during experience recalculation", e);
        }
    }


    protected Vote getCombinedVote(Vote finalVote, Vote vote)
    {
        finalVote.getRecalculateActions().addAll(vote.getRecalculateActions());
        return finalVote;
    }


    protected Vote getDefaultVote()
    {
        Vote result = new Vote();
        result.setRecalculateActions(new HashSet());
        return result;
    }


    protected void executeFinalVote(Vote finalVote)
    {
        if(ignoreRecalculation(finalVote))
        {
            return;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Final vote: {}", finalVote.getRecalculateActions());
        }
        this.cxRecalculationService.recalculate(createRecalculateActionList(filterActions(finalVote)));
    }


    protected boolean ignoreRecalculation(Vote finalVote)
    {
        return (finalVote == null || CollectionUtils.isEmpty(finalVote.getRecalculateActions()) || finalVote
                        .getRecalculateActions().contains(RecalculateAction.IGNORE));
    }


    protected List<RecalculateAction> createRecalculateActionList(Set<RecalculateAction> recalculateActionSet)
    {
        if(CollectionUtils.isEmpty(recalculateActionSet))
        {
            return Collections.emptyList();
        }
        List<RecalculateAction> actionList = new ArrayList<>(recalculateActionSet);
        if(actionList.contains(RecalculateAction.LOAD) && actionList.contains(RecalculateAction.RECALCULATE))
        {
            actionList.remove(RecalculateAction.LOAD);
        }
        return actionList;
    }


    protected Set<RecalculateAction> filterActions(Vote finalVote)
    {
        Set<RecalculateAction> actions = finalVote.getRecalculateActions();
        if(actions.isEmpty())
        {
            return actions;
        }
        if(!getCxConsentService().userHasActiveConsent())
        {
            return (Set<RecalculateAction>)actions.stream().filter(this::actionWithoutConsent).collect(Collectors.toSet());
        }
        return actions;
    }


    protected boolean actionWithoutConsent(RecalculateAction action)
    {
        return (
                        !RecalculateAction.ASYNC_PROCESS.equals(action) &&
                                        !RecalculateAction.UPDATE.equals(action) &&
                                        !action.name().startsWith(RecalculateAction.ASYNC_PROCESS.name()) &&
                                        !action.name().startsWith(RecalculateAction.UPDATE.name()));
    }


    @Autowired(required = false)
    public void setVoters(List<Voter> voters)
    {
        this.voters = CollectionUtils.isNotEmpty(voters) ? voters : Collections.<Voter>emptyList();
    }


    protected List<Voter> getVoters()
    {
        return this.voters;
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


    @Required
    public void setCxRecalculationService(CxRecalculationService cxRecalculationService)
    {
        this.cxRecalculationService = cxRecalculationService;
    }


    protected CxRecalculationService getCxRecalculationService()
    {
        return this.cxRecalculationService;
    }


    @Required
    public void setCxConsentService(CxConsentService cxConsentService)
    {
        this.cxConsentService = cxConsentService;
    }


    protected CxConsentService getCxConsentService()
    {
        return this.cxConsentService;
    }
}
