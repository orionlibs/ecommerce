package de.hybris.platform.cockpit.util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;

public class HybrisDesktopCleanup implements DesktopCleanup, DesktopInit
{
    private static final Logger LOG = LoggerFactory.getLogger(HybrisDesktopCleanup.class);
    private static final String Y_CLEANED = "y.cleaned";
    public static final String REMOVAL_LISTENER_ATTRIBUTE = "desktopRemovalListener";


    public static final void enableDesktopCleanup(Component comp, DesktopRemovalAwareComponent listener)
    {
        comp.setAttribute("desktopRemovalListener", listener);
    }


    protected DesktopRemovalAwareComponent getRemovalListener(Component comp)
    {
        if(comp instanceof DesktopRemovalAwareComponent)
        {
            return (DesktopRemovalAwareComponent)comp;
        }
        Object listener = comp.getAttribute("desktopRemovalListener");
        if(listener instanceof DesktopRemovalAwareComponent)
        {
            return (DesktopRemovalAwareComponent)listener;
        }
        return null;
    }


    private void performCleanup(Desktop desktop)
    {
        if(desktop.getAttribute("y.cleaned") == null)
        {
            try
            {
                Set<Component> toProcess = new LinkedHashSet<>(desktop.getComponents());
                while(!toProcess.isEmpty())
                {
                    Set<Component> additional = new LinkedHashSet<>();
                    for(Component comp : toProcess)
                    {
                        DesktopRemovalAwareComponent listener = getRemovalListener(comp);
                        if(listener != null)
                        {
                            try
                            {
                                listener.desktopRemoved(desktop);
                                if(listener instanceof DesktopRemovalAwareComponentExt)
                                {
                                    for(Component add : ((DesktopRemovalAwareComponentExt)listener).getAdditionalComponents())
                                    {
                                        if(!toProcess.contains(add))
                                        {
                                            additional.add(add);
                                        }
                                    }
                                }
                            }
                            catch(UiException uiException)
                            {
                            }
                            catch(Throwable e)
                            {
                                if(Executions.getCurrent() == null)
                                {
                                    if(LOG.isDebugEnabled())
                                    {
                                        LOG.error("desktop cleanup error", e);
                                    }
                                    continue;
                                }
                                LOG.error("desktop cleanup error", e);
                            }
                        }
                    }
                    toProcess = additional;
                }
            }
            finally
            {
                desktop.setAttribute("y.cleaned", Boolean.TRUE);
            }
        }
    }


    public void cleanup(Desktop desktop)
    {
        performCleanup(desktop);
    }


    public void init(Desktop desktop, Object request)
    {
        HttpServletRequest req = (HttpServletRequest)request;
        Map<String, Desktop> previousDesktops = (Map<String, Desktop>)req.getSession().getAttribute("previousDesktops");
        if(previousDesktops == null)
        {
            req.getSession().setAttribute("previousDesktops", previousDesktops = new HashMap<>());
        }
        String key = desktop.getRequestPath().toLowerCase();
        Desktop replaced = previousDesktops.get(key);
        if(replaced != null && !desktop.equals(replaced))
        {
            performCleanup(replaced);
        }
        previousDesktops.put(key, desktop);
    }
}
