package de.hybris.platform.util.jdbc;

public final class DatabaseNameResolver
{
    public static String guessDatabaseNameFromURL(String dbURL) throws IllegalArgumentException
    {
        String database = null;
        if(dbURL.contains("jdbc:hsqldb:"))
        {
            database = "hsqldb";
        }
        else if(dbURL.contains("jdbc:mysql:"))
        {
            database = "mysql";
        }
        else if(dbURL.contains("jdbc:oracle:"))
        {
            database = "oracle";
        }
        else if(dbURL.contains("jdbc:jtds:sqlserver:"))
        {
            database = "sqlserver";
        }
        else if(dbURL.contains("jdbc:sqlserver:"))
        {
            database = "sqlserver";
        }
        else if(dbURL.contains("jdbc:sap:"))
        {
            database = "sap";
        }
        else if(dbURL.contains("jdbc:postgresql:"))
        {
            database = "postgresql";
        }
        else
        {
            throw new IllegalArgumentException("Database url contains unsupported database type, " + dbURL);
        }
        return database;
    }
}
