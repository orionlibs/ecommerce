package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.sql.HybrisMSSqlBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.mssql.MSSqlPlatform;
import org.apache.log4j.Logger;

public class HybrisMSSqlPlatform extends MSSqlPlatform implements HybrisPlatform
{
    private static final Logger LOG = Logger.getLogger(HybrisMSSqlPlatform.class);


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
            PreparedStatement statement = conn.prepareStatement("SELECT SCHEMA_NAME()");
            try
            {
                ResultSet resultSet = statement.executeQuery();
                try
                {
                    if(resultSet.next())
                    {
                        String str = resultSet.getString(1);
                        if(resultSet != null)
                        {
                            resultSet.close();
                        }
                        if(statement != null)
                        {
                            statement.close();
                        }
                        if(conn != null)
                        {
                            conn.close();
                        }
                        return str;
                    }
                    throw new SQLException("Couldn't determine database schema name");
                }
                catch(Throwable throwable)
                {
                    if(resultSet != null)
                    {
                        try
                        {
                            resultSet.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
            }
            catch(Throwable throwable)
            {
                if(statement != null)
                {
                    try
                    {
                        statement.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
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


    public static HybrisPlatform build(DatabaseSettings databaseSettings)
    {
        HybrisMSSqlPlatform instance = new HybrisMSSqlPlatform();
        instance.provideCustomMapping();
        instance.setSqlBuilder((SqlBuilder)new HybrisMSSqlBuilder((Platform)instance, databaseSettings));
        HybrisMSSqlModelReader reader = new HybrisMSSqlModelReader((Platform)instance);
        reader.setDefaultTablePattern(databaseSettings.getTablePrefix() + "%");
        instance.setModelReader((JdbcModelReader)reader);
        return instance;
    }


    private void provideCustomMapping()
    {
        PlatformInfo platformInfo = getPlatformInfo();
        platformInfo.setMaxColumnNameLength(30);
        platformInfo.addNativeTypeMapping(12002, "BIGINT", -5);
        platformInfo.addNativeTypeMapping(12000, "NVARCHAR(MAX)", -1);
        platformInfo.addNativeTypeMapping(12003, "NVARCHAR(MAX)", -1);
        platformInfo.addNativeTypeMapping(12001, "NVARCHAR(MAX)", -1);
        platformInfo.addNativeTypeMapping(-5, "BIGINT");
        platformInfo.addNativeTypeMapping(12, "NVARCHAR");
        platformInfo.addNativeTypeMapping(-7, "TINYINT");
        platformInfo.addNativeTypeMapping(4, "INTEGER");
        platformInfo.addNativeTypeMapping(5, "INTEGER");
        platformInfo.addNativeTypeMapping(-6, "TINYINT", -6);
        platformInfo.addNativeTypeMapping(8, "FLOAT", 8);
        platformInfo.addNativeTypeMapping(6, "FLOAT", 8);
        platformInfo.addNativeTypeMapping(-9, "NVARCHAR", -9);
        platformInfo.addNativeTypeMapping(92, "DATETIME2", 93);
        platformInfo.addNativeTypeMapping(93, "DATETIME2");
        platformInfo.addNativeTypeMapping(2004, "VARBINARY(MAX)");
    }


    public String getTableName(Table table)
    {
        return getSqlBuilder().getTableName(table);
    }


    public String getColumnName(Column column)
    {
        return ((HybrisMSSqlBuilder)getSqlBuilder()).getColumnName(column);
    }
}
