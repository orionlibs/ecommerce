package de.hybris.platform.core;

import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jdbcwrapper.JDBCConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public interface DataSourceFactory
{
    HybrisDataSource createJNDIDataSource(String paramString1, Tenant paramTenant, String paramString2, boolean paramBoolean);


    HybrisDataSource createDataSource(String paramString, Tenant paramTenant, Map<String, String> paramMap, boolean paramBoolean);


    JDBCConnectionPool createConnectionPool(HybrisDataSource paramHybrisDataSource, GenericObjectPoolConfig paramGenericObjectPoolConfig);


    Connection wrapConnection(HybrisDataSource paramHybrisDataSource, Connection paramConnection);


    Statement wrapStatement(Connection paramConnection, Statement paramStatement);


    PreparedStatement wrapPreparedStatement(Connection paramConnection, PreparedStatement paramPreparedStatement, String paramString);


    ResultSet wrapResultSet(Statement paramStatement, ResultSet paramResultSet);
}
