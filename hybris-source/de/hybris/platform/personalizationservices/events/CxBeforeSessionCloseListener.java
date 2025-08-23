package de.hybris.platform.personalizationservices.events;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.servicelayer.event.events.BeforeSessionCloseEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CxBeforeSessionCloseListener extends AbstractEventListener<BeforeSessionCloseEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(CxBeforeSessionCloseListener.class);
    private ModelService modelService;
    private BaseSiteService baseSiteService;
    private UserService userService;
    private DefaultSessionTokenService defaultSessionTokenService;
    private CxConfigurationService cxConfigurationService;
    private CxUserSegmentSessionService cxUserSegmentSessionService;
    private TaskService taskService;
    private String anonymousUserTaskRunnerName;
    private String registeredUserTaskRunnerName;


    protected void onEvent(BeforeSessionCloseEvent event)
    {
        JaloSession session = (JaloSession)event.getSource();
        if(isPersonalizationActive(session))
        {
            try
            {
                UserModel user = (UserModel)this.modelService.get(session.getUser());
                if(this.userService.isAnonymousUser(user))
                {
                    scheduleTaskForAnonymousUser(this.defaultSessionTokenService.getOrCreateSessionToken());
                }
                else
                {
                    scheduleTaskForRegisteredUser(user);
                }
            }
            catch(RuntimeException e)
            {
                LOG.error("Before session close listener for personalization failed.", e);
            }
        }
    }


    protected boolean isPersonalizationActive(JaloSession session)
    {
        if(session != null && session.getHttpSessionId() != null && session.getSessionContext() != null)
        {
            return BooleanUtils.isTrue((Boolean)session.getSessionContext().getAttribute("ACTIVE_PERSONALIZATION"));
        }
        return false;
    }


    protected void scheduleTaskForAnonymousUser(String sessionKey)
    {
        LOG.debug("Sheduling task for anonymous user - sessionId : {}", sessionKey);
        if(shouldCreateTaskForAnonymousUser())
        {
            TaskModel task = (TaskModel)this.modelService.create(TaskModel.class);
            task.setContext(createContextForAnonymousUser(sessionKey));
            task.setRunnerBean(this.anonymousUserTaskRunnerName);
            task.setExecutionDate(new Date());
            this.taskService.scheduleTask(task);
        }
    }


    boolean shouldCreateTaskForAnonymousUser()
    {
        return StringUtils.isNotBlank(this.anonymousUserTaskRunnerName);
    }


    protected Object createContextForAnonymousUser(String sessionKey)
    {
        return sessionKey;
    }


    protected void scheduleTaskForRegisteredUser(UserModel user)
    {
        LOG.debug("Sheduling task for registered user : {}", user.getUid());
        if(shouldCreateTaskForRegisteredUser(user))
        {
            TaskModel task = (TaskModel)this.modelService.create(TaskModel.class);
            task.setContext(createContextForRegisteredUser(user));
            task.setRunnerBean(this.registeredUserTaskRunnerName);
            task.setExecutionDate(new Date());
            this.taskService.scheduleTask(task);
        }
    }


    boolean shouldCreateTaskForRegisteredUser(UserModel user)
    {
        return (StringUtils.isNotBlank(this.registeredUserTaskRunnerName) && this.cxConfigurationService.isUserSegmentsStoreInSession().booleanValue() &&
                        CollectionUtils.isNotEmpty(this.cxUserSegmentSessionService.getUserSegmentsFromSession(user)));
    }


    protected Object createContextForRegisteredUser(UserModel user)
    {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("user", user.getUid());
        contextMap.put("baseSite", this.baseSiteService.getCurrentBaseSite().getUid());
        contextMap.put("segments", this.cxUserSegmentSessionService.getUserSegmentsFromSession(user));
        return contextMap;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected TaskService getTaskService()
    {
        return this.taskService;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }


    protected String getAnonymousUserTaskRunnerName()
    {
        return this.anonymousUserTaskRunnerName;
    }


    public void setAnonymousUserTaskRunnerName(String anonymousUserTaskRunnerName)
    {
        this.anonymousUserTaskRunnerName = anonymousUserTaskRunnerName;
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


    protected DefaultSessionTokenService getDefaultSessionTokenService()
    {
        return this.defaultSessionTokenService;
    }


    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService)
    {
        this.defaultSessionTokenService = defaultSessionTokenService;
    }


    protected String getRegisteredUserTaskRunnerName()
    {
        return this.registeredUserTaskRunnerName;
    }


    public void setRegisteredUserTaskRunnerName(String registeredUserTaskRunnerName)
    {
        this.registeredUserTaskRunnerName = registeredUserTaskRunnerName;
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


    protected CxUserSegmentSessionService getCxUserSegmentSessionService()
    {
        return this.cxUserSegmentSessionService;
    }


    @Required
    public void setCxUserSegmentSessionService(CxUserSegmentSessionService cxUserSegmentSessionService)
    {
        this.cxUserSegmentSessionService = cxUserSegmentSessionService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
