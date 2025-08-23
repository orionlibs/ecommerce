package de.hybris.platform.cms2.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.contentslot.ContentSlot;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForPage;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.migration.DeploymentMigrationUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;

public class Cms2Manager extends GeneratedCms2Manager
{
    private static final Logger LOG = Logger.getLogger(Cms2Manager.class.getName());


    public static Cms2Manager getInstance()
    {
        return (Cms2Manager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("cms2");
    }


    @Deprecated(since = "4.3")
    public Collection<AbstractPage> getPages(ContentSlot contentSlot)
    {
        String query = "SELECT {page} FROM {" + GeneratedCms2Constants.TC.CONTENTSLOTFORPAGE + "} WHERE {contentSlot} = ?slot";
        return getSession().getFlexibleSearch()
                        .search(query, Collections.singletonMap("slot", contentSlot), ContentSlotForPage.class).getResult();
    }


    public void notifyInitializationStart(Map<String, String> params, JspContext ctx) throws Exception
    {
        if("update".equals(params.get("initmethod")))
        {
            boolean deploymentMigrate = Config.getBoolean("cms2.deployment.migrate", true);
            if(deploymentMigrate)
            {
                LOG.info("Migrating duplicated deployments for " + getName());
                DeploymentMigrationUtil.migrateGeneralizedDuplicatedDeployments("basecommerce", new String[] {"de.hybris.platform.persistence.link.cms2_" + GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, "de.hybris.platform.persistence.cms2_" + GeneratedCms2Constants.TC.CMSSITE});
            }
        }
    }
}
