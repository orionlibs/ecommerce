/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingLanguage;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingType;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.widgets.common.dynamicforms.ExpressionEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link ExpressionEvaluator} which supports {@link ScriptingLanguage#SP_EL}
 */
public class DefaultExpressionEvaluator implements ExpressionEvaluator
{
    public static final Logger LOG = LoggerFactory.getLogger(DefaultExpressionEvaluator.class);
    private ObjectValueService objectValueService;


    @Override
    public <T> T evaluateExpression(final ScriptingLanguage scriptingLanguage, final ScriptingType scriptingType,
                    final String expression, final Object target)
    {
        if(!ScriptingLanguage.SP_EL.equals(scriptingLanguage) || !ScriptingType.INLINE.equals(scriptingType))
        {
            final String msg = String.format(
                            "Cannot evaluate expression %s because chosen scripting language: %s or scripting type: %s is not "
                                            + "supported, please use ScriptingLanguage %s and ScriptingType %s instead", expression,
                            scriptingLanguage.toString(), scriptingType.toString(), ScriptingLanguage.SP_EL, ScriptingType.INLINE);
            LOG.error(msg);
            throw new UnsupportedOperationException(msg);
        }
        return objectValueService.getValue(expression, target);
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }
}
