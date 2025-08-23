package de.hybris.bootstrap.typesystem;

public abstract class YDescriptor extends YTypeSystemElement
{
    private String typeCode;
    private final String qualifier;
    private int modifiers;
    private boolean uniqueModifier;
    private YType type;


    public YDescriptor(YNamespace container, String qualifier, String typeCode)
    {
        super(container);
        this.qualifier = qualifier;
        this.typeCode = typeCode;
    }


    protected void redeclare(String typeCode)
    {
        this.typeCode = typeCode;
    }


    protected void redeclare(String typeCode, int modifiers)
    {
        redeclare(typeCode);
        this.modifiers = modifiers;
    }


    public String toString()
    {
        return getQualifier() + "(" + getQualifier() + "):" + getTypeCode();
    }


    public void validate()
    {
        super.validate();
        if(getQualifier() == null)
        {
            throw new IllegalStateException("Found descriptor with empty 'qualifier' attribute at " + getLoaderInfo() + ".");
        }
        getType();
    }


    public void resetCaches()
    {
        super.resetCaches();
        this.type = null;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public int getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public YType getType()
    {
        if(this.type == null)
        {
            this.type = getTypeSystem().getType(getTypeCode());
        }
        if(this.type == null)
        {
            throw new IllegalStateException("invalid descriptor " + this + " due to missing type '" + getTypeCode() + "'");
        }
        return this.type;
    }


    public void setUniqueModifier(boolean uniqueModifier)
    {
        this.uniqueModifier = uniqueModifier;
    }


    public boolean isUniqueModifier()
    {
        return this.uniqueModifier;
    }
}
