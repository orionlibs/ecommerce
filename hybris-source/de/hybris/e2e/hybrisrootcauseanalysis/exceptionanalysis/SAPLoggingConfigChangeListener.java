package de.hybris.e2e.hybrisrootcauseanalysis.exceptionanalysis;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SAPLoggingConfigChangeListener implements ConfigIntf.ConfigChangeListener
{
    private static final Logger LOG = LogManager.getLogger();
    @Resource
    private SAPLoggingProxyListener sapLoggingProxyListener;


    public void configChanged(String key, String value)
    {
        if(key.contains("e2e.rootcauseanalysis.exceptionanalysis.listlog."))
        {
            String[] exploded = key.split("\\.");
            String prop = exploded[exploded.length - 1];
            switch(prop)
            {
                case "logseverity":
                    this.sapLoggingProxyListener.setLogSeverity(value);
                    break;
                case "traceseverity":
                    this.sapLoggingProxyListener.setTraceSeverity(value);
                    break;
                case "enabletracing":
                    this.sapLoggingProxyListener.setEnableTracing(Boolean.parseBoolean(value));
                    break;
                case "enableevents":
                    this.sapLoggingProxyListener.setEnableEvents(Boolean.parseBoolean(value));
                    break;
                default:
                    LOG.error("The property '{}' is either invalid or not changeable at runtime", prop);
                    return;
            }
            LOG.info("ListLog property '{}' changed to '{}'", prop, value);
        }
    }


    public void init()
    {
        LOG.info("Registered the SAPLoggingConfigChangeListener");
        Registry.getCurrentTenant().getConfig().registerConfigChangeListener(this);
    }


    public SAPLoggingProxyListener getListLogLoggingListener()
    {
        return this.sapLoggingProxyListener;
    }


    public void setListLogLoggingListener(SAPLoggingProxyListener sapLoggingProxyListener)
    {
        this.sapLoggingProxyListener = sapLoggingProxyListener;
    }
}
