package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.core.Registry;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class MediaConversionManager extends GeneratedMediaConversionManager
{
    private static final Logger LOG = Logger.getLogger(MediaConversionManager.class);


    public MediaConversionManager()
    {
        LOG.debug("constructor of MediaconversionManager called.");
    }


    public static MediaConversionManager getInstance()
    {
        return (MediaConversionManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("mediaconversion");
    }


    public void init()
    {
        LOG.debug(";init() of MediaconversionManager called. " + getTenant().getTenantID());
    }


    public void destroy()
    {
        LOG.debug("destroy() of MediaconversionManager called, current tenant: " + getTenant().getTenantID());
    }
}
