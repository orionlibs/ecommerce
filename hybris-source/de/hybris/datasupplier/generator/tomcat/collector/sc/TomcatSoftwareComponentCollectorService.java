package de.hybris.datasupplier.generator.tomcat.collector.sc;

import com.sap.sup.admin.sldsupplier.error.SoftwareComponentDataCollectException;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;

public interface TomcatSoftwareComponentCollectorService
{
    void init();


    ProductSoftwareComponentDeployments collectSoftwareComponents(TomcatConfiguration paramTomcatConfiguration) throws SoftwareComponentDataCollectException;


    void destroy();
}
