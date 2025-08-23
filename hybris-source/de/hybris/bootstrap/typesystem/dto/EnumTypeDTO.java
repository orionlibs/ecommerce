package de.hybris.bootstrap.typesystem.dto;

public class EnumTypeDTO
{
    private final String extensionName;
    private final String code;
    private final String jaloClass;
    private final boolean autocreate;
    private final boolean generate;
    private final String typeDescription;


    public EnumTypeDTO(String extensionName, String code, String jaloClass, boolean autocreate, boolean generate, String typeDescription)
    {
        this.extensionName = extensionName;
        this.code = code;
        this.jaloClass = jaloClass;
        this.autocreate = autocreate;
        this.generate = generate;
        this.typeDescription = typeDescription;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getJaloClass()
    {
        return this.jaloClass;
    }


    public boolean isAutocreate()
    {
        return this.autocreate;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }


    public String getTypeDescription()
    {
        return this.typeDescription;
    }
}
