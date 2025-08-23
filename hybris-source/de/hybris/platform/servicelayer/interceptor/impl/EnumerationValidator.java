package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public class EnumerationValidator implements ValidateInterceptor
{
    private TypeService typeService;


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof EnumerationValueModel && ctx.isNew(model))
        {
            EnumerationValueModel enumValueModel = (EnumerationValueModel)model;
            EnumerationMetaTypeModel enumMetaTypeModel = this.typeService.getEnumerationTypeForCode(enumValueModel
                            .getItemtype());
            if(!Boolean.TRUE.equals(enumMetaTypeModel.getDynamic()))
            {
                throw new InterceptorException("Enum type " + enumMetaTypeModel
                                .getCode() + " is not dynamic - can not create new enum value " + enumValueModel
                                .getCode() + ". If you want to add a new value to this type you have to define the enum type as non dynamic at items.xml (needs system update afterwards). ");
            }
        }
    }
}
