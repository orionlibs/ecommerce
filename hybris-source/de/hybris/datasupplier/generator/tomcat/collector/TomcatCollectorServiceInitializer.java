package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import org.apache.log4j.Logger;

public class TomcatCollectorServiceInitializer
{
    private static final Logger LOG = Logger.getLogger(TomcatCollectorServiceInitializer.class.getName());
    private MBeanServer mbeanServer;
    private boolean initialized = false;


    public boolean isInitialized()
    {
        return this.initialized;
    }


    public MBeanServer getMBeanServer()
    {
        return this.mbeanServer;
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Initializing Tomcat JMX API objects");
        }
        this.mbeanServer = initMBeanServer();
        this.initialized = true;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Tomcat JMX API objects initialized");
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Destroy");
        }
    }


    protected MBeanServer initMBeanServer()
    {
        MBeanServer server = null;
        List<MBeanServer> myList = MBeanServerFactory.findMBeanServer(null);
        if((myList.toArray()).length > 0)
        {
            server = (MBeanServer)myList.toArray()[0];
        }
        if(server == null)
        {
            throw new InfrastructureException("Could not get MBeanServer instance");
        }
        return server;
    }


    public void setMBeanServer(MBeanServer mbeanServer)
    {
        this.mbeanServer = mbeanServer;
    }
}
