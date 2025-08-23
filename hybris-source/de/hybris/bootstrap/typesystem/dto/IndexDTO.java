package de.hybris.bootstrap.typesystem.dto;

import java.util.Collection;
import java.util.Map;

public class IndexDTO
{
    private final String extensionName;
    private final String typeCode;
    private final String indexName;
    private final String creationMode;
    private final boolean unique;
    private final Map<String, Boolean> keyLowerMap;
    private final Collection<String> includeCollection;
    private final boolean remove;
    private final boolean replace;


    public IndexDTO(String extensionName, String typeCode, String indexName, String creationMode, boolean unique, Map<String, Boolean> keyLowerMap, boolean remove, boolean replace)
    {
        this.extensionName = extensionName;
        this.typeCode = typeCode;
        this.indexName = indexName;
        this.creationMode = creationMode;
        this.unique = unique;
        this.keyLowerMap = keyLowerMap;
        this.remove = remove;
        this.replace = replace;
        this.includeCollection = null;
    }


    public IndexDTO(String extensionName, String typeCode, String indexName, String creationMode, boolean unique, Map<String, Boolean> keyLowerMap, boolean remove, boolean replace, Collection<String> includeCollection)
    {
        this.extensionName = extensionName;
        this.typeCode = typeCode;
        this.indexName = indexName;
        this.creationMode = creationMode;
        this.unique = unique;
        this.keyLowerMap = keyLowerMap;
        this.remove = remove;
        this.replace = replace;
        this.includeCollection = includeCollection;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public String getIndexName()
    {
        return this.indexName;
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public Map<String, Boolean> getKeyLowerMap()
    {
        return this.keyLowerMap;
    }


    public Collection<String> getIncludeCollection()
    {
        return this.includeCollection;
    }


    public boolean isRemove()
    {
        return this.remove;
    }


    public boolean isReplace()
    {
        return this.replace;
    }


    public String getCreationMode()
    {
        return this.creationMode;
    }
}
