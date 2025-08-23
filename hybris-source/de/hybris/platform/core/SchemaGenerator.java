package de.hybris.platform.core;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.DataSourceCreator;
import de.hybris.bootstrap.ddl.HybrisSchemaGenerator;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.io.File;

public class SchemaGenerator
{
    public static HybrisSchemaGenerator createSchemaGenerator(boolean init, boolean dryRun)
    {
        PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(Initialization.class);
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        try
        {
            HybrisSchemaGenerator generator = new HybrisSchemaGenerator(platformConfig, getPropertiesLoader(tenant), getDataSourceCreator(tenant), dryRun, tenant.getTenantID());
            File tempDir = platformConfig.getSystemConfig().getTempDir();
            String tenantID = tenant.getTenantID();
            ScriptPathProvider scriptPathProvider = new ScriptPathProvider(tempDir, init, tenantID);
            generator.setDdlDropFileName(scriptPathProvider.getPathToDdlDropFile());
            generator.setDdlFileName(scriptPathProvider.getPathToDdlFile());
            generator.setDmlFileName(scriptPathProvider.getPathToDmlFile());
            generator.setChangesFileName(scriptPathProvider.getPathToChangesFile());
            return generator;
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    private static DataSourceCreator getDataSourceCreator(Tenant tenant)
    {
        return (DataSourceCreator)new Object(tenant);
    }


    private static PropertiesLoader getPropertiesLoader(Tenant tenant)
    {
        return (PropertiesLoader)new TenantPropertiesLoader(tenant);
    }
}
