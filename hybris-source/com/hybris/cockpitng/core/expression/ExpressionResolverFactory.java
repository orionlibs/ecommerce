/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.expression;

/**
 * Factory for {@link ExpressionResolver}
 *
 */
public interface ExpressionResolverFactory
{
    /**
     *
     * @return new instance of expression resolver
     */
    ExpressionResolver createResolver();
}
