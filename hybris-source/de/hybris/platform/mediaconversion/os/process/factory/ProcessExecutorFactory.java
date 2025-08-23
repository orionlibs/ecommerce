package de.hybris.platform.mediaconversion.os.process.factory;

import de.hybris.platform.core.Registry;
import de.hybris.platform.mediaconversion.os.OsConfigurationService;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.ProcessContextRegistry;
import de.hybris.platform.mediaconversion.os.process.RMIRegistryService;
import de.hybris.platform.mediaconversion.os.process.impl.EmbeddedProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.impl.LimitedProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.impl.ProcessExecutorClient;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ProcessExecutorFactory
{
    private static final Logger LOG = Logger.getLogger(ProcessExecutorFactory.class);
    public static final String CONFIGURATION_KEY_PREFIX = "os.processexecutor";
    private int limit;
    private OsConfigurationService osConfigurationService;
    private ConfigurationService configurationService;
    private RMIRegistryService rmiRegistryService;
    private ProcessContextRegistry processContextRegistry;
    private final Collection<ProcessExecutor> processExecutors = new LinkedList<>();


    public ProcessExecutor create()
    {
        LimitedProcessExecutor limitedProcessExecutor;
        ProcessExecutor processExecutor = createOSSpecific();
        if(getLimit() > 0)
        {
            limitedProcessExecutor = new LimitedProcessExecutor(getLimit(), processExecutor);
        }
        synchronized(this)
        {
            this.processExecutors.add(limitedProcessExecutor);
        }
        return (ProcessExecutor)limitedProcessExecutor;
    }


    public void destroy()
    {
        synchronized(this)
        {
            try
            {
                for(ProcessExecutor pexi : this.processExecutors)
                {
                    try
                    {
                        pexi.quit();
                    }
                    catch(IOException e)
                    {
                        LOG.error("Failed to terminate process executor.", e);
                    }
                }
            }
            finally
            {
                this.processExecutors.clear();
            }
        }
    }


    protected ProcessExecutor createOSSpecific()
    {
        String flavor = retrieveFlavor();
        try
        {
            return create(flavor);
        }
        catch(IllegalArgumentException e)
        {
            LOG.error("Invalid process executor flavor '" + flavor + "' specified. Falling back to 'embedded'.", e);
            return (ProcessExecutor)new EmbeddedProcessExecutor();
        }
    }


    private String retrieveFlavor()
    {
        String flavor = getOsConfigurationService().retrieveOsSpecificProperty("os.processexecutor", Flavor.embedded
                        .name());
        LOG.debug("Using '" + flavor + "' process executor.");
        return flavor;
    }


    protected ProcessExecutor create(String flavor)
    {
        if(Flavor.embedded.name().equals(flavor))
        {
            return (ProcessExecutor)new EmbeddedProcessExecutor();
        }
        if(Flavor.rmi.name().equals(flavor))
        {
            try
            {
                return (ProcessExecutor)new ProcessExecutorClient(getConfigurationService().getConfiguration(), getRmiRegistryService(),
                                getProcessContextRegistry(), getTenantId());
            }
            catch(IOException e)
            {
                throw new ProcessExecutorCreationException("Failed to create rmi process executor.", e);
            }
            catch(NotBoundException e)
            {
                throw new ProcessExecutorCreationException("Failed to access rmi stub.", e);
            }
        }
        throw new IllegalArgumentException("Unrecognized process executor flavor '" + flavor + "'.");
    }


    protected String getTenantId()
    {
        return Registry.getCurrentTenant().getTenantID();
    }


    public int getLimit()
    {
        return this.limit;
    }


    public void setLimit(int limit)
    {
        boolean thereAreSome;
        synchronized(this)
        {
            thereAreSome = !this.processExecutors.isEmpty();
        }
        if(thereAreSome)
        {
            LOG.warn("ProcessExecutor already created. Limit cannot be reset.");
        }
        this.limit = limit;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public RMIRegistryService getRmiRegistryService()
    {
        return this.rmiRegistryService;
    }


    @Required
    public void setRmiRegistryService(RMIRegistryService rmiRegistryService)
    {
        this.rmiRegistryService = rmiRegistryService;
    }


    public OsConfigurationService getOsConfigurationService()
    {
        return this.osConfigurationService;
    }


    @Required
    public void setOsConfigurationService(OsConfigurationService osConfigurationService)
    {
        this.osConfigurationService = osConfigurationService;
    }


    public ProcessContextRegistry getProcessContextRegistry()
    {
        return this.processContextRegistry;
    }


    @Required
    public void setProcessContextRegistry(ProcessContextRegistry processContextRegistry)
    {
        this.processContextRegistry = processContextRegistry;
    }
}
