package de.hybris.bootstrap.typesystem;

import java.util.ArrayList;
import java.util.List;

public class YEnumType extends YComposedType
{
    private List<YEnumValue> values;
    private boolean dynamic = false;
    private String modelPackage;


    public YEnumType(YNamespace container, String code)
    {
        this(container, code, "EnumerationValue", "de.hybris.platform.jalo.enumeration.EnumerationValue");
    }


    public YEnumType(YNamespace container, String code, String superTypeCode, String jaloClassName)
    {
        super(container, code, superTypeCode, jaloClassName);
    }


    protected boolean allowMetaTypeInheritanceFrom(YComposedType superType)
    {
        return superType instanceof YEnumType;
    }


    protected boolean isRootEnumType()
    {
        return "EnumerationValue".equalsIgnoreCase(getSuperTypeCode());
    }


    protected String getDefaultMetaTypeCode()
    {
        return "EnumerationMetaType";
    }


    public void validate()
    {
        super.validate();
        YEnumValue def = null;
        for(YEnumValue ev : getValues())
        {
            if(ev.isDefault())
            {
                if(def != null)
                {
                    throw new IllegalStateException("Invalid enum type " + this + " since it got more than one default value ( found " + def + " and " + ev + ").");
                }
                def = ev;
            }
            for(YEnumValue ev2 : getValues())
            {
                if(ev.getCode().equals(ev2.getCode()) && ev != ev2)
                {
                    throw new IllegalStateException("Invalid enum type " +
                                    getCode() + " since it has declared value " + ev.getCode() + " twice ( found at " + ev + " and " + ev2 + ").");
                }
            }
        }
        if(!isDynamic() && getValues().isEmpty())
        {
            throw new IllegalStateException("Enumeration type " + getCode() + " is not dynamic and has no enumeration values defined (" + this + ").");
        }
    }


    public List<YEnumValue> getValues()
    {
        if(this.values == null)
        {
            this.values = new ArrayList<>(getTypeSystem().getEnumValues(getCode()));
        }
        return this.values;
    }


    public Object getDefaultValue()
    {
        for(YEnumValue ev : getValues())
        {
            if(ev.isDefault())
            {
                return ev;
            }
        }
        return null;
    }


    public boolean isDynamic()
    {
        return this.dynamic;
    }


    public void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }


    public String getModelPackage()
    {
        return this.modelPackage;
    }


    public void setModelPackage(String modelPackage)
    {
        this.modelPackage = modelPackage;
    }
}
