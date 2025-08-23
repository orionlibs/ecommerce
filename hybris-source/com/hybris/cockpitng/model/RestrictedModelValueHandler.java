/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.model;

import com.hybris.cockpitng.core.model.impl.DefaultModelValueHandler;
import com.hybris.cockpitng.dataaccess.services.impl.expression.RestrictedAccessException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.zkoss.util.resource.Labels;

public class RestrictedModelValueHandler extends DefaultModelValueHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(RestrictedModelValueHandler.class);
    private static final String LABEL_RESTRICTED_DATA = "data.restricted";


    @Override
    protected Object getValueFromResolver(final Object model, final String key, final Map<String, Object> variables)
    {
        try
        {
            return super.getValueFromResolver(model, key, variables);
        }
        catch(final EvaluationException e)
        {
            if(isAccessRestricted(e))
            {
                LOG.warn("No read access to expression '{}' for type '{}'", key, model.getClass().getSimpleName());
                return getAccessRestrictedLabel();
            }
            else
            {
                throw e;
            }
        }
    }


    protected String getAccessRestrictedLabel()
    {
        return Labels.getLabel(LABEL_RESTRICTED_DATA);
    }


    @Override
    protected void setValueThroughResolver(final Object model, final String key, final Object value)
    {
        try
        {
            super.setValueThroughResolver(model, key, value);
        }
        catch(final EvaluationException e)
        {
            if(isAccessRestricted(e))
            {
                LOG.error("No write access to expression '{}' for type '{}'", key, model.getClass().getSimpleName());
            }
            else
            {
                throw e;
            }
        }
    }


    private boolean isAccessRestricted(final EvaluationException exception)
    {
        return exception.getCause() instanceof RestrictedAccessException;
    }
}
