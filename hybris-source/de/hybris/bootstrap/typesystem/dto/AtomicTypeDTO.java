package de.hybris.bootstrap.typesystem.dto;

public class AtomicTypeDTO
{
    private final String extensionName;
    private final String code;
    private final String superType;
    private final boolean autocreate;
    private final boolean generate;


    public AtomicTypeDTO(String extensionName, String code, String superType, boolean autocreate, boolean generate)
    {
        this.extensionName = extensionName;
        this.code = code;
        this.superType = superType;
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


    public String getSuperType()
    {
        return this.superType;
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
