package de.hybris.platform.pcmbackoffice.jalo;

import de.hybris.platform.core.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PcmbackofficeManager extends GeneratedPcmbackofficeManager
{
    private static final Logger LOG = LoggerFactory.getLogger(PcmbackofficeManager.class.getName());


    public PcmbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PcmbackofficeManager called.");
        }
    }


    public static PcmbackofficeManager getInstance()
    {
        return (PcmbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("pcmbackoffice");
    }


    @Deprecated(since = "6.5", forRemoval = true)
    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of YbackofficeManager called. {}", getTenant().getTenantID());
        }
    }


    @Deprecated(since = "6.5", forRemoval = true)
    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of YbackofficeManager called, current tenant: {}", getTenant().getTenantID());
        }
    }
}
