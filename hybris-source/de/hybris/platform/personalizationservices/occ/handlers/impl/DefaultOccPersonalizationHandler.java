package de.hybris.platform.personalizationservices.occ.handlers.impl;

import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.handlers.PersonalizationHandler;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.personalizationservices.occ.CxOccInterceptor;
import de.hybris.platform.personalizationservices.occ.voters.CxOccVoter;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefaultOccPersonalizationHandler implements PersonalizationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOccPersonalizationHandler.class);
    private DefaultSessionTokenService tokenService;
    private CxConfigurationService cxConfigurationService;
    private CxRecalculationService cxRecalculationService;
    private CxOccAttributesStrategy cxOccAttributesStrategy;
    private Validator personalizationIdValidator;
    private SessionService sessionService;
    private List<CxOccVoter> voters = Collections.emptyList();
    private List<CxOccInterceptor> interceptors = Collections.emptyList();


    public void handlePersonalization(HttpServletRequest request, HttpServletResponse response)
    {
        if(isPersonalizationEnabled())
        {
            handlePersonalizationId(request, response);
            if(shouldPersonalizeRequest(request))
            {
                activatePersonalization();
                beforeGetVote(request);
                Vote vote = getVote(request);
                beforeVoteExecution(request, vote);
                executeVote(vote);
                afterVoteExecution(request, response, vote);
            }
            else
            {
                executeVote(getDefaultVote());
            }
        }
    }


    protected void activatePersonalization()
    {
        this.sessionService.setAttribute("ACTIVE_OCC_PERSONALIZATION", Boolean.TRUE);
    }


    protected boolean isPersonalizationEnabled()
    {
        return ((Boolean)this.cxConfigurationService.getConfiguration().map(CxConfigModel::getOccPersonalizationEnabled).orElse(Boolean.FALSE)).booleanValue();
    }


    protected void handlePersonalizationId(HttpServletRequest request, HttpServletResponse response)
    {
        String id;
        Optional<String> personalizationId = this.cxOccAttributesStrategy.readPersonalizationId(request).filter(this::isPersonalizationIdValid);
        if(!personalizationId.isPresent())
        {
            id = this.tokenService.getOrCreateSessionToken();
        }
        else
        {
            id = personalizationId.get();
            this.tokenService.setSessionToken(id);
        }
        this.cxOccAttributesStrategy.setPersonalizationId(id, request, response);
    }


    protected boolean isPersonalizationIdValid(String id)
    {
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(id, "personalizationId");
        this.personalizationIdValidator.validate(id, (Errors)beanPropertyBindingResult);
        if(beanPropertyBindingResult.hasErrors())
        {
            LOG.debug("Incorrect personalizationId. New one will be generated");
            return false;
        }
        return true;
    }


    protected boolean shouldPersonalizeRequest(HttpServletRequest request)
    {
        CxOccInterceptorVote finalVote = getDefaultInterceptorVote();
        for(CxOccInterceptor voter : this.interceptors)
        {
            try
            {
                CxOccInterceptorVote vote = voter.shouldPersonalizeRequestVote(request);
                if(vote.isConclusive())
                {
                    finalVote.setVote(vote.isVote());
                    break;
                }
                finalVote.setVote(getCombinedInterceptorVote(finalVote, vote));
            }
            catch(RuntimeException e)
            {
                LOG.warn("Interceptor Vote {} failed", voter.getClass().getSimpleName(), e);
            }
        }
        return finalVote.isVote();
    }


    protected CxOccInterceptorVote getDefaultInterceptorVote()
    {
        CxOccInterceptorVote result = new CxOccInterceptorVote();
        result.setVote(true);
        return result;
    }


    protected void beforeGetVote(HttpServletRequest request)
    {
        this.interceptors.forEach(i -> i.beforeGetVote(request));
    }


    protected Vote getVote(HttpServletRequest request)
    {
        Vote finalVote = getDefaultVote();
        for(CxOccVoter voter : this.voters)
        {
            try
            {
                Vote vote = voter.getVote(request);
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
        return finalVote;
    }


    protected Vote getCombinedVote(Vote finalVote, Vote vote)
    {
        finalVote.getRecalculateActions().addAll(vote.getRecalculateActions());
        return finalVote;
    }


    protected boolean getCombinedInterceptorVote(CxOccInterceptorVote finalVote, CxOccInterceptorVote vote)
    {
        return (finalVote.isVote() && vote.isVote());
    }


    protected Vote getDefaultVote()
    {
        Vote result = new Vote();
        result.setRecalculateActions(new HashSet());
        result.getRecalculateActions().add(RecalculateAction.LOAD);
        return result;
    }


    protected void beforeVoteExecution(HttpServletRequest request, Vote vote)
    {
        this.interceptors.forEach(i -> i.beforeVoteExecution(request, vote));
    }


    protected void executeVote(Vote finalVote)
    {
        try
        {
            if(ignoreRecalculation(finalVote))
            {
                return;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Final vote: {}", finalVote.getRecalculateActions());
            }
            this.cxRecalculationService.recalculate(createRecalculateActionList(finalVote.getRecalculateActions()));
        }
        catch(RuntimeException e)
        {
            LOG.warn("Failed during experience recalculation", e);
        }
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
        return actionList;
    }


    protected void afterVoteExecution(HttpServletRequest request, HttpServletResponse response, Vote vote)
    {
        this.interceptors.forEach(i -> i.afterVoteExecution(request, response, vote));
    }


    @Autowired(required = false)
    public void setVoters(List<CxOccVoter> voters)
    {
        this.voters = CollectionUtils.isNotEmpty(voters) ? voters : Collections.<CxOccVoter>emptyList();
    }


    protected List<CxOccVoter> getVoters()
    {
        return this.voters;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
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


    protected List<CxOccInterceptor> getInterceptors()
    {
        return this.interceptors;
    }


    @Autowired(required = false)
    public void setInterceptors(List<CxOccInterceptor> interceptors)
    {
        this.interceptors = CollectionUtils.isNotEmpty(interceptors) ? interceptors : Collections.<CxOccInterceptor>emptyList();
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


    protected DefaultSessionTokenService getTokenService()
    {
        return this.tokenService;
    }


    @Required
    public void setTokenService(DefaultSessionTokenService tokenService)
    {
        this.tokenService = tokenService;
    }


    protected Validator getPersonalizationIdValidator()
    {
        return this.personalizationIdValidator;
    }


    @Required
    public void setPersonalizationIdValidator(Validator personalizationIdValidator)
    {
        this.personalizationIdValidator = personalizationIdValidator;
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
