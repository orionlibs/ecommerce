package de.hybris.bootstrap.ddl;

import javax.sql.DataSource;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;

public class HybrisPlatformFactory
{
    public static final String ORACLE_STATEMENT_DELIMITER = "-- hybris comment";
    public static final String DEFAULT_STATEMENT_DELIMITER = ";";

    static
    {
        PlatformFactory.registerPlatform("MySQL5", HybrisMySqlPlatform.class);
        PlatformFactory.registerPlatform("MySQL", HybrisMySqlPlatform.class);
        PlatformFactory.registerPlatform("HsqlDb", HybrisHsqlDbPlatform.class);
        PlatformFactory.registerPlatform("Oracle10", HybrisOraclePlatform.class);
        PlatformFactory.registerPlatform("Oracle9", HybrisOraclePlatform.class);
        PlatformFactory.registerPlatform("Oracle", HybrisOraclePlatform.class);
        PlatformFactory.registerPlatform("MsSql", HybrisMSSqlPlatform.class);
        PlatformFactory.registerPlatform("sap", HybrisHanaPlatform.class);
        PlatformFactory.registerPlatform("PostgreSql", HybrisPostgreSqlPlatform.class);
    }

    private static Platform platform = null;


    public static Platform createInstance(DatabaseSettings databaseSettings) throws DdlUtilsException
    {
        if(dbSettingsSupportsProvider(databaseSettings, DataBaseProvider.MYSQL))
        {
            databaseSettings.setStatementDelimiter(";");
            platform = (Platform)HybrisMySqlPlatform.build(databaseSettings);
        }
        else if(dbSettingsSupportsProvider(databaseSettings, DataBaseProvider.HSQL))
        {
            databaseSettings.setStatementDelimiter(";");
            platform = (Platform)HybrisHsqlDbPlatform.build(databaseSettings);
        }
        else if(dbSettingsSupportsProvider(databaseSettings, DataBaseProvider.ORACLE))
        {
            databaseSettings.setStatementDelimiter("-- hybris comment");
            platform = (Platform)HybrisOraclePlatform.build(databaseSettings);
        }
        else if(dbSettingsSupportsProvider(databaseSettings, DataBaseProvider.MSSQL))
        {
            databaseSettings.setStatementDelimiter(";");
            platform = (Platform)HybrisMSSqlPlatform.build(databaseSettings);
        }
        else if(dbSettingsSupportsProvider(databaseSettings, DataBaseProvider.HANA))
        {
            databaseSettings.setStatementDelimiter(";");
            platform = (Platform)HybrisHanaPlatform.build(databaseSettings);
        }
        else if(dbSettingsSupportsProvider(databaseSettings, DataBaseProvider.POSTGRESQL))
        {
            databaseSettings.setStatementDelimiter(";");
            platform = (Platform)HybrisPostgreSqlPlatform.build(databaseSettings);
        }
        else
        {
            throw new IllegalArgumentException("unsupported database type " + databaseSettings.getDataBaseProvider());
        }
        return platform;
    }


    private static boolean dbSettingsSupportsProvider(DatabaseSettings settings, DataBaseProvider provider)
    {
        return (settings.getDataBaseProvider() == provider);
    }


    public static Platform createInstance(DatabaseSettings databaseSettings, DataSource dataSource) throws DdlUtilsException
    {
        Platform platform = createInstance(databaseSettings);
        platform.setDataSource(dataSource);
        return platform;
    }


    public static synchronized Platform getInstance()
    {
        if(platform == null)
        {
            throw new IllegalStateException("Platform is not initialized. Call createInstance method first!");
        }
        return platform;
    }
}
