/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Template engine that uses Apache Velocity
 *
 * @see com.hybris.cockpitng.core.util.template.impl.VelocityTemplateEngine
 * @deprecated since 6.5
 */
@Deprecated(since = "6.5", forRemoval = true)
public class VelocityTemplateEngine extends AbstractTemplateEngine
{
    @Override
    protected InputStream applyTemplate(final Object context, final String templateId, final InputStream template,
                    final Map<String, Object> values) throws IOException
    {
        final VelocityContext ctx = new VelocityContext();
        values.entrySet().forEach(entry -> ctx.put(entry.getKey(), entry.getValue()));
        final StringWriter writer = new StringWriter();
        final Reader reader = new InputStreamReader(template);
        Velocity.evaluate(ctx, writer, templateId, reader);
        return new ByteArrayInputStream(writer.toString().getBytes());
    }
}
