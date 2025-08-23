/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.template;

import java.util.Map;

/**
 * Provides additional values for templates to be used
 */
public interface TemplateValuesProvider
{
    /**
     * Method allows to define values available in template.
     *
     * @param context
     *           object against which a template is to be resolved
     * @param templateId
     *           identity of a template to be resolved
     * @return map of values available in template
     */
    Map<String, Object> provideTemplateValues(final Object context, final String templateId);
}
