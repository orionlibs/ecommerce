package de.hybris.platform.servicelayer.stats.collector_impl;

import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.log4j.Logger;

public class HTTPSessionCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    private MBeanServer mServer;
    private final String webroot;
    private ObjectName objectName;
    private boolean enabled;
    private static final Logger LOG = Logger.getLogger(HTTPSessionCollector.class);
    private final long timestamp;


    public HTTPSessionCollector(String name, String label, String webroot, String color)
    {
        super(name, label, color);
        this.webroot = webroot;
        this.timestamp = System.currentTimeMillis();
        try
        {
            this.mServer = ManagementFactory.getPlatformMBeanServer();
            this.objectName = new ObjectName("Catalina:type=Manager,path=" + webroot + ",host=localhost");
            this.enabled = true;
        }
        catch(MalformedObjectNameException e)
        {
            this.objectName = null;
            this.mServer = null;
            this.enabled = false;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Unbale to create ObjectName for webroot '" + webroot + "'.");
            }
        }
    }


    public float collect()
    {
        float result = -1.0F;
        if(!this.enabled)
        {
            return result;
        }
        if(System.currentTimeMillis() - this.timestamp > 100000L)
        {
            try
            {
                result = ((Integer)this.mServer.getAttribute(this.objectName, "activeSessions")).floatValue();
            }
            catch(Exception e)
            {
                this.enabled = false;
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Unable to collect active Sessions for webroot [" + this.webroot + "]. Collecting data stopped for this collector.");
                }
            }
        }
        return result;
    }


    public boolean evaluateValue(float value)
    {
        return (value > 0.0F);
    }
}
