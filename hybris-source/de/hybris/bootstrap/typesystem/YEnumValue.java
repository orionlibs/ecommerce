package de.hybris.bootstrap.typesystem;

public class YEnumValue extends YTypeSystemElement implements Comparable<YEnumValue>
{
    private final String enumTypeCode;
    private YEnumType type;
    private final String code;
    private boolean defaultVariable;
    private int position = 0;
    private String description;


    public YEnumValue(YNamespace container, String enumTypeCode, String code)
    {
        super(container);
        this.enumTypeCode = enumTypeCode;
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getEnumTypeCode()
    {
        return this.enumTypeCode;
    }


    public YEnumType getEnumType()
    {
        if(this.type == null)
        {
            YType tmp = getTypeSystem().getType(getEnumTypeCode());
            if(!(tmp instanceof YEnumType))
            {
                throw new IllegalStateException("invalid enum value " + this + " due to missing enum type '" + getEnumTypeCode() + "'");
            }
            this.type = (YEnumType)tmp;
        }
        return this.type;
    }


    public boolean isDefault()
    {
        return this.defaultVariable;
    }


    public void setDefault(boolean isDefault)
    {
        this.defaultVariable = isDefault;
    }


    public int getPosition()
    {
        return this.position;
    }


    public void setPosition(int position)
    {
        this.position = position;
    }


    public int compareTo(YEnumValue enumValue)
    {
        int diff = getPosition() - enumValue.getPosition();
        return (diff != 0) ? diff : getCode().compareTo(enumValue.getCode());
    }


    protected String getDefaultMetaTypeCode()
    {
        return getEnumTypeCode();
    }


    public void validate()
    {
        if(getCode() == null)
        {
            throw new IllegalStateException("Found enumeration value with empty 'code' attribute at " + getLoaderInfo() + ".");
        }
        super.validate();
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }
}
