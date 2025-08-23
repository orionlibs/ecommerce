package de.hybris.platform.promotions.util.legacy;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.promotions.PromotionsService;
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class LegacyModeChecker implements ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(LegacyModeChecker.class);
    private static final String MESSAGE = "\r\n##########################################################################\r\nYou are using the deprecated legacy extension 'promotions'.\r\nPlease note that this extension is no longer supported. To enable it (without support!) and get rid of this message,\r\nplease set the property 'promotions.legacy.mode' to 'true' in your local.properties file and restart the server.\r\n##########################################################################";
    private ApplicationContext applicationContext;
    private PromotionsService promotionsService;
    private PromotionsService legacyPromotionsService;
    private boolean legacyModeEnabled;


    public void check()
    {
        if(!isLegacyModeEnabled())
        {
            logErrorAndThrowException();
        }
    }


    protected void checkLegacyModeOnStartUp()
    {
        if(!isLegacyModeEnabled())
        {
            if(getLegacyPromotionsService().getClass().isInstance(getPromotionsService()))
            {
                String[] promotionServiceBeanNames = getApplicationContext().getBeanNamesForType(PromotionsService.class);
                if(promotionServiceBeanNames.length == 1)
                {
                    logWarning();
                }
                else
                {
                    logErrorAndThrowException();
                }
            }
            else
            {
                Arrays.<Field>stream(getPromotionsService().getClass().getDeclaredFields()).filter(f -> {
                    try
                    {
                        return getLegacyPromotionsService().getClass().isInstance(f.get(getPromotionsService()));
                    }
                    catch(IllegalArgumentException | IllegalAccessException exc)
                    {
                        return false;
                    }
                }).findFirst().ifPresent(f -> logWarning());
            }
        }
    }


    @PostConstruct
    protected void init()
    {
        Registry.registerTenantListener((TenantListener)new LegacyModeCheckerTenantListener(this, Registry.getCurrentTenantNoFallback()));
    }


    protected void logWarning()
    {
        LOG.warn("\r\n##########################################################################\r\nYou are using the deprecated legacy extension 'promotions'.\r\nPlease note that this extension is no longer supported. To enable it (without support!) and get rid of this message,\r\nplease set the property 'promotions.legacy.mode' to 'true' in your local.properties file and restart the server.\r\n##########################################################################");
    }


    protected void logErrorAndThrowException()
    {
        LOG.error("\r\n##########################################################################\r\nYou are using the deprecated legacy extension 'promotions'.\r\nPlease note that this extension is no longer supported. To enable it (without support!) and get rid of this message,\r\nplease set the property 'promotions.legacy.mode' to 'true' in your local.properties file and restart the server.\r\n##########################################################################");
        throw new LegacyException("Legacy implementation of 'promotionsService' is being used.");
    }


    protected PromotionsService getPromotionsService()
    {
        return this.promotionsService;
    }


    @Required
    public void setPromotionsService(PromotionsService promotionsService)
    {
        this.promotionsService = promotionsService;
    }


    protected PromotionsService getLegacyPromotionsService()
    {
        return this.legacyPromotionsService;
    }


    @Required
    public void setLegacyPromotionsService(PromotionsService legacyPromotionsService)
    {
        this.legacyPromotionsService = legacyPromotionsService;
    }


    protected ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    protected boolean isLegacyModeEnabled()
    {
        return this.legacyModeEnabled;
    }


    @Required
    public void setLegacyModeEnabled(boolean legacyModeEnabled)
    {
        this.legacyModeEnabled = legacyModeEnabled;
    }
}
