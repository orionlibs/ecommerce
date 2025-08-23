package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import com.sap.sup.admin.sldsupplier.collector.SLDDataCollector;

public interface TomcatSLDDataCollector extends SLDDataCollector
{
    CollectorContext getCollectorContext();


    void setCollectorContext(CollectorContext paramCollectorContext);


    void setServiceInitializer(TomcatCollectorServiceInitializer paramTomcatCollectorServiceInitializer);


    TomcatCollectorServiceInitializer getServiceInitializer();
}
