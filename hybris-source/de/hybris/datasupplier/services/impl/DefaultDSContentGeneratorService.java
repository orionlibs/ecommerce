package de.hybris.datasupplier.services.impl;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import com.sap.sup.admin.sldsupplier.error.SLDDataSupplierApplicationException;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import de.hybris.datasupplier.exceptions.DSContentGenerationException;
import de.hybris.datasupplier.generator.tomcat.collector.TomcatCollectorServiceFactory;
import de.hybris.datasupplier.generator.tomcat.collector.TomcatSLDDataSupplierService;
import de.hybris.datasupplier.generator.tomcat.data.DatabaseComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatSLDData;
import de.hybris.datasupplier.generator.tomcat.gen.TomcatSLDContentGenerator;
import de.hybris.datasupplier.services.DSConfigurationService;
import de.hybris.datasupplier.services.DSContentGeneratorService;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultDSContentGeneratorService implements DSContentGeneratorService
{
    private static final String SYSTEM_ID_REGEX = "^[A-Z\\d]{3}[A-Z\\d_]{0,5}$";
    private static final Logger LOG = Logger.getLogger(DefaultDSContentGeneratorService.class);
    private DSConfigurationService configurationService;


    public String generateContent() throws DSContentGenerationException
    {
        TomcatSLDData sldData;
        SLDSupplierConfiguration sldSldSupplierConfiguration = this.configurationService.getSLDSldSupplierConfiguration();
        validateConfiguration(sldSldSupplierConfiguration);
        try
        {
            sldData = collectSLDData(sldSldSupplierConfiguration);
        }
        catch(Exception e)
        {
            throw new DSContentGenerationException(e);
        }
        try
        {
            TomcatSLDContentGenerator contentGenerator = new TomcatSLDContentGenerator();
            contentGenerator.setConfiguration(sldSldSupplierConfiguration);
            return contentGenerator.generateSLD(sldData);
        }
        catch(Exception e)
        {
            throw new DSContentGenerationException(e);
        }
        finally
        {
        }
    }


    public void setConfigurationService(DSConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected void validateConfiguration(SLDSupplierConfiguration configuration) throws DSContentGenerationException
    {
        if(StringUtils.isEmpty(configuration.getSystemId()) || !Pattern.matches("^[A-Z\\d]{3}[A-Z\\d_]{0,5}$", configuration.getSystemId()))
        {
            throw new DSContentGenerationException("com.sap.sup.admin.sldsupplier.SYSTEM_ID value:" + configuration.getSystemId() + " doesn't match ^[A-Z\\d]{3}[A-Z\\d_]{0,5}$");
        }
    }


    protected TomcatSLDData collectSLDData(SLDSupplierConfiguration configuration) throws SLDDataSupplierApplicationException
    {
        TomcatCollectorServiceFactory factory = TomcatCollectorServiceFactory.createInstance();
        CollectorContext collectorContext = createCollectorContext(configuration);
        TomcatSLDDataSupplierService service = factory.createSLDSupplierService(collectorContext);
        service.init();
        TomcatSLDData sldData = null;
        try
        {
            TomcatConfiguration tomcatConfiguration = service.collectTomcatConfiguration();
            ProductSoftwareComponentDeployments productSoftwareComponentDeployments = service.collectProductSoftwareComponenDeployments(tomcatConfiguration);
            DatabaseComponentDeployment databaseDeployment = service.collectDatabaseComponentDeployment(tomcatConfiguration);
            sldData = new TomcatSLDData(tomcatConfiguration, productSoftwareComponentDeployments, databaseDeployment);
        }
        finally
        {
            service.destroy();
        }
        return sldData;
    }


    protected static CollectorContext createCollectorContext(SLDSupplierConfiguration configuration)
    {
        CollectorContext context = new CollectorContext();
        context.setSLDSupplierConfiguration(configuration);
        boolean securityEnabled = configuration.getUseSecurityProxy();
        context.setSecurityProxyEnabled(securityEnabled);
        String userName = configuration.getUserName();
        context.setUserName(userName);
        int verboseLevel = configuration.getVerboseLevel();
        context.setVerbose(verboseLevel);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("CollectorContext created. collectorSecurityEnabled = " + securityEnabled);
        }
        return context;
    }
}
