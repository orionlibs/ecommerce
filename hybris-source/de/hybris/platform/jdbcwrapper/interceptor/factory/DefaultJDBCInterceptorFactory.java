package de.hybris.platform.jdbcwrapper.interceptor.factory;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;
import de.hybris.platform.jdbcwrapper.interceptor.recover.DefaultSQLRecoveryStrategy;
import de.hybris.platform.jdbcwrapper.interceptor.recover.SQLRecoverableExceptionHandler;
import de.hybris.platform.jdbcwrapper.interceptor.recover.SQLRecoveryStrategy;

public class DefaultJDBCInterceptorFactory implements JDBCInterceptorFactory
{
    public JDBCInterceptor createJDBCInterceptor(Tenant tenant)
    {
        SQLRecoveryStrategy sqlRecoveryStrategy = createSQLRecoveryStrategy(tenant);
        return (JDBCInterceptor)new SQLRecoverableExceptionHandler(sqlRecoveryStrategy);
    }


    protected SQLRecoveryStrategy createSQLRecoveryStrategy(Tenant tenant)
    {
        return (SQLRecoveryStrategy)new DefaultSQLRecoveryStrategy(tenant.getConfig());
    }
}
