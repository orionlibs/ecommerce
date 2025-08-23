package de.hybris.platform.testframework;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class PropertyConfigSwitcher
{
    private static final Logger LOG = Logger.getLogger(PropertyConfigSwitcher.class);
    private final String propertyName;
    private boolean cfgSaved = false;
    private String cfgBefore = null;


    public PropertyConfigSwitcher(String propertyName)
    {
        this.propertyName = propertyName;
    }


    public void switchToValue(String value)
    {
        if(!this.cfgSaved)
        {
            if(isPersistenceLegacyFalse())
            {
                this.cfgBefore = BooleanUtils.toString((Boolean)getSessionService().getAttribute(this.propertyName), "true", "false", null);
            }
            else
            {
                this.cfgBefore = Config.getParameter(this.propertyName);
            }
            this.cfgSaved = true;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Setting up new value for the property: " + this.propertyName + " => " + value + ", default was: " + this.cfgBefore);
        }
        setParameter(value);
    }


    public void switchBackToDefault()
    {
        if(this.cfgSaved)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Setting back default value for property: " + this.propertyName + ", default value: " + this.cfgBefore);
            }
            setParameter(this.cfgBefore);
            this.cfgSaved = false;
            this.cfgBefore = null;
        }
    }


    private static SessionService getSessionService()
    {
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        return (SessionService)ctx.getBean("sessionService", SessionService.class);
    }


    private boolean isPersistenceLegacyFalse()
    {
        return "persistence.legacy.mode".equals(this.propertyName);
    }


    private void setParameter(String value)
    {
        if(isPersistenceLegacyFalse())
        {
            getSessionService().setAttribute(this.propertyName, BooleanUtils.toBooleanObject(value));
        }
        else
        {
            Config.setParameter(this.propertyName, value);
            Utilities.getConfig().setParameter(this.propertyName, value);
        }
    }
}
