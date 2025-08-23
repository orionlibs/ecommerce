package de.hybris.platform.jdbcwrapper.interceptor;

import java.util.Objects;

final class MethodJDBCInterceptorContext implements JDBCInterceptorContext
{
    private final Class<?> clazz;
    private final String methodName;


    public MethodJDBCInterceptorContext(Class<?> clazz, String methodName)
    {
        this.clazz = Objects.<Class<?>>requireNonNull(clazz, "clazz mustn't be null.");
        this.methodName = Objects.<String>requireNonNull(methodName, "methodName mustn't be null.");
    }


    public boolean matchesMethod(Class<?> clazz, String methodName)
    {
        return (this.clazz.equals(clazz) && this.methodName.equals(methodName));
    }
}
