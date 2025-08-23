/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.model;

import com.hybris.cockpitng.core.impl.ModelValueHandlerFactory;
import com.hybris.cockpitng.core.model.ModelValueHandler;

public class RestrictedModelValueHandlerFactory extends ModelValueHandlerFactory
{
    @Override
    public ModelValueHandler createModelValueHandler()
    {
        final RestrictedModelValueHandler modelValueHandler = new RestrictedModelValueHandler();
        modelValueHandler.setExpressionResolverFactory(getExpressionResolverFactory());
        return modelValueHandler;
    }
}
