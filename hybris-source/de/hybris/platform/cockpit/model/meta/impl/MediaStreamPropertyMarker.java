package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class MediaStreamPropertyMarker implements PropertyDescriptor
{
    private String editorType;


    @Required
    public void setEditorType(String type)
    {
        if(StringUtils.isBlank(type))
        {
            throw new IllegalArgumentException("editor type was null or empty");
        }
        this.editorType = type;
    }


    public String getEditorType()
    {
        return this.editorType;
    }


    public PropertyDescriptor.Multiplicity getMultiplicity()
    {
        return null;
    }


    public PropertyDescriptor.Occurrence getOccurence()
    {
        return null;
    }


    public String getQualifier()
    {
        return getName();
    }


    public String getAttributeQualifier()
    {
        return null;
    }


    public String getTypeCode()
    {
        return null;
    }


    public boolean isLocalized()
    {
        return false;
    }


    public String toString()
    {
        return getName();
    }


    public int hashCode()
    {
        return getQualifier().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj != null && getQualifier().equalsIgnoreCase(((PropertyDescriptor)obj).getQualifier()));
    }


    public String getName(String languageIso)
    {
        return getName();
    }


    public String getName()
    {
        return "MediaContent";
    }


    public String getDescription()
    {
        return "Invented only for media related property";
    }


    public boolean isReadable()
    {
        return true;
    }


    public boolean isWritable()
    {
        return true;
    }


    public String getSelectionOf()
    {
        return null;
    }
}
