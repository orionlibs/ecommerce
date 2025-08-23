package de.hybris.bootstrap.typesystem.dto;

import java.util.Map;

public class DBTypeMappingDTO
{
    private final String extensionName;
    private final String dbName;
    private final String primKey;
    private final String nullStr;
    private final String notNullStr;
    private final Map<String, String> typeMappings;


    public DBTypeMappingDTO(String extensionName, String dbName, String primKey, String nullStr, String notNullStr, Map<String, String> typeMappings)
    {
        this.extensionName = extensionName;
        this.dbName = dbName;
        this.primKey = primKey;
        this.nullStr = nullStr;
        this.notNullStr = notNullStr;
        this.typeMappings = typeMappings;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getDbName()
    {
        return this.dbName;
    }


    public String getPrimKey()
    {
        return this.primKey;
    }


    public String getNullStr()
    {
        return this.nullStr;
    }


    public String getNotNullStr()
    {
        return this.notNullStr;
    }


    public Map<String, String> getTypeMappings()
    {
        return this.typeMappings;
    }
}
