package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.sql.HybrisOracleBuilder;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.oracle.Oracle10Platform;
import org.apache.ddlutils.util.SqlTokenizer;
import org.apache.log4j.Logger;

public class HybrisOraclePlatform extends Oracle10Platform implements HybrisPlatform
{
    private static final Logger LOG = Logger.getLogger(DDLGeneratorUtils.class);
    private static final String NVARCHAR2_DEFAULT_SIZE = "4000";
    private static HybrisOracleBuilder hybrisOracleBuilder;


    public static HybrisPlatform build(DatabaseSettings databaseSettings)
    {
        HybrisOraclePlatform instance = new HybrisOraclePlatform();
        instance.provideCustomMapping();
        instance.setSqlBuilder((SqlBuilder)new HybrisOracleBuilder((Platform)instance, databaseSettings));
        HybrisOracleModelReader hybrisOracleModelReader = new HybrisOracleModelReader((Platform)instance);
        hybrisOracleModelReader.setDefaultTablePattern((databaseSettings.getTablePrefix() + "%").toUpperCase(LocaleHelper.getPersistenceLocale()));
        instance.setModelReader((JdbcModelReader)hybrisOracleModelReader);
        return instance;
    }


    public Database readModelFromDatabase(String name) throws DatabaseOperationException
    {
        try
        {
            return readModelFromDatabase(name, null, determineSchemaName(), null);
        }
        catch(SQLException e)
        {
            throw new DatabaseOperationException(e);
        }
    }


    protected String determineSchemaName() throws SQLException
    {
        Connection conn = getDataSource().getConnection();
        try
        {
            String str = conn.getMetaData().getUserName();
            if(conn != null)
            {
                conn.close();
            }
            return str;
        }
        catch(Throwable throwable)
        {
            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    private void provideCustomMapping()
    {
        PlatformInfo platformInfo = getPlatformInfo();
        platformInfo.setMaxColumnNameLength(30);
        platformInfo.addNativeTypeMapping(12002, "NUMBER", -5);
        platformInfo.setHasPrecisionAndScale(12002, true);
        platformInfo.setHasSize(12002, false);
        platformInfo.addNativeTypeMapping(12000, "VARCHAR2(4000)", 12);
        platformInfo.addNativeTypeMapping(12003, "CLOB", -1);
        platformInfo.addNativeTypeMapping(12001, "VARCHAR", 12);
        platformInfo.addNativeTypeMapping(2, "NUMBER", 2);
        platformInfo.addNativeTypeMapping(93, "TIMESTAMP(3)");
        platformInfo.setDefaultSize(12, 4000);
        mapToNumberWithScaleAndPrecision(platformInfo, 8);
        mapToNumberWithScaleAndPrecision(platformInfo, -5);
        mapToNumberWithScaleAndPrecision(platformInfo, 6);
        mapToNumberWithScaleAndPrecision(platformInfo, -7);
        mapToNumberWithScaleAndPrecision(platformInfo, 5);
        mapToNumberWithScaleAndPrecision(platformInfo, 4);
    }


    private void mapToNumberWithScaleAndPrecision(PlatformInfo platformInfo, int jdbcType)
    {
        platformInfo.addNativeTypeMapping(jdbcType, "NUMBER{0}");
        platformInfo.setHasPrecisionAndScale(jdbcType, true);
    }


    public int evaluateBatch(Connection connection, String sql, boolean continueOnError) throws DatabaseOperationException
    {
        Statement statement = null;
        int errors = 0;
        int commandCount = 0;
        try
        {
            statement = connection.createStatement();
            SqlTokenizer tokenizer = new SqlTokenizer(sql);
            while(tokenizer.hasMoreStatements())
            {
                String command = tokenizer.getNextStatement();
                command = command.trim();
                if(command.length() == 0)
                {
                    continue;
                }
                commandCount++;
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("About to execute SQL " + command);
                }
                try
                {
                    int results = statement.executeUpdate(command);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("After execution, " + results + " row(s) have been changed");
                    }
                }
                catch(SQLException ex)
                {
                    if(continueOnError)
                    {
                        LOG.warn("SQL Command " + command + " failed with: " + ex.getMessage());
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(ex);
                        }
                        errors++;
                    }
                    else
                    {
                        throw new DatabaseOperationException("Error while executing SQL " + command, ex);
                    }
                }
                SQLWarning warning = connection.getWarnings();
                while(warning != null)
                {
                    LOG.warn(warning.toString());
                    warning = warning.getNextWarning();
                }
                connection.clearWarnings();
            }
            LOG.info("Executed " + commandCount + " SQL command(s) with " + errors + " error(s)");
        }
        catch(SQLException ex)
        {
            throw new DatabaseOperationException("Error while executing SQL", ex);
        }
        finally
        {
            closeStatement(statement);
        }
        return errors;
    }


    public String getColumnName(Column column)
    {
        return ((HybrisOracleBuilder)getSqlBuilder()).getColumnName(column);
    }


    public String getTableName(Table table)
    {
        return getSqlBuilder().getTableName(table);
    }
}
