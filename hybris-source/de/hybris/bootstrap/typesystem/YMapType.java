package de.hybris.bootstrap.typesystem;

public class YMapType extends YType
{
    private final String argumentTypeCode;
    private final String returnTypeCode;
    private YType argumentType;
    private YType returnType;


    public YMapType(YNamespace container, String code, String argumentTypeCode, String returnTypeCode)
    {
        super(container, code);
        if(argumentTypeCode == null)
        {
            throw new IllegalArgumentException("argument type of " + code + " was null");
        }
        if(returnTypeCode == null)
        {
            throw new IllegalArgumentException("return type of " + code + " was null");
        }
        this.argumentTypeCode = argumentTypeCode;
        this.returnTypeCode = returnTypeCode;
    }


    protected String getDefaultMetaTypeCode()
    {
        return "MapType";
    }


    protected Class getLangItemClass()
    {
        return getTypeSystem().resolveClass(this, "de.hybris.platform.jalo.c2l.Language");
    }


    public boolean isLocalizationMap()
    {
        if(!(getArgumentType() instanceof YComposedType))
        {
            return false;
        }
        Class langItemCl = getLangItemClass();
        if(langItemCl != null)
        {
            return langItemCl.isAssignableFrom(((YComposedType)getArgumentType()).getJaloClass());
        }
        return "de.hybris.platform.jalo.c2l.Language"
                        .equalsIgnoreCase(((YComposedType)getArgumentType()).getJaloClassName());
    }


    public void validate()
    {
        super.validate();
        getArgumentType();
        getReturnType();
    }


    public String getArgumentTypeCode()
    {
        return this.argumentTypeCode;
    }


    public YType getArgumentType()
    {
        if(this.argumentType == null)
        {
            this.argumentType = getTypeSystem().getType(getArgumentTypeCode());
        }
        if(this.argumentType == null)
        {
            throw new IllegalStateException("invalid map type " + this + " due to missing argument type '" + getArgumentTypeCode() + "'");
        }
        return this.argumentType;
    }


    public String getReturnTypeCode()
    {
        return this.returnTypeCode;
    }


    public YType getReturnType()
    {
        if(this.returnType == null)
        {
            this.returnType = getTypeSystem().getType(getReturnTypeCode());
        }
        if(this.returnType == null)
        {
            throw new IllegalStateException("invalid map type " + this + " due to missing return type '" +
                            getReturnTypeCode() + "'");
        }
        return this.returnType;
    }


    public String getJavaClassName()
    {
        return "java.util.Map<" + getArgumentType().getJavaClassName() + "," + getReturnType().getJavaClassName() + ">";
    }


    public boolean isAssignableFrom(YType type)
    {
        return (type instanceof YMapType && (
                        equals(type) || (getArgumentType().isAssignableFrom(((YMapType)type).getArgumentType()) && getReturnType()
                                        .isAssignableFrom(((YMapType)type).getReturnType()))));
    }


    public boolean equals(Object type)
    {
        return (type instanceof YMapType && ((YMapType)type).getCode().equals(getCode()));
    }


    public int hashCode()
    {
        return getCode().hashCode();
    }
}
