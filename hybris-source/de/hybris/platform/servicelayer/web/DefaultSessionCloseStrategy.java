package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.spring.HybrisContextLoaderListener;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import java.util.concurrent.Callable;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultSessionCloseStrategy implements SessionCloseStrategy
{
    private static final Log LOG = LogFactory.getLog(DefaultSessionCloseStrategy.class.getName());
    public static final int DEFAULT_SESSION_TIMEOUT = 86400;


    public void closeSessionInHttpSession(HttpSession session)
    {
        JaloSession js = (JaloSession)session.getAttribute("jalosession");
        if(js != null && !RedeployUtilities.isShutdownInProgress())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Closed/expired HttpSession " + session.getId() + " contains JaloSession " + js.getSessionID() + " which is being closed now");
            }
            removeJaloSessionFromHttpSession(session);
            closeJaloSession(js);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Closed JaloSession " + js.getSessionID());
            }
        }
    }


    public void setTimeoutOnHttpSessionCreation(HttpSession httpSession)
    {
        Tenant tenant = getCurrentTenant(httpSession);
        if(!trySettingExtensionSpecificTimeout(httpSession, tenant))
        {
            int sessionTimeoutInSecs = tenant.getConfig().getInt("default.session.timeout", 86400);
            httpSession.setMaxInactiveInterval(sessionTimeoutInSecs);
        }
    }


    private boolean trySettingExtensionSpecificTimeout(HttpSession httpSession, Tenant tenant)
    {
        String webApp = httpSession.getServletContext().getContextPath();
        String extensionName = Utilities.getExtensionForWebroot(webApp, tenant.getTenantID());
        int extensionTimeout = tenant.getConfig().getInt(extensionName + ".session.timeout", -1);
        if(extensionTimeout < 0)
        {
            return false;
        }
        httpSession.setMaxInactiveInterval(extensionTimeout);
        return true;
    }


    private Tenant getCurrentTenant(HttpSession httpSession)
    {
        String tenantId = HybrisContextLoaderListener.getTenantIDForWebapp(httpSession.getServletContext());
        if(tenantId == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("tenantId is null, using Master Tenant");
            }
            return (Tenant)MasterTenant.getInstance();
        }
        Tenant tenant = Registry.getTenantByID(tenantId);
        if(tenant != null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("tenantId is provided, using " + tenantId);
            }
            return tenant;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("could not find tenant by tenantId " + tenantId + ", using Master Tenant");
        }
        return (Tenant)MasterTenant.getInstance();
    }


    public void closeJaloSession(JaloSession js)
    {
        try
        {
            Registry.runAsTenant(js.getTenant(), (Callable)new Object(this, js));
        }
        catch(Exception e)
        {
            LOG.warn("error closing JaloSession " + js.getSessionID(), e);
        }
    }


    public void removeJaloSessionFromHttpSession(HttpSession session)
    {
        try
        {
            session.removeAttribute("jalosession");
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("error removing http session attribute 'jalosession' - ignoring since session is invalid any way", e);
            }
        }
    }
}
