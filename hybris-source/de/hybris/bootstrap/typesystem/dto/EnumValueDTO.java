package de.hybris.bootstrap.typesystem.dto;

public class EnumValueDTO
{
    private final String extensionName;
    private final String enumTypeCode;
    private final String valueCode;
    private final int sequenceNumber;
    private final boolean asDefault;
    private final String description;


    public EnumValueDTO(String extensionName, String enumTypeCode, String valueCode, int sequenceNumber, boolean asDefault, String description)
    {
        this.extensionName = extensionName;
        this.enumTypeCode = enumTypeCode;
        this.valueCode = valueCode;
        this.sequenceNumber = sequenceNumber;
        this.asDefault = asDefault;
        this.description = description;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getEnumTypeCode()
    {
        return this.enumTypeCode;
    }


    public String getValueCode()
    {
        return this.valueCode;
    }


    public int getSequenceNumber()
    {
        return this.sequenceNumber;
    }


    public boolean isAsDefault()
    {
        return this.asDefault;
    }


    public String getDescription()
    {
        return this.description;
    }
}
