package de.hybris.platform.voucher.backoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class VoucherbackofficeManager extends GeneratedVoucherbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(VoucherbackofficeManager.class.getName());


    public VoucherbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of VoucherbackofficeManager called.");
        }
    }


    public static VoucherbackofficeManager getInstance()
    {
        return (VoucherbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("voucherbackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
