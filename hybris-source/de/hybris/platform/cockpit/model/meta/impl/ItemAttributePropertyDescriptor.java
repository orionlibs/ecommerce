package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ItemAttributePropertyDescriptor implements PropertyDescriptor
{
    private final String typeCode;
    private final String declaringTypeCode;
    private final String attributeQualifier;
    private final String qualifier;
    private final PropertyDescriptor.Multiplicity multiplicity;
    private final boolean localized;
    private PropertyDescriptor.Occurrence occurence;
    private String editorType;
    private final List<AttributeDescriptorModel> attributeDescriptors;
    private final ComposedTypeModel enclosingType;
    private final ComposedTypeModel declaringEnclosingType;
    private String selectionOf;


    public ItemAttributePropertyDescriptor(ComposedTypeModel enclosingType, List<AttributeDescriptorModel> attributeDescriptors)
    {
        this(enclosingType, null, attributeDescriptors);
    }


    public ItemAttributePropertyDescriptor(ComposedTypeModel enclosingType, ComposedTypeModel declaringEnclosingType, List<AttributeDescriptorModel> attributeDescriptors)
    {
        this.attributeDescriptors = attributeDescriptors;
        this.enclosingType = enclosingType;
        this.typeCode = this.enclosingType.getCode();
        this.declaringEnclosingType = (declaringEnclosingType == null) ? enclosingType : declaringEnclosingType;
        this.declaringTypeCode = this.declaringEnclosingType.getCode();
        StringWriter quali = new StringWriter();
        quali.append(this.declaringTypeCode);
        for(AttributeDescriptorModel ad : attributeDescriptors)
        {
            quali.append(".").append(ad.getQualifier());
        }
        this.qualifier = quali.toString();
        int qualiDelimPos = this.qualifier.indexOf(".");
        if(qualiDelimPos == -1)
        {
            throw new IllegalArgumentException("invalid attribute qualifier '" + this.qualifier + "' - expected <type code>.<qualifier>(.<qualifier>*)");
        }
        this.attributeQualifier = this.qualifier.substring(qualiDelimPos + 1);
        try
        {
            AttributeDescriptorModel attdescr = getLastAttributeDescriptor();
            this.localized = TypeTools.primitiveValue(attdescr.getLocalized());
            TypeModel adType = attdescr.getAttributeType();
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
            else
            {
                this.multiplicity = PropertyDescriptor.Multiplicity.SINGLE;
            }
            if(!TypeTools.primitiveValue(attdescr.getOptional()))
            {
                this.occurence = PropertyDescriptor.Occurrence.REQUIRED;
            }
            else if(TypeTools.primitiveValue(attdescr.getInitial()))
            {
                this.occurence = PropertyDescriptor.Occurrence.RECOMMENDED;
            }
            else
            {
                this.occurence = PropertyDescriptor.Occurrence.OPTIONAL;
            }
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalArgumentException("invalid qualifier '" + this.qualifier + "' - cannot find attribute");
        }
    }


    public ComposedTypeModel getEnclosingType()
    {
        return this.declaringEnclosingType;
    }


    public List<AttributeDescriptorModel> getAttributeDescriptors()
    {
        return this.attributeDescriptors;
    }


    public AttributeDescriptorModel getLastAttributeDescriptor()
    {
        List<AttributeDescriptorModel> ads = getAttributeDescriptors();
        return ads.get(ads.size() - 1);
    }


    public AttributeDescriptorModel getFirstAttributeDescriptor()
    {
        List<AttributeDescriptorModel> ads = getAttributeDescriptors();
        return ads.get(0);
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


    public void setOccurence(PropertyDescriptor.Occurrence occ)
    {
        this.occurence = occ;
    }


    public PropertyDescriptor.Occurrence getOccurence()
    {
        return this.occurence;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public String getAttributeQualifier()
    {
        return this.attributeQualifier;
    }


    public List<String> getAttributeQualifiers()
    {
        if(this.attributeQualifier.indexOf(".") == -1)
        {
            return Collections.singletonList(this.attributeQualifier);
        }
        return Arrays.asList(this.attributeQualifier.split("\\."));
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(getQualifier()).append("::");
        if(isLocalized())
        {
            stringbuilder.append("<l>");
        }
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[getMultiplicity().ordinal()])
        {
            case 1:
                stringbuilder.append("[");
                break;
            case 2:
                stringbuilder.append("{");
                break;
        }
        stringbuilder.append(getEditorType());
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[getMultiplicity().ordinal()])
        {
            case 1:
                stringbuilder.append("]");
                break;
            case 2:
                stringbuilder.append("}");
                break;
        }
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Occurrence[getOccurence().ordinal()])
        {
            case 1:
                stringbuilder.append("!");
                break;
            case 2:
                stringbuilder.append("!?");
                break;
            case 3:
                stringbuilder.append("?");
                break;
        }
        return stringbuilder.toString();
    }


    public int hashCode()
    {
        return getQualifier().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj != null && getQualifier().equalsIgnoreCase(((PropertyDescriptor)obj).getQualifier()));
    }


    public String getName()
    {
        return getName(null);
    }


    public String getName(String languageIso)
    {
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        String name = (String)sessionService.executeInLocalView((SessionExecutionBody)new Object(this, languageIso));
        return StringUtils.isBlank(name) ? null : name;
    }


    public String getDescription()
    {
        try
        {
            SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext(
                            JaloSession.getCurrentSession().getSessionContext());
            ctx.setAttribute("enable.language.fallback.serviceLayer", Boolean.TRUE);
            ctx.setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
            return getLastAttributeDescriptor().getDescription();
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
    }


    public boolean isReadable()
    {
        return TypeTools.primitiveValue(getLastAttributeDescriptor().getReadable());
    }


    public boolean isWritable()
    {
        return TypeTools.primitiveValue(getLastAttributeDescriptor().getWritable());
    }


    public boolean isSingleAttribute()
    {
        return !this.attributeQualifier.contains(".");
    }


    public void setSelectionOf(String selectionOf)
    {
        this.selectionOf = selectionOf;
    }


    public String getSelectionOf()
    {
        return this.selectionOf;
    }


    public boolean isRedeclared()
    {
        return this.typeCode.equals(this.declaringTypeCode);
    }
}
