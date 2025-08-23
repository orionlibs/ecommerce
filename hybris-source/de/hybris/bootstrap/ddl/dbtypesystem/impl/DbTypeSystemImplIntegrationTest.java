package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.DBCPDataSourceCreator;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.bootstrap.ddl.StandalonePropertiesLoader;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.typesystem.PatchedYTypeSystemLoader;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@IntegrationTest
public class DbTypeSystemImplIntegrationTest
{
    private static final Logger LOG = Logger.getLogger(DbTypeSystemImplIntegrationTest.class);


    @Test
    public void assertLoadFromDB()
    {
        DBCPDataSourceCreator dBCPDataSourceCreator = new DBCPDataSourceCreator();
        StandalonePropertiesLoader standalonePropertiesLoader = new StandalonePropertiesLoader(ConfigUtil.getPlatformConfig(DbTypeSystemImplIntegrationTest.class), "junit");
        PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(DbTypeSystemImplIntegrationTest.class);
        DatabaseSettings dbSettings = (new CustomHybrisDatabaseSettingsFactory((PropertiesLoader)standalonePropertiesLoader)).createDatabaseSettings();
        DataSource dataSource = dBCPDataSourceCreator.createDataSource(dbSettings);
        DbTypeSystem dbTypeSystem = (new DbTypeSystemFactory()).createDbTypeSystem(dataSource, dbSettings.getTablePrefix(), dbSettings
                        .getTypeSystemName());
        YTypeSystem system = (new CodeGenerator(platformConfig, (YTypeSystemHandler)new PatchedYTypeSystemLoader((YTypeSystemHandler)new YTypeSystemLoader(true), dbTypeSystem))).getTypeSystem();
        Assertions.assertThat(system.getComposedTypes()).isNotEmpty();
        Assertions.assertThat(system.getAtomicTypes()).isNotEmpty();
        Assertions.assertThat(system.getAttributes()).isNotEmpty();
        Assertions.assertThat(system.getMapTypes()).isNotEmpty();
        Assertions.assertThat(system.getCollectionTypes()).isNotEmpty();
        Assertions.assertThat(system.getDeployments()).isNotEmpty();
    }
}
