package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPropertyDescriptor implements PropertyDescriptor
{
    private String qualifier;
    private boolean localized = false;
    private String type = "String";
    private PropertyDescriptor.Multiplicity multi = PropertyDescriptor.Multiplicity.SINGLE;
    private PropertyDescriptor.Occurrence occurence = PropertyDescriptor.Occurrence.OPTIONAL;


    public String getQualifier()
    {
        return this.qualifier;
    }


    @Required
    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public void setLocalized(boolean loc)
    {
        this.localized = loc;
    }


    public PropertyDescriptor.Multiplicity getMultiplicity()
    {
        return (this.multi != null) ? this.multi : PropertyDescriptor.Multiplicity.SINGLE;
    }


    public void setMultiplicity(PropertyDescriptor.Multiplicity multiplicity)
    {
        if(multiplicity == null)
        {
            throw new IllegalArgumentException("multiplicity cannot be null");
        }
        this.multi = multiplicity;
    }


    public String getEditorType()
    {
        return (this.type != null) ? this.type : "String";
    }


    @Required
    public void setEditorType(String type)
    {
        if(type == null)
        {
            throw new IllegalArgumentException("editor type cannot be null");
        }
        this.type = type;
    }


    public void setOccurence(PropertyDescriptor.Occurrence occurence)
    {
        if(occurence == null)
        {
            throw new IllegalArgumentException("occurence cannot be null");
        }
        this.occurence = occurence;
    }


    public PropertyDescriptor.Occurrence getOccurence()
    {
        return (this.occurence != null) ? this.occurence : PropertyDescriptor.Occurrence.OPTIONAL;
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getQualifier()).append("::");
        if(isLocalized())
        {
            stringBuilder.append("<l>");
        }
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[getMultiplicity().ordinal()])
        {
            case 1:
                stringBuilder.append("[");
                break;
            case 2:
                stringBuilder.append("{");
                break;
        }
        stringBuilder.append(getEditorType());
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[getMultiplicity().ordinal()])
        {
            case 1:
                stringBuilder.append("]");
                break;
            case 2:
                stringBuilder.append("}");
                break;
        }
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Occurrence[getOccurence().ordinal()])
        {
            case 1:
                stringBuilder.append("!");
                break;
            case 2:
                stringBuilder.append("!?");
                break;
            case 3:
                stringBuilder.append("?");
                break;
        }
        return stringBuilder.toString();
    }


    public int hashCode()
    {
        return (getQualifier().hashCode() ^ getEditorType().hashCode() ^ getMultiplicity().hashCode()) * (isLocalized() ? 2 : 1);
    }


    public boolean equals(Object obj)
    {
        return (obj != null && getQualifier().equalsIgnoreCase(((PropertyDescriptor)obj).getQualifier()) &&
                        getEditorType().equalsIgnoreCase(((PropertyDescriptor)obj).getEditorType()) &&
                        isLocalized() == ((PropertyDescriptor)obj).isLocalized() &&
                        getMultiplicity().equals(((PropertyDescriptor)obj).getMultiplicity()));
    }


    public String getName()
    {
        return this.qualifier;
    }


    public String getDescription()
    {
        return this.qualifier;
    }


    public String getSelectionOf()
    {
        return null;
    }
}
