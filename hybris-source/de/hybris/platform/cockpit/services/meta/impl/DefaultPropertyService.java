package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.PropertyService;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPropertyService implements PropertyService
{
    private TypeService typeService;


    public String getMultiplicityString(PropertyDescriptor propertyDescriptor)
    {
        String ret = "default";
        PropertyDescriptor.Multiplicity multiplicity = propertyDescriptor.getMultiplicity();
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[multiplicity.ordinal()])
        {
            case 1:
                ret = "default";
                break;
            case 2:
                ret = "range";
                break;
            case 3:
                ret = "multi";
                break;
            case 4:
                ret = "multi";
                break;
        }
        return ret;
    }


    public String getDefaultEditorType(String typeCode, boolean localized)
    {
        TypeModel type = this.typeService.getTypeForCode(typeCode);
        TypeModel basicType = null;
        if(localized && type instanceof MapTypeModel)
        {
            type = ((MapTypeModel)type).getReturntype();
        }
        if(type instanceof de.hybris.platform.core.model.type.AtomicTypeModel || type instanceof de.hybris.platform.core.model.type.ComposedTypeModel)
        {
            basicType = type;
        }
        else if(type instanceof CollectionTypeModel)
        {
            basicType = ((CollectionTypeModel)type).getElementType();
        }
        else if(type instanceof MapTypeModel)
        {
            basicType = ((MapTypeModel)type).getReturntype();
        }
        String returnType = "DUMMY";
        if(basicType instanceof de.hybris.platform.core.model.type.AtomicTypeModel)
        {
            String code = basicType.getCode();
            if("java.lang.String".equals(code))
            {
                returnType = "TEXT";
            }
            else if("java.lang.Integer".equals(code))
            {
                returnType = "INTEGER";
            }
            else if("java.lang.Long".equals(code))
            {
                returnType = "LONG";
            }
            else if("de.hybris.platform.core.PK".equals(code))
            {
                returnType = "PK";
            }
            else if("java.lang.Double".equals(code))
            {
                returnType = "DECIMAL";
            }
            else if("java.lang.Float".equals(code))
            {
                returnType = "FLOAT";
            }
            else if("java.lang.Boolean".equals(code))
            {
                returnType = "BOOLEAN";
            }
            else if("java.util.Date".equals(code))
            {
                returnType = "DATE";
            }
        }
        else if(basicType instanceof de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel)
        {
            returnType = "ENUM";
        }
        else if(basicType instanceof de.hybris.platform.core.model.type.ComposedTypeModel)
        {
            returnType = "REFERENCE";
        }
        return returnType;
    }


    public boolean isInitial(PropertyDescriptor propertyDescriptor)
    {
        boolean initial = false;
        if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
        {
            initial = BooleanUtils.toBoolean(((ItemAttributePropertyDescriptor)propertyDescriptor).getLastAttributeDescriptor()
                            .getInitial());
        }
        return initial;
    }


    public boolean isPartof(PropertyDescriptor propertyDescriptor)
    {
        boolean partof = false;
        if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
        {
            partof = BooleanUtils.toBoolean(((ItemAttributePropertyDescriptor)propertyDescriptor).getLastAttributeDescriptor()
                            .getPartOf());
        }
        return partof;
    }


    public boolean isMandatory(PropertyDescriptor propertyDescriptor, boolean creationMode)
    {
        PropertyDescriptor.Occurrence occurrence = propertyDescriptor.getOccurence();
        boolean mandatory = (PropertyDescriptor.Occurrence.REQUIRED.equals(occurrence) && propertyDescriptor.isWritable());
        if(!mandatory && creationMode)
        {
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
            {
                ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
                mandatory = (PropertyDescriptor.Occurrence.REQUIRED.equals(occurrence) && (BooleanUtils.toBoolean(iapd.getLastAttributeDescriptor().getInitial()) || propertyDescriptor.isWritable()));
            }
        }
        return mandatory;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
