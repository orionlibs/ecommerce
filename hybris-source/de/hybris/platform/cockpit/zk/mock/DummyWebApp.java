package de.hybris.platform.cockpit.zk.mock;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.sys.DesktopCache;
import org.zkoss.zk.ui.sys.DesktopCacheProvider;
import org.zkoss.zk.ui.sys.FailoverManager;
import org.zkoss.zk.ui.sys.IdGenerator;
import org.zkoss.zk.ui.sys.SessionCache;
import org.zkoss.zk.ui.sys.UiEngine;
import org.zkoss.zk.ui.sys.UiFactory;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.util.Configuration;

public class DummyWebApp implements WebApp, WebAppCtrl
{
    private DummyServletContext servletCtx;
    private final String springConstant = "org.springframework.web.context.WebApplicationContext.ROOT";
    private final ApplicationContext applicationContext;


    public DummyWebApp(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public String getAppName()
    {
        return null;
    }


    public Object getAttribute(String name)
    {
        return this.servletCtx.getAttribute(name);
    }


    public Map getAttributes()
    {
        return null;
    }


    public String getBuild()
    {
        return null;
    }


    public Configuration getConfiguration()
    {
        return null;
    }


    public String getInitParameter(String name)
    {
        return null;
    }


    public Iterator getInitParameterNames()
    {
        return null;
    }


    public String getMimeType(String file)
    {
        return null;
    }


    public Object getNativeContext()
    {
        this.servletCtx = new DummyServletContext();
        this.servletCtx.setAttribute("org.springframework.web.context.WebApplicationContext.ROOT", new DummyWebApplicationContext(this.applicationContext, (ServletContext)this.servletCtx));
        return this.servletCtx;
    }


    public String getRealPath(String path)
    {
        return null;
    }


    public URL getResource(String path)
    {
        return null;
    }


    public InputStream getResourceAsStream(String path)
    {
        return null;
    }


    public Set getResourcePaths(String path)
    {
        return null;
    }


    public int getSubversion(int portion)
    {
        return 0;
    }


    public String getUpdateURI()
    {
        return null;
    }


    public String getVersion()
    {
        return null;
    }


    public WebApp getWebApp(String uripath)
    {
        return null;
    }


    public void removeAttribute(String name)
    {
    }


    public void setAppName(String name)
    {
    }


    public void setAttribute(String name, Object value)
    {
    }


    public String getDirectory()
    {
        return null;
    }


    public void init(Object context, Configuration config)
    {
    }


    public void destroy()
    {
    }


    public UiEngine getUiEngine()
    {
        return null;
    }


    public void setUiEngine(UiEngine engine)
    {
    }


    public DesktopCache getDesktopCache(Session sess)
    {
        return null;
    }


    public DesktopCacheProvider getDesktopCacheProvider()
    {
        return null;
    }


    public void setDesktopCacheProvider(DesktopCacheProvider provider)
    {
    }


    public UiFactory getUiFactory()
    {
        return null;
    }


    public void setUiFactory(UiFactory factory)
    {
    }


    public FailoverManager getFailoverManager()
    {
        return null;
    }


    public void setFailoverManager(FailoverManager manager)
    {
    }


    public IdGenerator getIdGenerator()
    {
        return null;
    }


    public void setIdGenerator(IdGenerator generator)
    {
    }


    public SessionCache getSessionCache()
    {
        return null;
    }


    public void setSessionCache(SessionCache cache)
    {
    }


    public void sessionWillPassivate(Session sess)
    {
    }


    public void sessionDidActivate(Session sess)
    {
    }


    public void sessionDestroyed(Session sess)
    {
    }
}
