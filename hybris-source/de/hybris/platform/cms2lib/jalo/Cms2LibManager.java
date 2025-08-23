package de.hybris.platform.cms2lib.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.util.JspContext;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class Cms2LibManager extends GeneratedCms2LibManager
{
    private static final Logger LOG = Logger.getLogger(Cms2LibManager.class.getName());


    public static Cms2LibManager getInstance()
    {
        return (Cms2LibManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("cms2lib");
    }


    public Cms2LibManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of Cms2libManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of Cms2libManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of Cms2libManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc) throws Exception
    {
        super.createEssentialData(params, jspc);
    }


    public void createProjectData(Map<String, String> params, JspContext jspc) throws Exception
    {
        List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
        super.createProjectData(params, jspc);
    }


    protected void importCSVFromResources(String csv) throws Exception
    {
        importCSVFromResources(csv, "UTF-8", ';', '"', true);
    }


    protected void importCSVFromResources(String csv, String encoding, char fieldseparator, char quotecharacter, boolean codeExecution) throws Exception
    {
        LOG.info("importing resource " + csv);
        InputStream inputStream = Cms2LibManager.class.getResourceAsStream(csv);
        try
        {
            if(inputStream == null)
            {
                LOG.warn("Import resource '" + csv + "' not found!");
                if(inputStream != null)
                {
                    inputStream.close();
                }
                return;
            }
            ImpExManager.getInstance().importData(inputStream, encoding, fieldseparator, quotecharacter, codeExecution);
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
