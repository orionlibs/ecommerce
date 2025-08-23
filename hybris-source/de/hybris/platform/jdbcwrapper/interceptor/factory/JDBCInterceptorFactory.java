package de.hybris.platform.jdbcwrapper.interceptor.factory;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;

public interface JDBCInterceptorFactory
{
    JDBCInterceptor createJDBCInterceptor(Tenant paramTenant);


    static JDBCInterceptor create(Tenant tenant)
    {
        JDBCInterceptorFactory configuredFactory = getConfiguredFactory(tenant);
        return configuredFactory.createJDBCInterceptor(tenant);
    }


    static JDBCInterceptorFactory getConfiguredFactory(Tenant tenant)
    {
        if(!tenant.getConfig().getBoolean("jdbc.interceptor.enabled", true))
        {
            return (JDBCInterceptorFactory)new PassThroughInterceptor();
        }
        String configuredFactoryClass = tenant.getConfig().getString("jdbc.interceptor.factory.class", DefaultJDBCInterceptorFactory.class
                        .getName());
        try
        {
            return (JDBCInterceptorFactory)Class.forName(configuredFactoryClass).newInstance();
        }
        catch(InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            throw new IllegalStateException("Failed to instantiate '" + configuredFactoryClass + "' as JDBCInterceptorFactory.", e);
        }
    }
}
