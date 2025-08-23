package de.hybris.bootstrap.typesystem.dto;

import de.hybris.bootstrap.typesystem.YCollectionType;

public class CollectionTypeDTO
{
    private final String extensionName;
    private final String code;
    private final String elementType;
    private final YCollectionType.TypeOfCollection collType;
    private final boolean autocreate;
    private final boolean generate;


    public CollectionTypeDTO(String extensionName, String code, String elementType, YCollectionType.TypeOfCollection collType, boolean autocreate, boolean generate)
    {
        this.extensionName = extensionName;
        this.code = code;
        this.elementType = elementType;
        this.collType = collType;
        this.autocreate = autocreate;
        this.generate = generate;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getElementType()
    {
        return this.elementType;
    }


    public YCollectionType.TypeOfCollection getCollType()
    {
        return this.collType;
    }


    public boolean isAutocreate()
    {
        return this.autocreate;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }
}
