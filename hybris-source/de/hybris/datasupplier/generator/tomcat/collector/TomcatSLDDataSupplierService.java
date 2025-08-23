package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.collector.SLDDataSupplierService;
import com.sap.sup.admin.sldsupplier.error.ConfigurationDataCollectException;
import com.sap.sup.admin.sldsupplier.error.SoftwareComponentDataCollectException;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import de.hybris.datasupplier.generator.tomcat.data.DatabaseComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;

public interface TomcatSLDDataSupplierService extends SLDDataSupplierService
{
    void init();


    TomcatConfiguration collectTomcatConfiguration() throws ConfigurationDataCollectException;


    ProductSoftwareComponentDeployments collectProductSoftwareComponenDeployments(TomcatConfiguration paramTomcatConfiguration) throws SoftwareComponentDataCollectException;


    DatabaseComponentDeployment collectDatabaseComponentDeployment(TomcatConfiguration paramTomcatConfiguration);


    void destroy();
}
