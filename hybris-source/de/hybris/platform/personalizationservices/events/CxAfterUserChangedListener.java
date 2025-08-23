package de.hybris.platform.personalizationservices.events;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;

public class CxAfterUserChangedListener implements ApplicationListener<AfterSessionUserChangeEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(CxAfterUserChangedListener.class);
    private UserService userService;
    private SessionService sessionService;
    private CxConfigurationService cxConfigurationService;
    private CxRecalculationService cxRecalculationService;
    private CxUserSegmentSessionService cxUserSegmentSessionService;


    public void onApplicationEvent(AfterSessionUserChangeEvent event)
    {
        try
        {
            UserModel user = this.userService.getCurrentUser();
            if(isValidChange(user, event))
            {
                List<RecalculateAction> actions = getUserChangedActions();
                if(actions.contains(RecalculateAction.IGNORE))
                {
                    return;
                }
                if(!actions.contains(RecalculateAction.UPDATE) && this.cxConfigurationService.isUserSegmentsStoreInSession().booleanValue())
                {
                    this.cxUserSegmentSessionService.loadUserSegmentsIntoSession(user);
                }
                this.cxRecalculationService.recalculate(actions);
            }
        }
        catch(RuntimeException e)
        {
            LOG.error("could not calculate Cx Variation", e);
        }
    }


    protected List<RecalculateAction> getUserChangedActions()
    {
        Set<String> userChangedActions = this.cxConfigurationService.getUserChangedActions();
        return (List<RecalculateAction>)userChangedActions.stream()
                        .filter(this::actionExist)
                        .map(RecalculateAction::valueOf)
                        .collect(Collectors.toList());
    }


    protected boolean actionExist(String actionName)
    {
        try
        {
            RecalculateAction.valueOf(actionName);
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Recalculate action doesn't exist :" + actionName, e);
            return false;
        }
        return true;
    }


    protected boolean isValidChange(UserModel user, AfterSessionUserChangeEvent event)
    {
        return (Boolean.TRUE.equals(this.sessionService.getAttribute("ACTIVE_PERSONALIZATION")) &&
                        !StringUtils.equals(user.getUid(), event.getPreviousUserUID()) &&
                        !this.userService.isAnonymousUser(user) &&
                        !this.userService.isAdmin(user) && user instanceof de.hybris.platform.core.model.user.CustomerModel &&
                        !isEvaluationInProgress(user));
    }


    protected boolean isEvaluationInProgress(UserModel user)
    {
        JaloSession currentSession = JaloSession.getCurrentSession();
        synchronized(currentSession)
        {
            Object result = this.sessionService.getAttribute("personalizationservices.AfterSessionUserChanged.last.value");
            String uid = user.getUid();
            if(!uid.equals(result))
            {
                this.sessionService.setAttribute("personalizationservices.AfterSessionUserChanged.last.value", uid);
            }
            return uid.equals(result);
        }
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setCxRecalculationService(CxRecalculationService cxRecalculationService)
    {
        this.cxRecalculationService = cxRecalculationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    protected CxRecalculationService getCxRecalculationService()
    {
        return this.cxRecalculationService;
    }


    protected CxUserSegmentSessionService getCxUserSegmentSessionService()
    {
        return this.cxUserSegmentSessionService;
    }


    @Required
    public void setCxUserSegmentSessionService(CxUserSegmentSessionService cxSegmentSessionService)
    {
        this.cxUserSegmentSessionService = cxSegmentSessionService;
    }
}
