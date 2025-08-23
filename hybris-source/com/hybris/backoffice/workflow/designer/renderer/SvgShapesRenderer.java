/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;

/**
 * Resolves svg shapes stored as velocity templates
 */
public class SvgShapesRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(SvgShapesRenderer.class);
    public static final String LOG_TAG = "svgshapegenerator";
    private VelocityEngine velocityEngine;


    /**
     * Resolves svg shapes stored as velocity templates and returns their parsed representation. Moreover, it takes an
     * extra argument for particular svg shape resource prepared for Internet Explorer and takes the proper resource
     * depending which browser is being used.
     *
     * @param shapeResource
     *           path to the resource containing svg shape in velocity template
     * @param shapeResourceIeFallback
     *           path to the resource containing svg shape in velocity template with svg elements supported by Internet
     *           Explorer
     * @param ctx
     *           with additional information necessary for parsing svg shape
     * @return parsed svg shape
     */
    public String getSvgShape(final String shapeResource, final String shapeResourceIeFallback, final Map<String, Object> ctx)
    {
        final boolean isIE = StringUtils.equals(Executions.getCurrent().getBrowser(), "ie");
        final String templateLocation = isIE ? shapeResourceIeFallback : shapeResource;
        return getSvgShape(templateLocation, ctx);
    }


    /**
     * Resolves svg shapes stored as velocity templates and returns their parsed representation
     *
     * @param shapeResource
     *           path to the resource containing svg shape in velocity template
     * @param ctx
     *           with additional information necessary for parsing svg shape
     * @return parsed svg shape
     */
    public String getSvgShape(final String shapeResource, final Map<String, Object> ctx)
    {
        final VelocityContext context = new VelocityContext(ctx);
        final StringWriter writer = new StringWriter();
        resolveResource(shapeResource).ifPresent(resolvedResource -> {
            velocityEngine.evaluate(context, writer, LOG_TAG, resolvedResource);
            try
            {
                resolvedResource.close();
            }
            catch(final IOException e)
            {
                LOG.error("Cannot close stream", e);
            }
        });
        return writer.toString();
    }


    protected Optional<InputStreamReader> resolveResource(final String resourcePath)
    {
        final InputStream resourceAsStream = SvgShapesRenderer.class.getResourceAsStream(resourcePath);
        if(resourceAsStream != null)
        {
            return Optional.of(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
        }
        return Optional.empty();
    }


    @Resource
    public void setVelocityEngine(final VelocityEngine velocityEngine)
    {
        this.velocityEngine = velocityEngine;
    }
}
