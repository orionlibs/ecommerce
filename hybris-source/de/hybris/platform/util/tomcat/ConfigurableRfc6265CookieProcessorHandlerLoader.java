package de.hybris.platform.util.tomcat;

import de.hybris.bootstrap.tomcat.cookieprocessor.CookieHandler;
import de.hybris.bootstrap.tomcat.cookieprocessor.LogHandler;
import de.hybris.bootstrap.tomcat.cookieprocessor.Rfc6265CookieProcessorLogicHolder;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.config.ConfigIntf;
import org.springframework.beans.factory.InitializingBean;

public class ConfigurableRfc6265CookieProcessorHandlerLoader implements InitializingBean
{
    public static final String PARAM_INSTALL_APPLICATION_HANDLER = "cookies.SameSite.install.application.handler";
    public static final String PARAM_SAMESITE_ENABLED = "cookies.SameSite.enabled";


    public void afterPropertiesSet() throws Exception
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(tenant.equals(Registry.getMasterTenant()) && !Registry.isStandaloneMode())
        {
            ConfigIntf cfg = tenant.getConfig();
            if(cfg.getBoolean("cookies.SameSite.install.application.handler", false))
            {
                configureCookieProcessor(cfg);
            }
        }
    }


    protected void configureCookieProcessor(ConfigIntf cfg)
    {
        Rfc6265CookieProcessorLogicHolder.setCookieHandler((CookieHandler)new ConfigurableRfc6265CookieHandler(cfg));
        Rfc6265CookieProcessorLogicHolder.setLogHandler((LogHandler)new Slf4JLogHandler());
    }
}
