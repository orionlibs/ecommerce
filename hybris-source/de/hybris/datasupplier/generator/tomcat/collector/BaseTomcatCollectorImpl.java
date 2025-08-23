package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;

public abstract class BaseTomcatCollectorImpl implements TomcatCollectorServiceInitializable, TomcatSLDDataCollector
{
    private CollectorContext collectorContext;
    private TomcatCollectorServiceInitializer serviceInitializer;


    public CollectorContext getCollectorContext()
    {
        return this.collectorContext;
    }


    public void setCollectorContext(CollectorContext collectorContext)
    {
        this.collectorContext = collectorContext;
    }


    public TomcatCollectorServiceInitializer getServiceInitializer()
    {
        return this.serviceInitializer;
    }


    public void setServiceInitializer(TomcatCollectorServiceInitializer serviceInitializer)
    {
        this.serviceInitializer = serviceInitializer;
    }


    public final int getVerbose()
    {
        if(this.collectorContext == null)
        {
            throw new IllegalArgumentException("CollextorContext is null");
        }
        return this.collectorContext.getVerbose();
    }
}
