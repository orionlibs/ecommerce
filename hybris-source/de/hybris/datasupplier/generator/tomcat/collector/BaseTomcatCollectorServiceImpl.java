package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import org.apache.log4j.Logger;

public abstract class BaseTomcatCollectorServiceImpl implements TomcatCollectorServiceInitializable
{
    private static final Logger LOG = Logger.getLogger(BaseTomcatCollectorServiceImpl.class);
    private TomcatCollectorServiceInitializer serviceInitializer;
    private CollectorContext collectorContext;
    private TomcatCollectorServiceFactory factory;
    private static final boolean INITIALIZED = false;


    public boolean isInitialized()
    {
        return false;
    }


    public TomcatCollectorServiceInitializer getServiceInitializer()
    {
        return this.serviceInitializer;
    }


    public void setServiceInitializer(TomcatCollectorServiceInitializer serviceInitializer)
    {
        this.serviceInitializer = serviceInitializer;
    }


    public CollectorContext getCollectorContext()
    {
        return this.collectorContext;
    }


    public void setCollectorContext(CollectorContext collectorContext)
    {
        this.collectorContext = collectorContext;
    }


    public TomcatCollectorServiceFactory getFactory()
    {
        return this.factory;
    }


    public void setFactory(TomcatCollectorServiceFactory factory)
    {
        this.factory = factory;
    }


    public final int getVerbose()
    {
        if(this.collectorContext == null)
        {
            throw new IllegalArgumentException("CollextorContext is null");
        }
        return this.collectorContext.getVerbose();
    }


    public void init()
    {
        if(isInitialized())
        {
            return;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" Initializing collector service instance: " + LOG.getName());
        }
        if(this.collectorContext == null)
        {
            throw new IllegalArgumentException("CollectorContext is null");
        }
        if(this.factory == null)
        {
            throw new IllegalArgumentException("TomcatCollectorServicesFactory is null");
        }
        if(getServiceInitializer() == null)
        {
            throw new IllegalArgumentException("TomcatManagementServiceInitializer is null");
        }
        this.serviceInitializer.init();
        doInit();
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" Init finished:" + LOG.getName());
        }
    }


    protected void doInit()
    {
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" Destroying: " + LOG.getName());
        }
        doDestroy();
        if(getServiceInitializer() != null)
        {
            this.serviceInitializer.destroy();
        }
    }


    protected void doDestroy()
    {
    }


    protected void initializeCollector(TomcatSLDDataCollector collector)
    {
        collector.setCollectorContext(this.collectorContext);
        collector.setServiceInitializer(this.serviceInitializer);
    }
}
