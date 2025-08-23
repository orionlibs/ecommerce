package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.sql.HybrisPostgreSqlBuilder;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.postgresql.PostgreSqlModelReader;
import org.apache.ddlutils.platform.postgresql.PostgreSqlPlatform;

public class HybrisPostgreSqlPlatform extends PostgreSqlPlatform implements HybrisPlatform
{
    public static HybrisPlatform build(DatabaseSettings databaseSettings)
    {
        HybrisPostgreSqlPlatform instance = new HybrisPostgreSqlPlatform();
        instance.provideCustomMapping();
        instance.setSqlBuilder((SqlBuilder)new HybrisPostgreSqlBuilder((Platform)instance, databaseSettings));
        PostgreSqlModelReader reader = new PostgreSqlModelReader((Platform)instance);
        reader.setDefaultTablePattern(databaseSettings.getTablePrefix() + "%");
        instance.setModelReader((JdbcModelReader)reader);
        return instance;
    }


    private void provideCustomMapping()
    {
        PlatformInfo platformInfo = getPlatformInfo();
        platformInfo.setMaxIdentifierLength(63);
        platformInfo.setMaxColumnNameLength(30);
        platformInfo.addNativeTypeMapping(12002, "BIGINT", -5);
        platformInfo.addNativeTypeMapping(12000, "TEXT", -1);
        platformInfo.addNativeTypeMapping(12003, "TEXT", -1);
        platformInfo.addNativeTypeMapping(12001, "TEXT", -1);
        platformInfo.addNativeTypeMapping(16, "SMALLINT", 5);
        platformInfo.addNativeTypeMapping(-7, "SMALLINT", 5);
        platformInfo.addNativeTypeMapping(12001, "TEXT", -1);
        platformInfo.setDefaultSize(1, 255);
        platformInfo.setDefaultSize(12, 255);
    }


    public String getColumnName(Column column)
    {
        return ((HybrisPostgreSqlBuilder)getSqlBuilder()).getColumnName(column);
    }


    public String getTableName(Table table)
    {
        return getSqlBuilder().getTableName(table);
    }
}
