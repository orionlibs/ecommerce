package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAttributeDescriptorModelHelperService implements AttributeDescriptorModelHelperService
{
    private TypeService typeService;


    public Class<?> getAttributeClass(AttributeDescriptorModel attributeDescriptor)
    {
        ComposedTypeModel declaringEnclosingType = attributeDescriptor.getDeclaringEnclosingType();
        Class<ItemModel> modelClass = getTypeService().getModelClass(declaringEnclosingType);
        String qualifier = attributeDescriptor.getQualifier();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(modelClass);
        try
        {
            if(!beanWrapper.isReadableProperty(qualifier))
            {
                return getClass();
            }
            Method method = beanWrapper.getPropertyDescriptor(qualifier).getReadMethod();
            Type genericReturnType = method.getGenericReturnType();
            if(genericReturnType instanceof ParameterizedType)
            {
                ParameterizedType paramType = (ParameterizedType)genericReturnType;
                return (Class)paramType.getActualTypeArguments()[0];
            }
            return method.getReturnType();
        }
        catch(InvalidPropertyException e)
        {
            return getClass();
        }
    }


    public Class<?> getDeclaringEnclosingTypeClass(AttributeDescriptorModel attributeDescriptor)
    {
        ComposedTypeModel declaringEnclosingType = attributeDescriptor.getDeclaringEnclosingType();
        return getTypeService().getModelClass(declaringEnclosingType);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
