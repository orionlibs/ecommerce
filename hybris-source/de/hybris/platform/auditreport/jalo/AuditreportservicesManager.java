package de.hybris.platform.auditreport.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditreportservicesManager extends GeneratedAuditreportservicesManager
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditreportservicesManager.class);


    public static AuditreportservicesManager getInstance()
    {
        return (AuditreportservicesManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("auditreportservices");
    }


    public AuditreportservicesManager()
    {
        LOG.debug("constructor of AuditreportservicesManager called.");
    }


    public void init()
    {
        LOG.debug("init() of AuditreportservicesManager called, current tenant: {}", getTenant().getTenantID());
    }


    public void destroy()
    {
        LOG.debug("destroy() of AuditreportservicesManager called, current tenant: {}", getTenant().getTenantID());
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
