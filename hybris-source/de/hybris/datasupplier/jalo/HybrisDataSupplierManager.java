package de.hybris.datasupplier.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class HybrisDataSupplierManager extends GeneratedHybrisDataSupplierManager
{
    private static final Logger LOG = Logger.getLogger(HybrisDataSupplierManager.class.getName());


    public static HybrisDataSupplierManager getInstance()
    {
        return (HybrisDataSupplierManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("hybrisdatasupplier");
    }


    public HybrisDataSupplierManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of HybrisDataSupplierManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of HybrisDataSupplierManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of HybrisDataSupplierManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
