/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.expression;

import java.util.Map;
import org.springframework.expression.EvaluationContext;

/**
 * Factory for {@link EvaluationContext}
 *
 */
public interface EvaluationContextFactory
{
    /**
     * Creates evaluation context.
     *
     * @return new instance if evaluation context
     */
    EvaluationContext createContext();


    /**
     * Creates evaluation context according to the given parameters.
     *
     * @param contextParameters parameters to be passed to {@link EvaluationContext#setVariable(String, Object)}
     *
     * @return new instance of evaluation context
     */
    EvaluationContext createContext(Map<String, Object> contextParameters);


    /**
     * Creates evaluation context according to the given parameters.
     *
     * @param rootObject set as root object of {@link EvaluationContext}
     * @param contextParameters parameters to be passed to {@link EvaluationContext#setVariable(String, Object)}
     *
     * @return new instance of evaluation context
     */
    EvaluationContext createContext(Object rootObject, Map<String, Object> contextParameters);
}
