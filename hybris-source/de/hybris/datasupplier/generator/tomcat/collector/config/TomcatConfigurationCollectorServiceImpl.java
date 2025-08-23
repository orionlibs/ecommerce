package de.hybris.datasupplier.generator.tomcat.collector.config;

import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import com.sap.sup.admin.sldsupplier.error.ConfigurationDataCollectException;
import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import com.sap.sup.admin.sldsupplier.error.SLDDataSupplierTechnicalException;
import de.hybris.datasupplier.generator.tomcat.collector.BaseTomcatCollectorServiceImpl;
import de.hybris.datasupplier.generator.tomcat.collector.TomcatSLDDataCollector;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfigurationLogger;
import org.apache.log4j.Logger;

public class TomcatConfigurationCollectorServiceImpl extends BaseTomcatCollectorServiceImpl implements TomcatConfigurationCollectorService
{
    private static final Logger LOG = Logger.getLogger(TomcatConfigurationCollectorServiceImpl.class);


    public String toString()
    {
        return "TomcatConfigurationCollectorServiceImpl";
    }


    public TomcatConfiguration collectConfiguration() throws ConfigurationDataCollectException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("ATLDDS01300) start collecting Tomcat configuration");
        }
        TomcatConfigurationCollectorImpl configurationCollector = createConfigurationCollector();
        SLDSupplierConfiguration configuration = getCollectorContext().getSLDSupplierConfiguration();
        String logicalHostName = configuration.getHostName();
        if(logicalHostName != null)
        {
            configurationCollector.setLocalHostName(logicalHostName);
        }
        try
        {
            configurationCollector.collect();
        }
        catch(InfrastructureException e)
        {
            throw new ConfigurationDataCollectException(e);
        }
        catch(SecurityException e)
        {
            throw new ConfigurationDataCollectException(e);
        }
        catch(Exception e)
        {
            throw new SLDDataSupplierTechnicalException(e);
        }
        TomcatConfiguration tomcatConfiguration = configurationCollector.getTomcatConfiguration();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("  [ConfigurationCollectorService] Collected Tomcat configuration data: " + tomcatConfiguration);
        }
        if(LOG.isDebugEnabled())
        {
            TomcatConfigurationLogger logger = new TomcatConfigurationLogger();
            LOG.debug("TomcatConfiguration SLD content: ");
            LOG.debug(logger.toSLDLog(tomcatConfiguration));
        }
        return tomcatConfiguration;
    }


    protected TomcatConfigurationCollectorImpl createConfigurationCollector()
    {
        TomcatConfigurationCollectorImpl collector = new TomcatConfigurationCollectorImpl();
        initializeCollector((TomcatSLDDataCollector)collector);
        return collector;
    }
}
