package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.sql.HybrisMySqlBuilder;
import java.util.Objects;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.mysql.MySql50ModelReader;
import org.apache.ddlutils.platform.mysql.MySql50Platform;

public class HybrisMySqlPlatform extends MySql50Platform implements HybrisPlatform
{
    private static final String MYSQL_ALLOW_FRACTIONAL_SECONDS = "mysql.allow.fractional.seconds";
    private final boolean isFractionalSecondsSupportEnabled;


    private HybrisMySqlPlatform(boolean isFractionalSecondsSupportEnabled)
    {
        this.isFractionalSecondsSupportEnabled = isFractionalSecondsSupportEnabled;
    }


    public static HybrisPlatform build(DatabaseSettings databaseSettings)
    {
        Objects.requireNonNull(databaseSettings);
        boolean allowFractionaSeconds = Boolean.parseBoolean(databaseSettings.getProperty("mysql.allow.fractional.seconds", Boolean.TRUE
                        .toString()));
        HybrisMySqlPlatform instance = new HybrisMySqlPlatform(allowFractionaSeconds);
        instance.provideCustomMapping();
        instance.setSqlBuilder((SqlBuilder)new HybrisMySqlBuilder((Platform)instance, databaseSettings));
        MySql50ModelReader reader = new MySql50ModelReader((Platform)instance);
        reader.setDefaultTablePattern(databaseSettings.getTablePrefix() + "%");
        instance.setModelReader((JdbcModelReader)reader);
        return instance;
    }


    private void provideCustomMapping()
    {
        PlatformInfo platformInfo = getPlatformInfo();
        platformInfo.setMaxColumnNameLength(30);
        platformInfo.addNativeTypeMapping(-1, "TEXT");
        platformInfo.addNativeTypeMapping(12002, "BIGINT", -5);
        platformInfo.addNativeTypeMapping(12000, "TEXT", -1);
        platformInfo.addNativeTypeMapping(12003, "LONGTEXT", -1);
        platformInfo.addNativeTypeMapping(12001, "TEXT", -1);
        platformInfo.addNativeTypeMapping(12, "VARCHAR", 12);
        platformInfo.setDefaultSize(12, 255);
        platformInfo.addNativeTypeMapping(6, "FLOAT{0}");
        platformInfo.setHasPrecisionAndScale(6, true);
        if(this.isFractionalSecondsSupportEnabled)
        {
            platformInfo.setHasSize(93, true);
            platformInfo.setDefaultSize(93, 6);
        }
    }


    public String getColumnName(Column column)
    {
        return ((HybrisMySqlBuilder)getSqlBuilder()).getColumnName(column);
    }


    public String getTableName(Table table)
    {
        return getSqlBuilder().getTableName(table);
    }
}
