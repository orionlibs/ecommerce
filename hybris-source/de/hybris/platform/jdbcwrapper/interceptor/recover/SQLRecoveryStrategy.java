package de.hybris.platform.jdbcwrapper.interceptor.recover;

import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptorContext;
import java.sql.SQLException;
import java.time.Duration;

public interface SQLRecoveryStrategy
{
    Duration calculateBackoffDuration(int paramInt);


    boolean isRecoverable(SQLException paramSQLException);


    boolean canRecover(JDBCInterceptorContext paramJDBCInterceptorContext);
}
