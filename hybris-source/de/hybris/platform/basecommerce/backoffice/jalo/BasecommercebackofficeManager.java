package de.hybris.platform.basecommerce.backoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class BasecommercebackofficeManager extends GeneratedBasecommercebackofficeManager
{
    private static final Logger LOG = Logger.getLogger(BasecommercebackofficeManager.class.getName());


    public BasecommercebackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of BasecommercebackofficeManager called.");
        }
    }


    public static BasecommercebackofficeManager getInstance()
    {
        return (BasecommercebackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("basecommercebackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
