/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An interface of object able to parse and resolve a template
 *
 * @see com.hybris.cockpitng.core.util.template.TemplateEngine
 * @deprecated since 6.5
 */
@Deprecated(since = "6.5", forRemoval = true)
public interface TemplateEngine
{
    /**
     * Resolves specified template
     *
     * @param context
     *           an object that requires to resolve a template
     * @param templateId
     *           identity of template to be resolved
     * @param values
     *           template parameters
     * @return stream to resolved template
     * @throws IOException
     */
    InputStream applyTemplate(final Object context, final String templateId, Map<String, Object> values) throws IOException;
}
