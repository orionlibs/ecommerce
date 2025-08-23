package de.hybris.platform.jdbcwrapper.interceptor;

public interface JDBCInterceptorContext
{
    static JDBCInterceptorContext unknown()
    {
        return UnknownJDBCInterceptorContext.UNKNOWN;
    }


    static JDBCInterceptorContext forMethod(Class<?> clazz, String methodName)
    {
        return (JDBCInterceptorContext)new MethodJDBCInterceptorContext(clazz, methodName);
    }


    boolean matchesMethod(Class<?> paramClass, String paramString);
}
