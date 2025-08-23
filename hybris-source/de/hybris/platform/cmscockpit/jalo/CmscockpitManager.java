package de.hybris.platform.cmscockpit.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.util.JspContext;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CmscockpitManager extends GeneratedCmscockpitManager
{
    private static final Logger LOG = Logger.getLogger(CmscockpitManager.class.getName());


    public static CmscockpitManager getInstance()
    {
        return (CmscockpitManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("cmscockpit");
    }


    public CmscockpitManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of CmscockpitManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of CmscockpitManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of CmscockpitManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc) throws Exception
    {
        super.createEssentialData(params, jspc);
        try
        {
            importCSVFromResources("/cmscockpit/import/cockpit_templates.csv", "utf-8");
            importCSVFromResources("/cmscockpit/import/cmsmanagergroup_setup.csv", "utf-8");
            importCSVFromResources("/cmscockpit/import/cmscockpit-users.csv", "utf-8");
        }
        catch(Exception e)
        {
            LOG.warn("Could not import CMS Cockpit related Cockpit templates. Please import manually.", e);
        }
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
        try
        {
            importCSVFromResources("/cmscockpit/import/ui_components.csv", "utf-8");
        }
        catch(Exception e)
        {
            LOG.warn("Could not import CMS Cockpit UI component configuration data. Please import manually.", e);
        }
    }


    protected void importCSVFromResources(String csv, String encoding) throws Exception
    {
        String enc = StringUtils.isBlank(encoding) ? "utf-8" : encoding;
        LOG.info("importing resource " + csv);
        InputStream inputStream = CmscockpitManager.class.getResourceAsStream(csv);
        try
        {
            ImpExManager.getInstance().importData(inputStream, enc, ';', '"', true);
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        catch(Throwable throwable)
        {
            if(inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }
}
