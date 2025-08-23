package de.hybris.platform.util;

import de.hybris.platform.core.Registry;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public abstract class Config
{
    static final String MODELT_DETECTION_FLAG1 = "modelt.environment.type";
    static final String MODELT_DETECTION_FLAG2 = "modelt.licence.id";


    public static Map<String, String> getParametersByPattern(String pattern)
    {
        Map<String, String> origParams = getAllParameters();
        Map<String, String> params = new HashMap<>(origParams);
        for(String key : origParams.keySet())
        {
            if(!key.startsWith(pattern))
            {
                params.remove(key);
            }
        }
        return params;
    }


    public static Map<String, String> getAllParameters()
    {
        return Registry.getCurrentTenant().getConfig().getAllParameters();
    }


    public static String getParameter(String key)
    {
        return Registry.getCurrentTenant().getConfig().getParameter(key);
    }


    public static String getString(String key, String def)
    {
        return Registry.getCurrentTenant().getConfig().getString(key, def);
    }


    public static char getChar(String key, char def) throws IndexOutOfBoundsException
    {
        return Registry.getCurrentTenant().getConfig().getChar(key, def);
    }


    public static boolean getBoolean(String key, boolean def)
    {
        return Registry.getCurrentTenant().getConfig().getBoolean(key, def);
    }


    public static int getInt(String key, int def) throws NumberFormatException
    {
        return Registry.getCurrentTenant().getConfig().getInt(key, def);
    }


    public static long getLong(String key, long def) throws NumberFormatException
    {
        return Registry.getCurrentTenant().getConfig().getLong(key, def);
    }


    public static double getDouble(String key, double def) throws NumberFormatException
    {
        return Registry.getCurrentTenant().getConfig().getDouble(key, def);
    }


    public static void setParameter(String key, String value)
    {
        Registry.getCurrentTenant().getConfig().setParameter(key, value);
    }


    public static String getDatabase()
    {
        return Registry.getCurrentTenant().getDataSource().getDatabaseName();
    }


    public static String getDatabaseVersion()
    {
        return Registry.getCurrentTenant().getDataSource().getDatabaseVersion();
    }


    public static DatabaseName getDatabaseName()
    {
        return DatabaseName.fromString(Registry.getCurrentTenant().getDataSource().getDatabaseName());
    }


    public static boolean isOracleUsed()
    {
        return ("oracle" == getDatabase());
    }


    public static boolean isHSQLDBUsed()
    {
        return ("hsqldb" == getDatabase());
    }


    public static boolean isMySQLUsed()
    {
        return ("mysql" == getDatabase());
    }


    public static boolean isMySQL8Used()
    {
        return (isMySQLUsed() && StringUtils.startsWith(getDatabaseVersion(), "8."));
    }


    public static boolean isSQLServerUsed()
    {
        return ("sqlserver" == getDatabase());
    }


    public static boolean isHanaUsed()
    {
        return ("sap" == getDatabase());
    }


    public static boolean isPostgreSQLUsed()
    {
        return ("postgresql" == getDatabase());
    }


    public static String getDatabaseURL()
    {
        return getParameter(SystemSpecificParams.DB_URL);
    }


    public static boolean isCloudEnvironment()
    {
        return !StringUtils.isAllEmpty(new CharSequence[] {getParameter("modelt.environment.type"), getParameter("modelt.licence.id")});
    }


    public static String trim(String value, char[] ignore)
    {
        char c, characters[] = value.toCharArray();
        int count = characters.length;
        int offset = 0;
        int limit = count + 0;
        if(count == 0 || (characters[0] > ' ' && characters[limit - 1] > ' '))
        {
            return value;
        }
        int begin = 0;
        do
        {
            if(begin == limit)
            {
                return "";
            }
            c = characters[begin++];
        }
        while(c <= ' ' && !contains(c, ignore));
        int end = limit;
        do
        {
            c = characters[--end];
        }
        while(c <= ' ' && !contains(c, ignore));
        return value.substring(begin - 0 - 1, end - 0 + 1);
    }


    private static boolean contains(char c, char[] list)
    {
        for(int i = 0; i < list.length; i++)
        {
            if(c == list[i])
            {
                return true;
            }
        }
        return false;
    }
}
