/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template;

import java.util.Map;

/**
 * Provides additional values for templates to be used
 *
 * @see com.hybris.cockpitng.core.util.template.TemplateValuesProvider
 * @deprecated 6.5
 */
@Deprecated(since = "6.5", forRemoval = true)
public interface TemplateValuesProvider
{
    Map<String, Object> provideTemplateValues(final Object context, final String templateId);
}
