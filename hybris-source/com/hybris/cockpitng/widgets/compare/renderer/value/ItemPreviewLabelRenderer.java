/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer.value;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

/**
 * A label renderer that adds a thumbnail of element, if available, before label.
 *
 */
public class ItemPreviewLabelRenderer extends AbstractCockpitEditorRenderer<Object>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemPreviewLabelRenderer.class);
    private static final String SCLASS_IMAGE = "yw-compareview-value-image";
    private static final String SCLASS_IMAGE_PREVIEW = "yw-compareview-value-image-preview";
    private static final String SCLASS_LABEL_CONTAINER = "yw-compareview-value-label-container";
    private CockpitConfigurationService configurationService;
    private ObjectPreviewService objectPreviewService;
    private CockpitEditorRenderer<Object> labelRenderer;
    private TypeFacade typeFacade;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        Validate.notNull("Parent component and editor context are mandatory", parent, context);
        try
        {
            final String typeCode = getTypeFacade().getType(context.getInitialValue());
            final Base configuration = getConfigurationService().loadConfiguration(new DefaultConfigContext("base", typeCode),
                            Base.class);
            createPreview(context.getInitialValue(), configuration, parent);
            final Div labelContainer = new Div();
            UITools.addSClass(labelContainer, SCLASS_LABEL_CONTAINER);
            getLabelRenderer().render(labelContainer, context, listener);
            parent.appendChild(labelContainer);
        }
        catch(final CockpitConfigurationException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
    }


    protected void createPreview(final Object data, final Base configuration, final Component layout)
    {
        final ObjectPreview preview = getObjectPreviewService().getPreview(data, configuration);
        if(preview != null)
        {
            final Image image = new Image(preview.getUrl());
            UITools.addSClass(image, SCLASS_IMAGE);
            preparePreviewPopup(layout, preview, image);
            layout.appendChild(image);
        }
    }


    protected void preparePreviewPopup(final Component parent, final ObjectPreview preview, final Image target)
    {
        Validate.notNull("All arguments are mandatory", parent, preview, target);
        if(preview.isFallback())
        {
            return;
        }
        final Popup zoomPopup = new Popup();
        final Image popupImage = new Image(preview.getUrl());
        UITools.addSClass(popupImage, SCLASS_IMAGE_PREVIEW);
        zoomPopup.appendChild(popupImage);
        parent.appendChild(zoomPopup);
        target.addEventListener(Events.ON_MOUSE_OVER, event -> zoomPopup.open(target, "before_start"));
        target.addEventListener(Events.ON_MOUSE_OUT, event -> zoomPopup.close());
    }


    protected CockpitEditorRenderer<Object> getLabelRenderer()
    {
        return labelRenderer;
    }


    @Required
    public void setLabelRenderer(final CockpitEditorRenderer<Object> labelRenderer)
    {
        this.labelRenderer = labelRenderer;
    }


    protected ObjectPreviewService getObjectPreviewService()
    {
        return objectPreviewService;
    }


    @Required
    public void setObjectPreviewService(final ObjectPreviewService objectPreviewService)
    {
        this.objectPreviewService = objectPreviewService;
    }


    protected CockpitConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    @Required
    public void setConfigurationService(final CockpitConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
