package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.DataSourceFactory;
import de.hybris.platform.core.SlaveTenantDataSourceFactory;
import de.hybris.platform.core.Tenant;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SlaveTenantDataSource extends DataSourceImpl
{
    public SlaveTenantDataSource(Tenant tenant, String id, Map<String, String> connectionParams, boolean readOnly, SlaveTenantDataSourceFactory factory)
    {
        super(tenant, id, connectionParams, readOnly, (DataSourceFactory)factory);
    }


    public Connection getConnection(boolean transactionBound) throws SQLException
    {
        return doGetConnection(transactionBound);
    }
}
