package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.PushComponent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.PushController;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.suspend.SuspendResumeService;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.suspend.SystemStatus;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;

public abstract class AbstractPushController extends RegistrableThread implements PushController
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPushController.class);
    protected static final int DEFAULT_DELAY = 60000;
    public static final String PARAM_UPDATE_INTERVAL = "updateInterval";
    private int updateInterval = 60000;
    private volatile boolean done = false;
    private final Tenant tenant;
    private transient PushComponent component = null;
    protected final Map<String, Object> params = new HashMap<>();
    private SuspendResumeService suspendResumeService;


    public AbstractPushController()
    {
        this(null);
    }


    public AbstractPushController(PushComponent pushComp)
    {
        this(pushComp, 60000);
    }


    public AbstractPushController(PushComponent pushComp, int updateInterval)
    {
        this.component = pushComp;
        this.updateInterval = updateInterval;
        this.tenant = Registry.getCurrentTenant();
        withInitialInfo(OperationInfo.builder().withCategory(getThreadOperationInfoCategory()).build());
    }


    public void setUpdateInterval(int milliseconds)
    {
        this.updateInterval = milliseconds;
    }


    public int getUpdateInterval()
    {
        return this.updateInterval;
    }


    public void setComponent(PushComponent component)
    {
        this.component = component;
    }


    public PushComponent getComponent()
    {
        return this.component;
    }


    protected Desktop getDesktop()
    {
        return (this.component == null) ? null : this.component.getDesktop();
    }


    public void setDone()
    {
        this.done = true;
    }


    protected boolean isDone()
    {
        return this.done;
    }


    protected void internalRun()
    {
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        if(getDesktop() == null)
        {
            LOG.warn("Desktop or the current execution is null. Aborting execution of push controller.");
            return;
        }
        if(this.component == null)
        {
            LOG.warn("Pushable component is null. Aborting execution of push controller.");
        }
        else
        {
            Session slSession = null;
            try
            {
                Registry.setCurrentTenant(this.tenant);
                UserModel user = getSession().getUser();
                if(user == null)
                {
                    LOG.warn("Can not create non-anonymous SL session. Reason: No user available.");
                }
                else
                {
                    try
                    {
                        slSession = sessionService.createNewSession();
                        sessionService.setAttribute("user", user);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Aborting push controller execution. Reason: Unknown error '" + e.getMessage() + "'.", e);
                        return;
                    }
                }
                before();
                while(!isDone())
                {
                    try
                    {
                        if(getDesktop() == null)
                        {
                            setDone();
                            LOG.warn("Can not update using server push. Reason: No desktop available.");
                            if(getDesktop() != null)
                            {
                                try
                                {
                                    Executions.deactivate(getDesktop());
                                }
                                catch(Exception e)
                                {
                                    LOG.warn("Could not deactivate desktop.", e);
                                }
                            }
                            break;
                        }
                        if(getSuspendResumeService().getSystemStatus() != SystemStatus.RUNNING)
                        {
                            setDone();
                            LOG.info("System suspended, stopping cockpit push thread:" + getThreadOperationInfoCategory());
                        }
                        else if(isUpdateNeeded())
                        {
                            Executions.activate(getDesktop());
                            getComponent().clearEvents();
                            update();
                            getComponent().dispatchEvents();
                        }
                        else
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug(toString() + ": Nothing to update. Sleeping for " + toString() + " seconds");
                            }
                            Threads.sleep(getUpdateInterval());
                        }
                    }
                    catch(SystemIsSuspendedException se)
                    {
                        setDone();
                        String message = String.format("Stopping push controller '%s'...", new Object[] {getThreadOperationInfoCategory()});
                        if(LOG.isDebugEnabled())
                        {
                            LOG.info(message, (Throwable)se);
                        }
                        else
                        {
                            LOG.info(message);
                        }
                        break;
                    }
                    catch(IllegalStateException ise)
                    {
                        this.done = true;
                        if(Registry.getCurrentTenant() != null && Registry.getCurrentTenant().getSingletonCreator() != null)
                        {
                            throw ise;
                        }
                        LOG.info("Stopping push controller '" + toString() + "'...");
                        break;
                    }
                    catch(Exception e)
                    {
                        if(!isDone())
                        {
                            LOG.error("An error occurred while executing server push. Removing push controller '" + toString() + "'.", e);
                            this.done = true;
                        }
                        break;
                    }
                    finally
                    {
                        if(getDesktop() != null)
                        {
                            try
                            {
                                Executions.deactivate(getDesktop());
                            }
                            catch(Exception e)
                            {
                                LOG.warn("Could not deactivate desktop.", e);
                            }
                        }
                    }
                }
            }
            finally
            {
                try
                {
                    if(slSession != null)
                    {
                        sessionService.closeSession(slSession);
                    }
                }
                catch(IllegalStateException ise)
                {
                    LOG.info("Deactivating SL session failed. Reason: '" + ise.getMessage() + "'. System shutdown in progress?");
                }
                after();
                this.component.enableServerPush(false);
            }
        }
    }


    private String getThreadOperationInfoCategory()
    {
        return String.format("%s - [updateInterval='%sms']", new Object[] {getClass().getSimpleName(), Integer.valueOf(this.updateInterval)});
    }


    public String toString()
    {
        return super.toString() + " - [done='" + super.toString() + "', updateInterval='" + this.done + "ms']";
    }


    protected void addEvent(CockpitEvent cockpitEvent)
    {
        getComponent().addEvent(cockpitEvent);
    }


    public void startController()
    {
        this.done = false;
        loadParameters();
        start();
    }


    protected UISession getSession()
    {
        UISession session = null;
        try
        {
            session = (UISession)getComponent().getDesktop().getWebApp().getAttribute("session");
        }
        catch(Exception e)
        {
            LOG.error("An error occurred while retrieving UI session.", e);
        }
        return session;
    }


    protected WebApplicationContext getWebApplicationContext() throws IllegalStateException
    {
        WebApplicationContext webCtx = null;
        try
        {
            webCtx = WebApplicationContextUtils.getWebApplicationContext((ServletContext)getComponent().getDesktop().getWebApp().getNativeContext());
        }
        catch(ClassCastException cce)
        {
            throw new IllegalStateException("Could not retrieve spring web application context.", cce);
        }
        return webCtx;
    }


    protected abstract void before();


    protected abstract void after();


    public void setParameters(Map<String, Object> params)
    {
        this.params.clear();
        if(params != null)
        {
            this.params.putAll(params);
        }
    }


    public Map<String, Object> getParameters()
    {
        return Collections.unmodifiableMap(this.params);
    }


    protected void loadParameters()
    {
        Object updateParam = this.params.get("updateInterval");
        if(updateParam != null)
        {
            try
            {
                this.updateInterval = Integer.parseInt(updateParam.toString());
            }
            catch(NumberFormatException nfe)
            {
                LOG.warn("Specified update interval not valid. Using previously set value.");
            }
        }
    }


    private SuspendResumeService getSuspendResumeService()
    {
        if(this.suspendResumeService == null)
        {
            this.suspendResumeService = (SuspendResumeService)getWebApplicationContext().getBean("suspendResumeService", SuspendResumeService.class);
        }
        return this.suspendResumeService;
    }
}
