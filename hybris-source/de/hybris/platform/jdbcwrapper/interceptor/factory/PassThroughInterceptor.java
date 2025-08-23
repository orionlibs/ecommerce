package de.hybris.platform.jdbcwrapper.interceptor.factory;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptorContext;
import java.sql.SQLException;

class PassThroughInterceptor implements JDBCInterceptor, JDBCInterceptorFactory
{
    public <T> T get(JDBCInterceptorContext ctx, JDBCInterceptor.SupplierWithSQLException<T> supplier) throws SQLException
    {
        return (T)supplier.get();
    }


    public JDBCInterceptor createJDBCInterceptor(Tenant tenant)
    {
        return this;
    }
}
