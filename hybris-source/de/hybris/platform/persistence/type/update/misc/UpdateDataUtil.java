package de.hybris.platform.persistence.type.update.misc;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.jdbc.DBColumn;
import java.util.HashMap;
import java.util.Map;

public class UpdateDataUtil
{
    public static final int MSSQL_MAX_LONGNVARCHAR_LENGTH = 1073741823;
    public static final int HSSQL_MAX_VARCHAR_LENGTH = 16777216;
    private static final Map<String, SpecificColumnResolver> resolverMappings = new HashMap<>();

    static
    {
        resolverMappings.put("sqlserver", new SQLServerSpecificColumnResolver());
        resolverMappings.put("oracle", new DummyColumnResolver("oracle"));
        resolverMappings.put("mysql", new DummyColumnResolver("mysql"));
        resolverMappings.put("hsqldb", new HSQLDBServerSpecificColumnResolver());
    }

    public static boolean compare(DBColumn existingColumn, String newcolumnDefinitionStr)
    {
        ColumnDefinition newColumnDefinition = parseColumnDefinition(newcolumnDefinitionStr);
        return newColumnDefinition.equals(existingColumn);
    }


    private static ColumnDefinition parseColumnDefinition(String columnDefinition)
    {
        String def = columnDefinition;
        String typeDef = null;
        Integer length = null;
        Integer precision = null;
        int startLength = def.indexOf('(');
        if(startLength > 0)
        {
            int endLength = def.indexOf(')', startLength);
            int separator = def.indexOf(',', startLength);
            typeDef = def.trim().toLowerCase(LocaleHelper.getPersistenceLocale()).substring(0, startLength);
            if(separator > 0 && separator < endLength)
            {
                String lengthTxt = def.substring(startLength + 1, separator).trim();
                try
                {
                    length = Integer.valueOf(lengthTxt);
                }
                catch(NumberFormatException numberFormatException)
                {
                }
                String precTxt = def.substring(separator + 1, endLength).trim();
                try
                {
                    precision = Integer.valueOf(precTxt);
                }
                catch(NumberFormatException numberFormatException)
                {
                }
            }
            else
            {
                String lengthTxt = def.substring(startLength + 1, endLength).trim();
                try
                {
                    length = Integer.valueOf(lengthTxt);
                }
                catch(NumberFormatException numberFormatException)
                {
                }
            }
        }
        else
        {
            typeDef = def.trim().toLowerCase(LocaleHelper.getPersistenceLocale());
        }
        return (ColumnDefinition)new DefaultColumnDefinition(typeDef, length, precision);
    }


    public static boolean isDatabaseSupported()
    {
        return (Config.isMySQLUsed() || Config.isSQLServerUsed());
    }
}
