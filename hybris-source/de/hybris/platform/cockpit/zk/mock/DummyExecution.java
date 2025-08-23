package de.hybris.platform.cockpit.zk.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.zkoss.idom.Document;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.au.AuResponse;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.sys.ExecutionCtrl;
import org.zkoss.zk.ui.sys.Visualizer;
import org.zkoss.zk.xel.Evaluator;

public class DummyExecution implements Execution, ExecutionCtrl
{
    private final ApplicationContext applicationContext;


    public DummyExecution(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public void addAuResponse(String key, AuResponse resposne)
    {
    }


    public void addResponseHeader(String name, String value)
    {
    }


    public boolean containsResponseHeader(String name)
    {
        return false;
    }


    public Component[] createComponents(PageDefinition pagedef, Map arg)
    {
        return null;
    }


    public Component[] createComponents(String uri, Map arg)
    {
        return null;
    }


    public Component createComponents(PageDefinition pagedef, Component parent, Map arg)
    {
        return null;
    }


    public Component createComponents(String uri, Component parent, Map arg)
    {
        return null;
    }


    public Component[] createComponentsDirectly(String content, String extension, Map arg)
    {
        return null;
    }


    public Component[] createComponentsDirectly(Document content, String extension, Map arg)
    {
        return null;
    }


    public Component[] createComponentsDirectly(Reader reader, String extension, Map arg) throws IOException
    {
        return null;
    }


    public Component createComponentsDirectly(String content, String extension, Component parent, Map arg)
    {
        return null;
    }


    public Component createComponentsDirectly(Document content, String extension, Component parent, Map arg)
    {
        return null;
    }


    public Component createComponentsDirectly(Reader reader, String extension, Component parent, Map arg) throws IOException
    {
        return null;
    }


    public String encodeURL(String uri)
    {
        return null;
    }


    public Object evaluate(Component comp, String expr, Class expectedType)
    {
        return null;
    }


    public Object evaluate(Page page, String expr, Class expectedType)
    {
        return null;
    }


    public void forward(String page) throws IOException
    {
    }


    public void forward(Writer writer, String page, Map params, int mode) throws IOException
    {
    }


    public Map getArg()
    {
        return null;
    }


    public Object getAttribute(String name)
    {
        return null;
    }


    public Map getAttributes()
    {
        return null;
    }


    public String getContextPath()
    {
        return null;
    }


    public Desktop getDesktop()
    {
        return (Desktop)new DummyDesktop(this.applicationContext);
    }


    public Evaluator getEvaluator(Page page, Class expfcls)
    {
        return null;
    }


    public Evaluator getEvaluator(Component comp, Class expfcls)
    {
        return null;
    }


    public String getHeader(String name)
    {
        return null;
    }


    public Iterator getHeaderNames()
    {
        return null;
    }


    public Iterator getHeaders(String name)
    {
        return null;
    }


    public String getLocalAddr()
    {
        return null;
    }


    public String getLocalName()
    {
        return null;
    }


    public int getLocalPort()
    {
        return 0;
    }


    public Object getNativeRequest()
    {
        return null;
    }


    public Object getNativeResponse()
    {
        return null;
    }


    public PageDefinition getPageDefinition(String uri)
    {
        return null;
    }


    public PageDefinition getPageDefinitionDirectly(String content, String extension)
    {
        return null;
    }


    public PageDefinition getPageDefinitionDirectly(Document content, String extension)
    {
        return null;
    }


    public PageDefinition getPageDefinitionDirectly(Reader reader, String extension) throws IOException
    {
        return null;
    }


    public String getParameter(String name)
    {
        return null;
    }


    public Map getParameterMap()
    {
        return null;
    }


    public String[] getParameterValues(String name)
    {
        return null;
    }


    public String getRemoteAddr()
    {
        return null;
    }


    public String getRemoteHost()
    {
        return null;
    }


    public String getRemoteName()
    {
        return null;
    }


    public String getRemoteUser()
    {
        return null;
    }


    public String getScheme()
    {
        return null;
    }


    public String getServerName()
    {
        return null;
    }


    public int getServerPort()
    {
        return 0;
    }


    public String getUserAgent()
    {
        return null;
    }


    public Principal getUserPrincipal()
    {
        return null;
    }


    public VariableResolver getVariableResolver()
    {
        return null;
    }


    public void include(String page) throws IOException
    {
    }


    public void include(Writer writer, String page, Map params, int mode) throws IOException
    {
    }


    public boolean isAsyncUpdate(Page page)
    {
        return false;
    }


    public boolean isBrowser()
    {
        return false;
    }


    public boolean isBrowser(String type)
    {
        return false;
    }


    public boolean isExplorer()
    {
        return false;
    }


    public boolean isExplorer7()
    {
        return false;
    }


    public boolean isForwarded()
    {
        return false;
    }


    public boolean isGecko()
    {
        return false;
    }


    public boolean isGecko3()
    {
        return false;
    }


    public boolean isHilDevice()
    {
        return false;
    }


    public boolean isIncluded()
    {
        return false;
    }


    public boolean isMilDevice()
    {
        return false;
    }


    public boolean isOpera()
    {
        return false;
    }


    public boolean isRobot()
    {
        return false;
    }


    public boolean isSafari()
    {
        return false;
    }


    public boolean isUserInRole(String role)
    {
        return false;
    }


    public boolean isVoided()
    {
        return false;
    }


    public void popArg()
    {
    }


    public void postEvent(Event evt)
    {
    }


    public void postEvent(int priority, Event evt)
    {
    }


    public void pushArg(Map arg)
    {
    }


    public void removeAttribute(String name)
    {
    }


    public void sendRedirect(String uri)
    {
    }


    public void sendRedirect(String uri, String target)
    {
    }


    public void setAttribute(String name, Object value)
    {
    }


    public void setResponseHeader(String name, String value)
    {
    }


    public void setVoided(boolean voided)
    {
    }


    public String toAbsoluteURI(String uri, boolean skipInclude)
    {
        return null;
    }


    public Page getCurrentPage()
    {
        return null;
    }


    public void setCurrentPage(Page page)
    {
    }


    public PageDefinition getCurrentPageDefinition()
    {
        return null;
    }


    public void setCurrentPageDefinition(PageDefinition pgdef)
    {
    }


    public Event getNextEvent()
    {
        return null;
    }


    public boolean isActivated()
    {
        return false;
    }


    public void onActivate()
    {
    }


    public void onDeactivate()
    {
    }


    public boolean isRecovering()
    {
        return false;
    }


    public Visualizer getVisualizer()
    {
        return null;
    }


    public void setHeader(String name, String value)
    {
    }


    public void setDateHeader(String name, long value)
    {
    }


    public void addHeader(String name, String value)
    {
    }


    public void addDateHeader(String name, long value)
    {
    }


    public Object getRequestAttribute(String name)
    {
        return null;
    }


    public void setRequestAttribute(String name, Object value)
    {
    }


    public void setDesktop(Desktop desktop)
    {
    }


    public void setRequestId(String reqId)
    {
    }


    public String getRequestId()
    {
        return null;
    }


    public String locate(String arg0)
    {
        return null;
    }
}
