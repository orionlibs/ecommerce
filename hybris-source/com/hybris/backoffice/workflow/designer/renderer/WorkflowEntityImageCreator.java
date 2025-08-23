/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.cockpitng.components.visjs.network.data.Image;
import de.hybris.platform.core.model.ItemModel;

/**
 * Allows to create svg image for {@link com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity}
 */
public interface WorkflowEntityImageCreator
{
    /**
     * Creates svg image
     *
     * @param model
     *           which contains necessary data for velocity template to render it correctly
     * @param velocityTemplateLocation
     *           the path to velocity template resource
     * @param velocityTemplateLocationIE
     *           the path to velocity template resource which is valid for Internet Explorer browser
     * @param cssClass
     *           which will be passed to velocity template
     * @return svg image
     */
    Image createSvgImage(final ItemModel model, final String velocityTemplateLocation, final String velocityTemplateLocationIE,
                    final String cssClass);
}
