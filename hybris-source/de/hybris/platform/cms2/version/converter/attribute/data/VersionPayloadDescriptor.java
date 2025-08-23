package de.hybris.platform.cms2.version.converter.attribute.data;

public class VersionPayloadDescriptor
{
    private final String type;
    private final String value;


    public VersionPayloadDescriptor(String type, String value)
    {
        this.type = type;
        this.value = value;
    }


    public String getType()
    {
        return this.type;
    }


    public String getValue()
    {
        return this.value;
    }
}
