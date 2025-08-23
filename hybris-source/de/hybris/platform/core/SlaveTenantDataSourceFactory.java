package de.hybris.platform.core;

import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jdbcwrapper.SlaveTenantDataSource;
import java.util.Map;

public class SlaveTenantDataSourceFactory extends DataSourceImplFactory
{
    public HybrisDataSource createDataSource(String id, Tenant tenant, Map<String, String> connectionParams, boolean readOnly)
    {
        return (HybrisDataSource)new SlaveTenantDataSource(tenant, id, connectionParams, readOnly, this);
    }
}
