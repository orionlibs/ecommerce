package de.hybris.platform.jdbcwrapper.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import javax.sql.DataSource;

public interface JDBCInterceptor
{
    default <T> T wrap(T target, Class<T> jdbcInterface)
    {
        Objects.requireNonNull(target, "target mustn't be null.");
        Objects.requireNonNull(jdbcInterface, "jdbcInterface mustn't be null.");
        if(ResultSet.class.equals(jdbcInterface))
        {
            return (T)new ResultSetWithJDBCInterceptor((ResultSet)target, this);
        }
        if(PreparedStatement.class.equals(jdbcInterface))
        {
            return (T)new PreparedStatementWithJDBCInterceptor((PreparedStatement)target, this);
        }
        if(Statement.class.equals(jdbcInterface))
        {
            return (T)new StatementWithJDBCInterceptor((Statement)target, this);
        }
        if(Connection.class.equals(jdbcInterface))
        {
            return (T)new ConnectionWithJDBCInterceptor((Connection)target, this);
        }
        if(DataSource.class.equals(jdbcInterface))
        {
            return (T)new DataSourceWithJDBCInterceptor((DataSource)target, this);
        }
        throw new IllegalArgumentException(String.format("Couldn't find a wrapper for '%s'.", new Object[] {jdbcInterface}));
    }


    default void run(RunnableWithSQLException runnable) throws SQLException
    {
        Objects.requireNonNull(runnable, "runnable mustn't be null");
        get(JDBCInterceptorContext.unknown(), () -> {
            runnable.run();
            return null;
        });
    }


    default <T> T get(SupplierWithSQLException<T> supplier) throws SQLException
    {
        return get(JDBCInterceptorContext.unknown(), supplier);
    }


    <T> T get(JDBCInterceptorContext paramJDBCInterceptorContext, SupplierWithSQLException<T> paramSupplierWithSQLException) throws SQLException;
}
