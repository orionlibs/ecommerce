package de.hybris.bootstrap.typesystem.dto;

import java.util.Map;

public class IndexDeploymentDTO
{
    private final String extensionName;
    private final String itemDeploymentName;
    private final String name;
    private final boolean unique;
    private final boolean sqlserverclustered;
    private final Map<String, Boolean> fields;


    public IndexDeploymentDTO(String extensionName, String itemDeploymentName, String name, boolean unique, boolean sqlserverclustered, Map<String, Boolean> fields)
    {
        this.extensionName = extensionName;
        this.itemDeploymentName = itemDeploymentName;
        this.name = name;
        this.unique = unique;
        this.sqlserverclustered = sqlserverclustered;
        this.fields = fields;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getItemDeploymentName()
    {
        return this.itemDeploymentName;
    }


    public String getName()
    {
        return this.name;
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public boolean isSqlserverclustered()
    {
        return this.sqlserverclustered;
    }


    public Map<String, Boolean> getFields()
    {
        return this.fields;
    }
}
