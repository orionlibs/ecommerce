package de.hybris.platform.jdbcwrapper.interceptor;

final class UnknownJDBCInterceptorContext implements JDBCInterceptorContext
{
    static final JDBCInterceptorContext UNKNOWN = new UnknownJDBCInterceptorContext();


    public boolean matchesMethod(Class<?> class1, String string)
    {
        return false;
    }
}
