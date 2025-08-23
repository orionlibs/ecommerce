package de.hybris.platform.cockpit.zk.mock;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.zkoss.util.media.Media;
import org.zkoss.zk.device.Device;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.sys.DesktopCtrl;
import org.zkoss.zk.ui.sys.EventProcessingThread;
import org.zkoss.zk.ui.sys.RequestQueue;
import org.zkoss.zk.ui.sys.ServerPush;
import org.zkoss.zk.ui.sys.Visualizer;
import org.zkoss.zk.ui.util.EventInterceptor;

public class DummyDesktop implements Desktop, DesktopCtrl
{
    private final ApplicationContext applicationContext;


    public DummyDesktop(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public void addEventInterceptor(EventInterceptor eventInterceptor)
    {
    }


    public void addListener(Object listener)
    {
    }


    public boolean enableServerPush(boolean enable)
    {
        return false;
    }


    public Object getAttribute(String name)
    {
        return null;
    }


    public Map getAttributes()
    {
        return null;
    }


    public String getBookmark()
    {
        return null;
    }


    public Component getComponentByUuid(String uuid)
    {
        return null;
    }


    public Component getComponentByUuidIfAny(String uuid)
    {
        return null;
    }


    public Collection getComponents()
    {
        return null;
    }


    public String getCurrentDirectory()
    {
        return null;
    }


    public Device getDevice()
    {
        return null;
    }


    public String getDeviceType()
    {
        return null;
    }


    public String getDownloadMediaURI(Media media, String pathInfo)
    {
        return null;
    }


    public String getDynamicMediaURI(Component comp, String pathInfo)
    {
        return null;
    }


    public Execution getExecution()
    {
        return null;
    }


    public String getId()
    {
        return null;
    }


    public Page getPage(String pageId) throws ComponentNotFoundException
    {
        return null;
    }


    public Page getPageIfAny(String pageId)
    {
        return null;
    }


    public Collection getPages()
    {
        return null;
    }


    public String getRequestPath()
    {
        return null;
    }


    public Session getSession()
    {
        return null;
    }


    public String getUpdateURI(String pathInfo)
    {
        return null;
    }


    public WebApp getWebApp()
    {
        return (WebApp)new DummyWebApp(this.applicationContext);
    }


    public boolean hasPage(String pageId)
    {
        return false;
    }


    public void invalidate()
    {
    }


    public boolean isAlive()
    {
        return false;
    }


    public boolean isServerPushEnabled()
    {
        return false;
    }


    public Object removeAttribute(String name)
    {
        return null;
    }


    public boolean removeEventInterceptor(EventInterceptor eventInterceptor)
    {
        return false;
    }


    public boolean removeListener(Object listener)
    {
        return false;
    }


    public Object setAttribute(String name, Object value)
    {
        return null;
    }


    public void setBookmark(String name)
    {
    }


    public void setCurrentDirectory(String dir)
    {
    }


    public void setDeviceType(String deviceType)
    {
    }


    public void setServerPushDelay(int min, int max, int factor)
    {
    }


    public boolean activateServerPush(long timeout) throws InterruptedException
    {
        return false;
    }


    public void addComponent(Component comp)
    {
    }


    public void addPage(Page page)
    {
    }


    public void afterComponentAttached(Component comp, Page page)
    {
    }


    public void afterComponentDetached(Component comp, Page prevpage)
    {
    }


    public void afterComponentMoved(Component parent, Component child, Component prevparent)
    {
    }


    public void afterProcessEvent(Event event)
    {
    }


    public Event beforePostEvent(Event event)
    {
        return null;
    }


    public Event beforeProcessEvent(Event event)
    {
        return null;
    }


    public Event beforeSendEvent(Event event)
    {
        return null;
    }


    public boolean ceaseSuspendedThread(EventProcessingThread evtthd, String cause)
    {
        return false;
    }


    public void deactivateServerPush()
    {
    }


    public void destroy()
    {
    }


    public boolean enableServerPush(ServerPush serverpush)
    {
        return false;
    }


    public Object getActivationLock()
    {
        return null;
    }


    public Media getDownloadMedia(String medId, boolean reserved)
    {
        return null;
    }


    public Object getLastResponse(String channel, String reqId)
    {
        return null;
    }


    public int getNextKey()
    {
        return 0;
    }


    public String getNextUuid()
    {
        return null;
    }


    public RequestQueue getRequestQueue()
    {
        return null;
    }


    public int getResponseId(boolean advance)
    {
        return 0;
    }


    public ServerPush getServerPush()
    {
        return null;
    }


    public Collection getSuspendedThreads()
    {
        return null;
    }


    public Visualizer getVisualizer()
    {
        return null;
    }


    public void invokeDesktopCleanups()
    {
    }


    public void invokeExecutionCleanups(Execution exec, Execution parent, List errs)
    {
    }


    public void invokeExecutionInits(Execution exec, Execution parent) throws UiException
    {
    }


    public void onPiggyback()
    {
    }


    public void onPiggybackListened(Component comp, boolean listen)
    {
    }


    public void recoverDidFail(Throwable exception)
    {
    }


    public void removeComponent(Component comp)
    {
    }


    public void removePage(Page page)
    {
    }


    public void responseSent(String channel, String reqId, Object resInfo)
    {
    }


    public void sessionDidActivate(Session sess)
    {
    }


    public void sessionWillPassivate(Session sess)
    {
    }


    public void setBookmarkByClient(String name)
    {
    }


    public void setExecution(Execution exec)
    {
    }


    public void setId(String id)
    {
    }


    public void setResponseId(int resId)
    {
    }


    public void setVisualizer(Visualizer visualizer)
    {
    }


    public void setBookmark(String arg0, boolean arg1)
    {
    }
}
