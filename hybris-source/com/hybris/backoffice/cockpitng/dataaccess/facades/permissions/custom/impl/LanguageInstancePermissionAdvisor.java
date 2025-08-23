/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.impl;

import com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.InstancePermissionAdvisor;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class LanguageInstancePermissionAdvisor implements InstancePermissionAdvisor<LanguageModel>
{
    private ModelService modelService;


    @Override
    public boolean canModify(final LanguageModel instance)
    {
        return true;
    }


    @Override
    public boolean canDelete(final LanguageModel instance)
    {
        if(!modelService.isNew(instance))
        {
            final ItemModelContext context = instance.getItemModelContext();
            if(context.isDirty(LanguageModel.ACTIVE))
            {
                return BooleanUtils.isNotTrue(context.<Boolean>getOriginalValue(LanguageModel.ACTIVE));
            }
        }
        return BooleanUtils.isNotTrue(instance.getActive());
    }


    @Override
    public boolean isApplicableTo(final Object instance)
    {
        return instance instanceof LanguageModel;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}
