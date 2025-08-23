package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.jdbc.HSSQLPlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.jdbc.HanaPlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.jdbc.MSSQLPlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.jdbc.OraclePlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.jdbc.PlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.jdbc.PostgreSQLPlatformJDBCMappingProvider;

public enum DataBaseProvider
{
    MYSQL("mysql", new PlatformJDBCMappingProvider()),
    HSQL("hsqldb", (PlatformJDBCMappingProvider)new HSSQLPlatformJDBCMappingProvider()),
    ORACLE("oracle", (PlatformJDBCMappingProvider)new OraclePlatformJDBCMappingProvider()),
    MSSQL("sqlserver", (PlatformJDBCMappingProvider)new MSSQLPlatformJDBCMappingProvider()),
    HANA("sap", (PlatformJDBCMappingProvider)new HanaPlatformJDBCMappingProvider()),
    POSTGRESQL("postgresql", (PlatformJDBCMappingProvider)new PostgreSQLPlatformJDBCMappingProvider());
    private final String dbName;
    private final PlatformJDBCMappingProvider jdbcProvider;


    DataBaseProvider(String name, PlatformJDBCMappingProvider jdbcProvider)
    {
        this.dbName = name;
        this.jdbcProvider = jdbcProvider;
    }


    public String getDbName()
    {
        return this.dbName;
    }


    public PlatformJDBCMappingProvider getJdbcProvider()
    {
        return this.jdbcProvider;
    }


    public String getTableName(String tableName)
    {
        return tableName;
    }


    public boolean isMySqlUsed()
    {
        return MSSQL.getDbName().equalsIgnoreCase(getDbName());
    }


    public boolean isHSqlUsed()
    {
        return HSQL.getDbName().equalsIgnoreCase(getDbName());
    }


    public boolean isOracleUsed()
    {
        return ORACLE.getDbName().equalsIgnoreCase(getDbName());
    }


    public boolean isMssqlUsed()
    {
        return MSSQL.getDbName().equalsIgnoreCase(getDbName());
    }


    public boolean isHanaUsed()
    {
        return HANA.getDbName().equalsIgnoreCase(getDbName());
    }


    public boolean isPostgreSqlUsed()
    {
        return POSTGRESQL.getDbName().equalsIgnoreCase(getDbName());
    }
}
