package de.hybris.platform.core;

import java.io.Serializable;
import java.util.Map;

public class DatabaseProperties implements Serializable
{
    public static final String SQL_FALSE = "SQL_FALSE";
    public static final String SQL_TRUE = "SQL_TRUE";
    public static final String SQL_IS_FALSE = "SQL_IS_FALSE";
    public static final String SQL_IS_TRUE = "SQL_IS_TRUE";
    public static final String JDBC_DRIVER_CLASS = "JDBC_DRIVER_CLASS";
    public static final String JDBC_URL = "JDBC_URL";
    public static final String JDBC_DRIVER_VERSION = "JDBC_DRIVER_VERSION";
    public static final String LIMIT_SUPPORTED = "LIMIT_SUPPORTED";
    public static final String JDBC_ABSOLUTE_SUPPORTED = "JDBC_ABSOLUTE_SUPPORTED";
    private final Map infoMap;
    private final String database;
    private final String version;


    public DatabaseProperties(Map info, String database, String dbVersion)
    {
        this.infoMap = info;
        this.database = database;
        this.version = dbVersion;
    }


    public String getSQLString(String field)
    {
        return (String)this.infoMap.get(field);
    }


    public boolean hasProperty(String property)
    {
        Boolean hasProperty = (Boolean)this.infoMap.get(property);
        return (hasProperty != null && hasProperty.booleanValue());
    }


    public String getDatabaseName()
    {
        return this.database;
    }


    public String getDatabaseVersion()
    {
        return this.version;
    }
}
