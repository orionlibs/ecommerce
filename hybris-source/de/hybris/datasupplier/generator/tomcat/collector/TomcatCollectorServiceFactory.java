package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import de.hybris.datasupplier.generator.hybris.collector.sc.DatabaseComponentCollectorService;
import de.hybris.datasupplier.generator.hybris.collector.sc.DatabaseComponentCollectorServiceImpl;
import de.hybris.datasupplier.generator.tomcat.collector.config.TomcatConfigurationCollectorService;
import de.hybris.datasupplier.generator.tomcat.collector.config.TomcatConfigurationCollectorServiceImpl;
import de.hybris.datasupplier.generator.tomcat.collector.sc.TomcatSoftwareComponentCollectorService;
import de.hybris.datasupplier.generator.tomcat.collector.sc.TomcatSoftwareComponentCollectorServiceImpl;

public class TomcatCollectorServiceFactory
{
    private final TomcatCollectorServiceInitializer serviceInitializer = new TomcatCollectorServiceInitializer();


    public static TomcatCollectorServiceFactory createInstance()
    {
        return new TomcatCollectorServiceFactory();
    }


    public TomcatSLDDataSupplierService createSLDSupplierService(CollectorContext collectorContext)
    {
        TomcatSLDDataSupplierServiceImpl service = new TomcatSLDDataSupplierServiceImpl();
        service.setCollectorContext(collectorContext);
        service.setServiceInitializer(this.serviceInitializer);
        service.setFactory(this);
        return (TomcatSLDDataSupplierService)service;
    }


    public TomcatConfigurationCollectorService createConfigurationCollectorService(CollectorContext collectorContext)
    {
        TomcatConfigurationCollectorServiceImpl service = new TomcatConfigurationCollectorServiceImpl();
        service.setCollectorContext(collectorContext);
        service.setServiceInitializer(this.serviceInitializer);
        service.setFactory(this);
        return (TomcatConfigurationCollectorService)service;
    }


    public TomcatSoftwareComponentCollectorService createSoftwareComponentCollectorService(CollectorContext collectorContext)
    {
        TomcatSoftwareComponentCollectorServiceImpl service = new TomcatSoftwareComponentCollectorServiceImpl();
        service.setCollectorContext(collectorContext);
        service.setServiceInitializer(this.serviceInitializer);
        service.setFactory(this);
        return (TomcatSoftwareComponentCollectorService)service;
    }


    public DatabaseComponentCollectorService cerateDatabaseComponentCollectorService(CollectorContext collectorContext)
    {
        DatabaseComponentCollectorServiceImpl service = new DatabaseComponentCollectorServiceImpl();
        service.setCollectorContext(collectorContext);
        service.setServiceInitializer(this.serviceInitializer);
        service.setFactory(this);
        return (DatabaseComponentCollectorService)service;
    }
}
