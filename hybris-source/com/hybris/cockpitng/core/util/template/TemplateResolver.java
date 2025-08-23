/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.template;

import java.io.IOException;
import java.io.InputStream;

/**
 * Gets a template on basis of its id
 *
 */
public interface TemplateResolver
{
    /**
     * Gets a template identified by provided id
     *
     * @param templateId identity of template
     * @return template
     */
    InputStream resolveTemplate(final String templateId) throws IOException;
}
