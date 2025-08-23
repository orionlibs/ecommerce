package de.hybris.datasupplier.services.impl;

import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import com.sap.sup.admin.sldsupplier.facade.BaseSLDDataSupplierConfigurationManager;
import de.hybris.datasupplier.services.DSConfigurationService;
import de.hybris.platform.util.Config;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class DefaultDSConfigurationService implements DSConfigurationService
{
    private static final Logger LOG = Logger.getLogger(DefaultDSConfigurationService.class);
    private SLDSupplierConfiguration sldSupplierConfiguration;


    public SLDSupplierConfiguration getSLDSldSupplierConfiguration()
    {
        return this.sldSupplierConfiguration;
    }


    public void initializeProperties()
    {
        try
        {
            BaseSLDDataSupplierConfigurationManager configManager = new BaseSLDDataSupplierConfigurationManager();
            Map<String, String> parametersByPattern = Config.getParametersByPattern("com.sap.sup.admin.sldsupplier");
            Properties props = new Properties();
            props.putAll(parametersByPattern);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            props.store(output, (String)null);
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            this.sldSupplierConfiguration = configManager.loadConfigurationFromStream(input);
        }
        catch(IOException e)
        {
            LOG.error("Couldn't load SLD Supplier configuration", e);
        }
    }
}
