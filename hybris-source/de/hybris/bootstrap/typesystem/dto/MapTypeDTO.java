package de.hybris.bootstrap.typesystem.dto;

public class MapTypeDTO
{
    private final String extensionName;
    private final String code;
    private final String argumentType;
    private final String returnType;
    private final boolean autocreate;
    private final boolean generate;


    public MapTypeDTO(String extensionName, String code, String argumentType, String returnType, boolean autocreate, boolean generate)
    {
        this.extensionName = extensionName;
        this.code = code;
        this.argumentType = argumentType;
        this.returnType = returnType;
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


    public String getArgumentType()
    {
        return this.argumentType;
    }


    public String getReturnType()
    {
        return this.returnType;
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
