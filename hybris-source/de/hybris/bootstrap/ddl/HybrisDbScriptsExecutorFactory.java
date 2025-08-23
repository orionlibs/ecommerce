package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.tools.SqlScriptParser;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class HybrisDbScriptsExecutorFactory
{
    private static final int DDL_HANA_BATCH_SIZE = 1;
    private static final String DDL_DEFAULT_BATCH_SIZE = "1000";
    private static final String DML_DEFAULT_BATCH_SIZE = "5000";


    public static HybrisDbScriptsExecutor getExecutor(DataSourceCreator dataSourceCreator, DatabaseSettings databaseSettings, PropertiesLoader propertiesLoader, boolean dryRun)
    {
        if(dryRun)
        {
            return (HybrisDbScriptsExecutor)new DryRunDbScriptExecutor();
        }
        DataSource dataSource = dataSourceCreator.createDataSource(databaseSettings);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        if(DataBaseProvider.HANA.equals(databaseSettings.getDataBaseProvider()))
        {
            SqlScriptParser sqlScriptParser1 = SqlScriptParser.getDefaultSqlScriptParser(";");
            return (HybrisDbScriptsExecutor)new DefaultDbScriptsExecutor(jdbcTemplate, sqlScriptParser1, 1,
                            getDmlBatchSize(propertiesLoader));
        }
        if(DataBaseProvider.ORACLE.equals(databaseSettings.getDataBaseProvider()))
        {
            SqlScriptParser sqlScriptParser1 = SqlScriptParser.getOracleSqlScriptParser("-- hybris comment");
            return (HybrisDbScriptsExecutor)new DefaultDbScriptsExecutor(jdbcTemplate, sqlScriptParser1, getDdlBatchSize(propertiesLoader),
                            getDmlBatchSize(propertiesLoader));
        }
        SqlScriptParser sqlScriptParser = SqlScriptParser.getDefaultSqlScriptParser(";");
        return (HybrisDbScriptsExecutor)new DefaultDbScriptsExecutor(jdbcTemplate, sqlScriptParser, getDdlBatchSize(propertiesLoader),
                        getDmlBatchSize(propertiesLoader));
    }


    private static int getDdlBatchSize(PropertiesLoader propertiesLoader)
    {
        String value = propertiesLoader.getProperty("bootstrap.init.type.system.ddl.sql.batch.size", "1000");
        return Integer.parseInt(value);
    }


    private static int getDmlBatchSize(PropertiesLoader propertiesLoader)
    {
        String value = propertiesLoader.getProperty("bootstrap.init.type.system.dml.sql.batch.size", "5000");
        return Integer.parseInt(value);
    }
}
