package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.tools.MappingProviderSettings;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HybrisDatabaseSettingsFactory
{
    private static final Logger LOG = Logger.getLogger(HybrisDatabaseSettingsFactory.class);
    private static final String NEWINIT_DB_COMMENT = "newinit.db.comment";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_TABLEPREFX = "db.tableprefix";
    private static final String DB_FROM_JNDI = "db.pool.fromJNDI";
    private static final String JNDI_DB_TYPE = "db.pool.fromJNDI.dbtype";
    private final PropertiesLoader propertiesLoader;


    public HybrisDatabaseSettingsFactory(PropertiesLoader propertiesLoader)
    {
        this.propertiesLoader = propertiesLoader;
    }


    public DatabaseSettings createDatabaseSettings()
    {
        DataBaseProvider dataBaseProvider = getDataBaseProvider();
        return new DatabaseSettings(dataBaseProvider, this.propertiesLoader
                        .getProperty("db.url"), this.propertiesLoader
                        .getProperty("db.driver"), this.propertiesLoader
                        .getProperty("db.username"), this.propertiesLoader
                        .getProperty("db.password"), this.propertiesLoader
                        .getProperty("db.tableprefix"), this.propertiesLoader, this.propertiesLoader
                        .getProperty("newinit.db.comment", ""));
    }


    private DataBaseProvider getDataBaseProvider()
    {
        if(isDatasourceFromJNDI())
        {
            return getDataBaseProviderFromJNDIDatasourceType();
        }
        return guessDataBaseProviderFromDbUrl();
    }


    private boolean isDatasourceFromJNDI()
    {
        return !StringUtils.isEmpty(this.propertiesLoader.getProperty("db.pool.fromJNDI"));
    }


    private DataBaseProvider getDataBaseProviderFromJNDIDatasourceType()
    {
        String dbType = Objects.<String>requireNonNull(this.propertiesLoader.getProperty("db.pool.fromJNDI.dbtype"), "db.pool.fromJNDI.dbtype must be set.");
        for(DataBaseProvider candidate : DataBaseProvider.values())
        {
            if(dbType.equals(candidate.getDbName()))
            {
                return candidate;
            }
        }
        String errorMessage = "dbtype " + dbType + " is not supported";
        LOG.error(errorMessage);
        throw new IllegalStateException(errorMessage);
    }


    private DataBaseProvider guessDataBaseProviderFromDbUrl()
    {
        String url = this.propertiesLoader.getProperty("db.url");
        MappingProviderSettings.IS_CHAR_2_CHAR_MAPPING_ENABLED = !Boolean.parseBoolean(this.propertiesLoader.getProperty("db.mapping.char.legacy", "true"));
        return DDLGeneratorUtils.guessDatabaseFromURL(url);
    }
}
