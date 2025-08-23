package de.hybris.bootstrap.ddl;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.ddl.sql.ColumnNativeTypeDecorator;
import de.hybris.bootstrap.ddl.sql.HanaBlobColumnNativeTypeDecorator;
import de.hybris.bootstrap.ddl.sql.HanaSqlBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.dynabean.SqlDynaProperty;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.SqlBuilder;

public class HybrisHanaPlatform extends PlatformImplBase implements HybrisPlatform
{
    public static final String DATABASENAME = "sap";
    private final DatabaseSettings databaseSettings;


    public HybrisHanaPlatform(DatabaseSettings databaseSettings)
    {
        this.databaseSettings = databaseSettings;
        PlatformInfo info = getPlatformInfo();
        info.setMaxIdentifierLength(31);
        info.addNativeTypeMapping(2003, "BLOB", -4);
        info.addNativeTypeMapping(-2, "BLOB", -4);
        info.addNativeTypeMapping(-7, "DECIMAL", 2);
        info.addNativeTypeMapping(16, "DECIMAL", 2);
        info.addNativeTypeMapping(2004, "BLOB", 2004);
        info.addNativeTypeMapping(2005, "CLOB", 2005);
        info.addNativeTypeMapping(3, "DECIMAL", 2);
        info.addNativeTypeMapping(2001, "BLOB", -4);
        info.addNativeTypeMapping(8, "DECIMAL");
        info.addNativeTypeMapping(6, "DECIMAL", 8);
        info.addNativeTypeMapping(2000, "BLOB", -4);
        info.addNativeTypeMapping(-4, "BLOB");
        info.addNativeTypeMapping(-1, "CLOB", -1);
        info.addNativeTypeMapping(0, "BLOB", -4);
        info.addNativeTypeMapping(1111, "BLOB", -4);
        info.addNativeTypeMapping(2006, "BLOB", -4);
        info.addNativeTypeMapping(2002, "BLOB", -4);
        info.addNativeTypeMapping(-6, "INTEGER", 5);
        info.addNativeTypeMapping(-3, "BLOB", -4);
        info.addNativeTypeMapping(12, "VARCHAR");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setHasSize(-2, false);
        info.setHasSize(-3, false);
        setModelReader((JdbcModelReader)new HanaModelReader((Platform)this, databaseSettings.getTablePrefix()));
    }


    public String getName()
    {
        return "sap";
    }


    public static HybrisPlatform build(DatabaseSettings databaseSettings)
    {
        HybrisHanaPlatform instance = new HybrisHanaPlatform(databaseSettings);
        instance.provideCustomMapping();
        instance.setSqlBuilder((SqlBuilder)new HanaSqlBuilder((Platform)instance, databaseSettings, getNativeTypeDecorators(databaseSettings)));
        return instance;
    }


    private void provideCustomMapping()
    {
        PlatformInfo platformInfo = getPlatformInfo();
        platformInfo.setMaxColumnNameLength(30);
        platformInfo.addNativeTypeMapping(12002, "BIGINT", -5);
        platformInfo.addNativeTypeMapping(12000, "NVARCHAR{0}", -9);
        platformInfo.addNativeTypeMapping(12003, "NCLOB", -1);
        platformInfo.addNativeTypeMapping(12001, "NVARCHAR{0}", -9);
        platformInfo.setHasSize(12000, true);
        platformInfo.setHasSize(12001, true);
        platformInfo.addNativeTypeMapping(-7, "DECIMAL(1,0)", 2);
        platformInfo.addNativeTypeMapping(3, "DECIMAL", 3);
        platformInfo.setHasSize(6, true);
        platformInfo.setHasSize(8, true);
        platformInfo.setHasSize(-9, true);
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


    protected void setObject(PreparedStatement statement, int sqlIndex, DynaBean dynaBean, SqlDynaProperty property) throws SQLException
    {
        int typeCode = property.getColumn().getTypeCode();
        Object value = dynaBean.get(property.getName());
        if(value == null)
        {
            switch(typeCode)
            {
                case -4:
                case -3:
                case -2:
                case 2004:
                    statement.setBytes(sqlIndex, null);
                    return;
            }
            statement.setNull(sqlIndex, typeCode);
        }
        else
        {
            super.setObject(statement, sqlIndex, dynaBean, property);
        }
    }


    protected void setSqlBuilder(SqlBuilder builder)
    {
        super.setSqlBuilder((SqlBuilder)new HanaSqlBuilder((Platform)this, this.databaseSettings, getNativeTypeDecorators(this.databaseSettings)));
    }


    public String getColumnName(Column column)
    {
        return ((HanaSqlBuilder)getSqlBuilder()).getColumnName(column);
    }


    public String getTableName(Table table)
    {
        return getSqlBuilder().getTableName(table);
    }


    private static Iterable<ColumnNativeTypeDecorator> getNativeTypeDecorators(DatabaseSettings databaseSettings)
    {
        return (Iterable<ColumnNativeTypeDecorator>)ImmutableList.of(new HanaBlobColumnNativeTypeDecorator(databaseSettings));
    }
}
