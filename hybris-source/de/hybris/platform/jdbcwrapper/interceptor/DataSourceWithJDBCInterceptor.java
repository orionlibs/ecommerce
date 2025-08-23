package de.hybris.platform.jdbcwrapper.interceptor;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;
import javax.sql.DataSource;

class DataSourceWithJDBCInterceptor implements DataSource
{
    private final DataSource target;
    private final JDBCInterceptor jdbcInterceptor;


    public DataSourceWithJDBCInterceptor(DataSource target, JDBCInterceptor jdbcInterceptor)
    {
        this.target = Objects.<DataSource>requireNonNull(target, "target mustn't be null.");
        this.jdbcInterceptor = Objects.<JDBCInterceptor>requireNonNull(jdbcInterceptor, "jdbcInterceptor mustn't be null.");
    }


    public PrintWriter getLogWriter() throws SQLException
    {
        return (PrintWriter)this.jdbcInterceptor.get(() -> this.target.getLogWriter());
    }


    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        if(iface.isAssignableFrom(getClass()))
        {
            return iface.cast(this);
        }
        if(iface.isAssignableFrom(this.target.getClass()))
        {
            return iface.cast(this.target);
        }
        return (T)this.jdbcInterceptor.get(() -> this.target.unwrap(iface));
    }


    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return (iface.isAssignableFrom(getClass()) || ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isWrapperFor(iface)))).booleanValue());
    }


    public void setLogWriter(PrintWriter out) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setLogWriter(out));
    }


    public Connection getConnection() throws SQLException
    {
        return (Connection)this.jdbcInterceptor.get(JDBCInterceptorContext.forMethod(DataSource.class, "getConnection"), () -> this.target.getConnection());
    }


    public void setLoginTimeout(int seconds) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setLoginTimeout(seconds));
    }


    public Connection getConnection(String username, String password) throws SQLException
    {
        return (Connection)this.jdbcInterceptor.get(JDBCInterceptorContext.forMethod(DataSource.class, "getConnection"), () -> this.target.getConnection(username, password));
    }


    public int getLoginTimeout() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getLoginTimeout()))).intValue();
    }


    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        try
        {
            return (Logger)this.jdbcInterceptor.get(() -> this.target.getParentLogger());
        }
        catch(SQLFeatureNotSupportedException e)
        {
            throw e;
        }
        catch(SQLException e)
        {
            throw new SQLFeatureNotSupportedException(e);
        }
    }
}
