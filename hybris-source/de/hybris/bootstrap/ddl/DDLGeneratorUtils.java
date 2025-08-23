package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.typesystem.YDeployment;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

public class DDLGeneratorUtils
{
    public static final int ABSTRACT = 1;
    public static final int GENERIC = 2;
    public static final int FINAL = 8;
    public static final int NONITEM = 16;
    static final String HEX = "0123456789ABCDEF";


    public static String toHex(byte[] bytes)
    {
        char[] chars = new char[bytes.length * 2];
        int i = 0;
        for(byte b : bytes)
        {
            int upper = (0xFF & b) >> 4;
            int lower = 0xF & b;
            chars[i++] = "0123456789ABCDEF".charAt(upper);
            chars[i++] = "0123456789ABCDEF".charAt(lower);
        }
        return new String(chars);
    }


    public static String toHex(Object object)
    {
        return toHex(toByteArray(object));
    }


    private static byte[] toByteArray(Object o)
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(o);
            return bos.toByteArray();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public static String getTableName(Table table, SqlBuilder builder, DatabaseSettings databaseSettings, boolean addPrefix)
    {
        return shortenName(addPrefix ? adjustForTablePrefix(table.getName(), databaseSettings.getTablePrefix()) : table.getName(), builder
                        .getMaxTableNameLength());
    }


    public static String getIndexName(Index index, SqlBuilder builder, DatabaseSettings databaseSettings, boolean addPrefix)
    {
        return shortenName(addPrefix ? adjustForTablePrefix(index.getName(), databaseSettings.getTablePrefix()) : index.getName(), builder
                        .getMaxConstraintNameLength());
    }


    public static String getShortenedColumnName(String columnName)
    {
        return shortenName(columnName, 30);
    }


    public static String getColumnName(Column column, SqlBuilder builder)
    {
        return shortenName(column.getName(), builder.getMaxColumnNameLength());
    }


    static String shortenName(String name, int desiredLength)
    {
        if(desiredLength > 0 && name.length() > desiredLength)
        {
            return name.substring(0, desiredLength);
        }
        return name;
    }


    public static String adjustForTablePrefix(String name, String tablePrefix)
    {
        return (tablePrefix == null) ? name : (tablePrefix + tablePrefix);
    }


    public static DataBaseProvider guessDatabaseFromURL(String dbURL) throws IllegalArgumentException
    {
        if(dbURL.contains("jdbc:hsqldb:"))
        {
            return DataBaseProvider.HSQL;
        }
        if(dbURL.contains("jdbc:mysql:"))
        {
            return DataBaseProvider.MYSQL;
        }
        if(dbURL.contains("jdbc:oracle:"))
        {
            return DataBaseProvider.ORACLE;
        }
        if(dbURL.contains("jdbc:jtds:sqlserver:"))
        {
            return DataBaseProvider.MSSQL;
        }
        if(dbURL.contains("jdbc:sqlserver:"))
        {
            return DataBaseProvider.MSSQL;
        }
        if(dbURL.contains("jdbc:sap:"))
        {
            return DataBaseProvider.HANA;
        }
        if(dbURL.contains("jdbc:postgresql:"))
        {
            return DataBaseProvider.POSTGRESQL;
        }
        throw new IllegalArgumentException("Database url contains unsupported database type, " + dbURL);
    }


    public static int getDeploymentModifiers(YDeployment depl)
    {
        int result = 0;
        if(depl.isAbstract())
        {
            result |= 0x1;
        }
        if(depl.isGeneric())
        {
            result |= 0x2;
        }
        if(depl.isFinal())
        {
            result |= 0x8;
        }
        if(depl.isNonItemDeployment())
        {
            result |= 0x10;
        }
        return result;
    }
}
