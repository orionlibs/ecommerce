package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.DataSourceFactory;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;

public interface HybrisDataSource extends DataSource
{
    DataSourceFactory getDataSourceFactory();


    String getDriverVersion();


    Map<String, String> getConnectionParameters();


    String getJNDIName();


    String getID();


    JDBCConnectionPool getConnectionPool();


    Tenant getTenant();


    Connection getConnection(boolean paramBoolean) throws SQLException;


    void returnToPool(ConnectionImpl paramConnectionImpl);


    void invalidate(ConnectionImpl paramConnectionImpl);


    void destroy();


    int getNumInUse();


    int getNumPhysicalOpen();


    default int getNumReadOnlyOpen()
    {
        return 0;
    }


    int getMaxInUse();


    int getMaxPhysicalOpen();


    long totalGets();


    int getMaxAllowedPhysicalOpen();


    long getMillisWaitedForConnection();


    void resetStats();


    JDBCLogUtils getLogUtils();


    String getDatabaseName();


    int getMaxPreparedParameterCount();


    String getDatabaseVersion();


    String getDatabaseURL();


    String getCustomSessionSQL();


    String getDatabaseUser();


    String getSchemaName();


    boolean cannotConnect();


    boolean isDBLogActive();


    boolean isDBLogAppendStackTraceActive();


    void setDBLog(boolean paramBoolean);


    void setDBLogAppendStackTrace(boolean paramBoolean);


    boolean isReadOnly();


    String getTablePrefix();


    DataAccessException translateToDataAccessException(SQLException paramSQLException);


    JDBCInterceptor getJDBCInterceptor();
}
