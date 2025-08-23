/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingLanguage;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingType;

/**
 * Interface for evaluation of expressions with different scripting languages
 */
public interface ExpressionEvaluator
{
    /**
     * Evaluates expression on target object using passed scripting language
     *
     * @param scriptingLanguage scripting language
     * @param scriptingType scriptingType
     * @param expression expression to evaluate
     * @param target object on which expression should be evaluated
     * @param <T> type of returned value
     * @return evaluated value
     */
    <T> T evaluateExpression(ScriptingLanguage scriptingLanguage, ScriptingType scriptingType, String expression, Object target);
}
