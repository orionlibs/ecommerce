/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type.expression;

import com.hybris.cockpitng.core.expression.impl.DefaultExpressionResolverFactory;

/**
 *
 * Factory for {@link AttributeExpressionResolver}
 */
public class AttributeExpressionResolverFactory extends DefaultExpressionResolverFactory
{
    @Override
    public AttributeExpressionResolver createResolver()
    {
        return new AttributeExpressionResolver(getContextFactory());
    }
}
