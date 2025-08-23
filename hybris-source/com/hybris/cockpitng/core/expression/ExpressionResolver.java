/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.expression;

import java.util.Locale;
import java.util.Map;
import org.springframework.expression.ExpressionException;

/**
 * Resolves SpEL expressions
 */
public interface ExpressionResolver
{
    /**
     * Evaluates provided expression in regards to specified object. Current {@link Locale} is used during evaluation.
     *
     * @return value of evaluation
     */
    <T> T getValue(Object sourceObject, String expression) throws ExpressionException;


    /**
     * Evaluates provided expression in regards to specified object. The returning map holds a locale as key and the
     * value of this map holds the corresponding localized value of the expression.
     *
     * @param sourceObject object on which evaluation is performed
     * @param expression given expression
     * @param variables variables passed to {@link org.springframework.expression.EvaluationContext#setVariable(String, Object)}
     *
     * @return value of evaluation
     */
    <T> T getValue(Object sourceObject, String expression, Map<String, Object> variables) throws ExpressionException;


    /**
     * Evaluates value type provided expression in regards to specified object.
     *
     * @param sourceObject object on which evaluation is performed
     * @param expression given expression
     * @param variables variables passed to {@link org.springframework.expression.EvaluationContext#setVariable(String, Object)}
     *
     * @return value type of evaluation
     */
    <T> Class<T> getValueType(Object sourceObject, String expression, Map<String, Object> variables) throws ExpressionException;


    /**
     * Evaluates value type provided expression in regards to specified object.
     *
     * @param sourceObject object on which evaluation is performed
     * @param expression given expression
     *
     * @return value type of evaluation
     */
    <T> Class<T> getValueType(Object sourceObject, String expression) throws ExpressionException;


    /**
     * Sets the value for the specified expression.
     */
    void setValue(Object sourceObject, String expression, Object value);


    /**
     * Sets the value for the specified expression and locale. Assumes the the object has a setter with a {@link Locale} as
     * second argument for the given property.
     */
    void setValue(Object sourceObject, String expression, Object value, Locale locale);
}
