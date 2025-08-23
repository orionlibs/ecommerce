package de.hybris.platform.b2bpunchoutbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class B2bpunchoutbackofficeManager extends GeneratedB2bpunchoutbackofficeManager
{
    private static final Logger LOG = LoggerFactory.getLogger(B2bpunchoutbackofficeManager.class.getName());


    public B2bpunchoutbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of B2bpunchoutbackofficeManager called.");
        }
    }


    public static B2bpunchoutbackofficeManager getInstance()
    {
        return (B2bpunchoutbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("b2bpunchoutbackoffice");
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("init() of YbackofficeManager called. %s", new Object[] {getTenant().getTenantID()}));
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("destroy() of YbackofficeManager called, current tenant: %s", new Object[] {getTenant().getTenantID()}));
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
