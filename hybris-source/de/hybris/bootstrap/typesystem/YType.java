package de.hybris.bootstrap.typesystem;

public abstract class YType extends YTypeSystemElement
{
    private final String code;
    private String defaultValueDef;


    public YType(YNamespace container, String code)
    {
        super(container);
        this.code = code;
    }


    public String toString()
    {
        return getCode() + "::" + getCode();
    }


    public void validate()
    {
        super.validate();
        if(getCode() == null)
        {
            throw new IllegalStateException("Found type with empty 'code' attribute at " + getLoaderInfo() + ".");
        }
        if(getTypeSystem().getType(getCode()) != this)
        {
            throw new IllegalStateException("invalid type " + this + " since it doesnt seem to be assigned to type system");
        }
    }


    public String getCode()
    {
        return this.code;
    }


    public void setDefaultValueDefinition(String defaultValueDef)
    {
        this.defaultValueDef = defaultValueDef;
    }


    public String getDefaultValueDefinition()
    {
        return this.defaultValueDef;
    }


    public abstract String getJavaClassName();


    public abstract boolean isAssignableFrom(YType paramYType);
}
