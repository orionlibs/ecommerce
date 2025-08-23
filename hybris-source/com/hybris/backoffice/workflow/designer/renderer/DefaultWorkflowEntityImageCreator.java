/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.cockpitng.components.visjs.network.data.Image;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link WorkflowEntityImageCreator}
 */
public class DefaultWorkflowEntityImageCreator implements WorkflowEntityImageCreator
{
    private Base64ImageEncoder base64ImageEncoder;
    private SvgShapesRenderer svgShapesRenderer;


    @Override
    public Image createSvgImage(final ItemModel model, final String velocityTemplateLocation,
                    final String velocityTemplateLocationIE, final String cssClass)
    {
        final String resolvedSvgShapeAsString = svgShapesRenderer.getSvgShape(velocityTemplateLocation, velocityTemplateLocationIE,
                        Map.of("model", model, "class", cssClass));
        final String encodedSvgShape = base64ImageEncoder.encode(resolvedSvgShapeAsString);
        final String resolvedSelectedSvgShapeAsString = svgShapesRenderer.getSvgShape(velocityTemplateLocation,
                        velocityTemplateLocationIE, Map.of("model", model, "class", "selected"));
        final String encodedSelectedSvgShape = base64ImageEncoder.encode(resolvedSelectedSvgShapeAsString);
        return new Image.Builder().withSelected(encodedSelectedSvgShape).withUnselected(encodedSvgShape).build();
    }


    public Base64ImageEncoder getBase64ImageEncoder()
    {
        return base64ImageEncoder;
    }


    @Required
    public void setBase64ImageEncoder(final Base64ImageEncoder base64ImageEncoder)
    {
        this.base64ImageEncoder = base64ImageEncoder;
    }


    public SvgShapesRenderer getSvgShapesRenderer()
    {
        return svgShapesRenderer;
    }


    @Required
    public void setSvgShapesRenderer(final SvgShapesRenderer svgShapesRenderer)
    {
        this.svgShapesRenderer = svgShapesRenderer;
    }
}
