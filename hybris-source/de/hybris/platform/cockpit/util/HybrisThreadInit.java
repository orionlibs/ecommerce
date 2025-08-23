package de.hybris.platform.cockpit.util;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.zkoss.util.resource.LabelLocator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadInit;

public class HybrisThreadInit implements EventThreadInit
{
    private volatile Settings settings = null;


    public void prepare(Component comp, Event event) throws Exception
    {
        AbstractTenant tenant = (AbstractTenant)Registry.getCurrentTenantNoFallback();
        JaloSession jaloSession = (tenant != null) ? (JaloSession.hasCurrentSession() ? JaloSession.getCurrentSession((Tenant)tenant) : null) : null;
        MediaUtil.SecureMediaURLRenderer secureMediaURLRenderer = MediaUtil.getCurrentSecureMediaURLRenderer();
        MediaUtil.PublicMediaURLRenderer publicMediaURLRenderer = MediaUtil.getCurrentPublicMediaURLRenderer();
        this.settings = new Settings(tenant, jaloSession, secureMediaURLRenderer, publicMediaURLRenderer);
    }


    public boolean init(Component comp, Event event) throws Exception
    {
        Settings settings = this.settings;
        if(settings.tenant != null)
        {
            Registry.setCurrentTenant((Tenant)settings.tenant);
            if(settings.jaloSession != null)
            {
                settings.jaloSession.activate();
            }
        }
        MediaUtil.setCurrentSecureMediaURLRenderer(settings.secMediaURLRenderer);
        MediaUtil.setCurrentPublicMediaURLRenderer(settings.publicMediaURLRenderer);
        HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper)Executions.getCurrent().getNativeRequest();
        HttpServletRequest req = (HttpServletRequest)wrapper.getRequest();
        WebSessionFunctions.setCurrentHttpServletRequest(req);
        registerLabelLocator();
        return true;
    }


    private void registerLabelLocator()
    {
        if(StringUtils.isEmpty(Labels.getLabel("cockpitLabels")))
        {
            Labels.register((LabelLocator)new CockpitLocator());
            for(ExtensionInfo ext : ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder())
            {
                String resourceFolder = "/" + ext.getName() + "/cockpit/localization/";
                if(getClass().getResource(resourceFolder) != null)
                {
                    ConfigurableCockpitLabelLocator locator = new ConfigurableCockpitLabelLocator();
                    locator.setResourceFolder(resourceFolder);
                    Labels.register((LabelLocator)locator);
                }
            }
            ApplicationContext ctx = Registry.getCoreApplicationContext();
            Map<String, ConfigurableCockpitLabelLocator> labelLocators = ctx.getBeansOfType(ConfigurableCockpitLabelLocator.class);
            if(MapUtils.isNotEmpty(labelLocators))
            {
                for(ConfigurableCockpitLabelLocator locator : labelLocators.values())
                {
                    Labels.register((LabelLocator)locator);
                }
            }
        }
    }
}
