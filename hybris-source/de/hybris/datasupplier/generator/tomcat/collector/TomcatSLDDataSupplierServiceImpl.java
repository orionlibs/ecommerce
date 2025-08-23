package de.hybris.datasupplier.generator.tomcat.collector;

import com.sap.sup.admin.sldsupplier.collector.AppServerConfiguration;
import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import com.sap.sup.admin.sldsupplier.error.ConfigurationDataCollectException;
import com.sap.sup.admin.sldsupplier.error.SoftwareComponentDataCollectException;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import de.hybris.datasupplier.generator.hybris.collector.sc.DatabaseComponentCollectorService;
import de.hybris.datasupplier.generator.tomcat.collector.config.TomcatConfigurationCollectorService;
import de.hybris.datasupplier.generator.tomcat.collector.sc.TomcatSoftwareComponentCollectorService;
import de.hybris.datasupplier.generator.tomcat.data.DatabaseComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import org.apache.log4j.Logger;

public class TomcatSLDDataSupplierServiceImpl extends BaseTomcatCollectorServiceImpl implements TomcatSLDDataSupplierService
{
    private static final Logger LOG = Logger.getLogger(TomcatSLDDataSupplierServiceImpl.class);
    private TomcatConfigurationCollectorService configurationService;
    private TomcatSoftwareComponentCollectorService softwareComponentService;
    private DatabaseComponentCollectorService databaseComponentService;


    protected void doInit()
    {
        TomcatCollectorServiceFactory factory = getFactory();
        CollectorContext collectorContext = getCollectorContext();
        this.configurationService = factory.createConfigurationCollectorService(collectorContext);
        this.configurationService.init();
        this.softwareComponentService = factory.createSoftwareComponentCollectorService(collectorContext);
        this.softwareComponentService.init();
        this.databaseComponentService = factory.cerateDatabaseComponentCollectorService(collectorContext);
        this.databaseComponentService.init();
    }


    public AppServerConfiguration collectAppServerConfiguration() throws ConfigurationDataCollectException
    {
        return (AppServerConfiguration)collectTomcatConfiguration();
    }


    public TomcatConfiguration collectTomcatConfiguration() throws ConfigurationDataCollectException
    {
        TomcatConfiguration tomcatConfiguration = this.configurationService.collectConfiguration();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("collected TomcatConfiguration [" + tomcatConfiguration + "]");
        }
        return tomcatConfiguration;
    }


    public ProductSoftwareComponentDeployments collectProductSoftwareComponenDeployments(AppServerConfiguration serverConfiguration) throws SoftwareComponentDataCollectException
    {
        return collectProductSoftwareComponenDeployments((TomcatConfiguration)serverConfiguration);
    }


    public ProductSoftwareComponentDeployments collectProductSoftwareComponenDeployments(TomcatConfiguration tomcatConfiguration) throws SoftwareComponentDataCollectException
    {
        ProductSoftwareComponentDeployments productSoftwareDeployments = this.softwareComponentService.collectSoftwareComponents(tomcatConfiguration);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" collected [" + productSoftwareDeployments + "] Product/Software component objects");
        }
        return productSoftwareDeployments;
    }


    public DatabaseComponentDeployment collectDatabaseComponentDeployment(TomcatConfiguration tomcatConfiguration)
    {
        DatabaseComponentDeployment databaseDeployment = this.databaseComponentService.collectDatabaseComponents(tomcatConfiguration);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" collected [" + databaseDeployment + "] Database component objects");
        }
        return databaseDeployment;
    }


    protected void doDestroy()
    {
        if(this.configurationService != null)
        {
            this.configurationService.destroy();
        }
    }
}
