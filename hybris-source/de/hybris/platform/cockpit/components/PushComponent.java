package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.PushController;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Span;
import org.zkoss.zul.Timer;

public class PushComponent<T extends PushController> extends Span implements DesktopRemovalAwareComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(PushComponent.class);
    protected static final int DEFAULT_DELAY = 60000;
    private final transient List<CockpitEvent> events = new ArrayList<>();
    private final T pushController;
    private transient Timer eventTimer = null;


    public PushComponent(Class<T> controllerClass) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        this(controllerClass, 60000);
    }


    public PushComponent(Class<T> controllerClass, int updateInterval) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        this(controllerClass.newInstance(), updateInterval);
    }


    public PushComponent(T controller)
    {
        this(controller, controller.getUpdateInterval());
    }


    public PushComponent(T controller, int updateInterval)
    {
        if(controller == null)
        {
            throw new IllegalArgumentException("Push controller can not be null");
        }
        this.pushController = controller;
        this.pushController.setUpdateInterval(updateInterval);
        this.pushController.setComponent(this);
        initialize();
    }


    public boolean insertBefore(Component newChild, Component refChild)
    {
        LOG.warn("Push component does not allow any children components.");
        return false;
    }


    protected void appendChildInternal(Component child)
    {
        super.insertBefore(child, null);
    }


    public void clearEvents()
    {
        this.events.clear();
    }


    public void addEvent(CockpitEvent event)
    {
        this.events.add(event);
    }


    public List<CockpitEvent> getEvents()
    {
        return Collections.unmodifiableList(this.events);
    }


    public void dispatchEvents()
    {
        if(this.eventTimer == null)
        {
            initEventTimer();
        }
        this.eventTimer.start();
    }


    private void initEventTimer()
    {
        this.eventTimer = new Timer();
        appendChildInternal((Component)this.eventTimer);
        this.eventTimer.setDelay(1);
        this.eventTimer.setRepeats(false);
        this.eventTimer.addEventListener("onTimer", (EventListener)new Object(this));
    }


    private void initialize()
    {
        Timer initTimer = new Timer();
        appendChildInternal((Component)initTimer);
        initTimer.setRunning(true);
        initTimer.setDelay(1);
        initTimer.setRepeats(false);
        initTimer.addEventListener("onTimer", (EventListener)new Object(this, initTimer));
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    public void enableServerPush(boolean enable)
    {
        try
        {
            getDesktop().enableServerPush(enable);
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("%s server push for desktop:%s", new Object[] {enable ? "Enable" : "Disable", getDesktop()}));
            }
        }
        catch(Exception e)
        {
            LOG.warn(String.format("Cannot %s server push for desktop:%s", new Object[] {enable ? "enable" : "disable", getDesktop()}));
        }
    }


    private void cleanup()
    {
        this.pushController.setDone();
    }
}
