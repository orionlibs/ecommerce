package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class WidgetParameterPropertyDescriptor implements PropertyDescriptor
{
    private final WidgetParameterModel widgetParameter;
    private final String qualifier;
    private final boolean localized;
    private final PropertyDescriptor.Multiplicity multiplicity;
    private final PropertyDescriptor.Occurrence occurence;
    private String editorType;


    public WidgetParameterPropertyDescriptor(ComposedTypeModel enclosingType, WidgetParameterModel parameter)
    {
        this.localized = false;
        this.widgetParameter = parameter;
        this.qualifier = "_widget_" + enclosingType.getCode() + "." + parameter.getName();
        TypeModel adType = parameter.getType();
        if(this.localized && adType instanceof MapTypeModel)
        {
            adType = ((MapTypeModel)adType).getReturntype();
        }
        if(adType instanceof CollectionTypeModel)
        {
            if(((CollectionTypeModel)adType).getTypeOfCollection() == TypeOfCollectionEnum.SET)
            {
                this.multiplicity = PropertyDescriptor.Multiplicity.SET;
            }
            else
            {
                this.multiplicity = PropertyDescriptor.Multiplicity.LIST;
            }
        }
        else if(StringUtils.equals("java.util.Collection", parameter.getTargetType()))
        {
            this.multiplicity = PropertyDescriptor.Multiplicity.LIST;
        }
        else
        {
            this.multiplicity = PropertyDescriptor.Multiplicity.SINGLE;
            if(parameter.getType() instanceof de.hybris.platform.core.model.type.AtomicTypeModel && "java.lang.Boolean".equals(parameter.getTargetType()))
            {
                this.occurence = PropertyDescriptor.Occurrence.REQUIRED;
                return;
            }
        }
        this.occurence = PropertyDescriptor.Occurrence.OPTIONAL;
    }


    public String getName(String languageIso)
    {
        return this.widgetParameter.getName();
    }


    public boolean isReadable()
    {
        return true;
    }


    public boolean isWritable()
    {
        return true;
    }


    public String getDescription()
    {
        return "";
    }


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
        return this.multiplicity;
    }


    public String getName()
    {
        return this.widgetParameter.getName();
    }


    public PropertyDescriptor.Occurrence getOccurence()
    {
        return this.occurence;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public String getSelectionOf()
    {
        return null;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public WidgetParameterModel getWidgetParameter()
    {
        return this.widgetParameter;
    }


    public int hashCode()
    {
        return getQualifier().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj != null && getQualifier().equalsIgnoreCase(((PropertyDescriptor)obj).getQualifier()));
    }
}
