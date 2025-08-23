package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.sql.HybrisHsqlDbSqlBuilder;
import de.hybris.bootstrap.util.LocaleHelper;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.hsqldb.HsqlDbModelReader;
import org.apache.ddlutils.platform.hsqldb.HsqlDbPlatform;

public class HybrisHsqlDbPlatform extends HsqlDbPlatform implements HybrisPlatform
{
    public static HybrisPlatform build(DatabaseSettings databaseSettings)
    {
        HybrisHsqlDbPlatform instance = new HybrisHsqlDbPlatform();
        instance.provideCustomMapping();
        instance.setSqlBuilder((SqlBuilder)new HybrisHsqlDbSqlBuilder((Platform)instance, databaseSettings));
        HsqlDbModelReader reader = new HsqlDbModelReader((Platform)instance);
        reader.setDefaultTablePattern((databaseSettings.getTablePrefix() + "%").toUpperCase(LocaleHelper.getPersistenceLocale()));
        instance.setModelReader((JdbcModelReader)reader);
        return instance;
    }


    private void provideCustomMapping()
    {
        PlatformInfo platformInfo = getPlatformInfo();
        platformInfo.setMaxColumnNameLength(30);
        platformInfo.addNativeTypeMapping(12002, "BIGINT", -5);
        platformInfo.addNativeTypeMapping(12000, "LONGVARCHAR", -1);
        platformInfo.addNativeTypeMapping(12003, "LONGVARCHAR", -1);
        platformInfo.addNativeTypeMapping(12001, "LONGVARCHAR", -1);
        platformInfo.addNativeTypeMapping(12, "NVARCHAR", 12);
        platformInfo.addNativeTypeMapping("BIT", "TINYINT", "TINYINT");
        platformInfo.setDefaultSize(12, 255);
        platformInfo.addNativeTypeMapping(6, "FLOAT");
        platformInfo.addNativeTypeMapping(-6, "TINYINT", -6);
    }


    public String getColumnName(Column column)
    {
        return ((HybrisHsqlDbSqlBuilder)getSqlBuilder()).getColumnName(column);
    }


    public String getTableName(Table table)
    {
        return getSqlBuilder().getTableName(table);
    }
}
