/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.renderer;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.ImagePreview;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer.DefaultPreviewListCellRenderer;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

public abstract class AbstractImageComponentRenderer<P extends Component, C, D> extends AbstractWidgetComponentRenderer<P, C, D>
{
    public static final String SCLASS_YW_TITLE_ONLY = "yw-tile-title-only";
    private static final String SCLASS_IMAGE_CONTAINER = "yw-image-container";
    private ObjectPreviewService objectPreviewService;
    private Properties extensionsToMime;
    private boolean displayDefaultImage;
    private String defaultImage;


    public String getDefaultImage()
    {
        return defaultImage;
    }


    public void setDefaultImage(final String defaultImage)
    {
        this.defaultImage = defaultImage;
    }


    public boolean isDisplayDefaultImage()
    {
        return displayDefaultImage;
    }


    public void setDisplayDefaultImage(final boolean displayDefaultImage)
    {
        this.displayDefaultImage = displayDefaultImage;
    }


    protected void appendPopupPreview(final P listcell, final C configuration, final D o, final String imageUrl,
                    final Image target, final String style)
    {
        final Popup popup = preparePreviewPopup(listcell, imageUrl, target, style);
        if(popup != null)
        {
            fireComponentRendered(popup, listcell, configuration, o);
        }
    }


    protected Popup preparePreviewPopup(final P parent, final String imageUrl, final Image target, final String style)
    {
        final Popup zoomPopup = new Popup();
        final Image popupImage = new Image(imageUrl);
        UITools.modifySClass(popupImage, style, true);
        zoomPopup.appendChild(popupImage);
        parent.appendChild(zoomPopup);
        target.addEventListener(Events.ON_MOUSE_OVER, event -> zoomPopup.open(target, "before_start"));
        target.addEventListener(Events.ON_MOUSE_OUT, event -> zoomPopup.close());
        return zoomPopup;
    }


    protected ObjectPreview getObjectPreview(final C componentConfiguration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getObjectPreview(componentConfiguration, object, dataType, widgetInstanceManager, new DefaultCockpitContext());
    }


    protected ObjectPreview getObjectPreview(final C componentConfiguration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final CockpitContext context)
    {
        final Base configuration = loadBaseConfiguration(dataType.getCode(), widgetInstanceManager);
        return objectPreviewService.getPreview(object, configuration, context);
    }


    protected Function<C, ImagePreview> getConfigurationMapping()
    {
        return config -> (ImagePreview)config;
    }


    protected ObjectPreview getObjectPreview(final C componentConfiguration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final Map<String, Object> contextValues)
    {
        final DefaultCockpitContext context = new DefaultCockpitContext();
        context.setParameters(contextValues);
        final ObjectPreview preview = getObjectPreview(componentConfiguration, object, dataType, widgetInstanceManager, context);
        setDisplayDefaultImage(getConfigurationMapping().apply(componentConfiguration).isDisplayDefaultImage());
        setDefaultImage(getConfigurationMapping().apply(componentConfiguration).getDefaultImage());
        if(preview != null && !preview.isFallback())
        {
            return preview;
        }
        if(isDisplayDefaultImage())
        {
            final String image = getDefaultImage();
            final boolean isInUnknownGroup = preview != null && ObjectPreview.GROUP_UNKNOWN.equalsIgnoreCase(preview.getGroup());
            if(StringUtils.isNotBlank(image) && (preview == null || isInUnknownGroup))
            {
                final String mime = findMimeByExtension(image);
                if(StringUtils.isNotBlank(mime))
                {
                    return new ObjectPreview(image, mime, true);
                }
            }
        }
        return preview;
    }


    protected String findMimeByExtension(final String image)
    {
        final String extension = UITools.extractExtension(image);
        if(extension == null)
        {
            return StringUtils.EMPTY;
        }
        return StringUtils.defaultIfBlank(getExtensionsToMime().getProperty(extension), extension);
    }


    protected Base loadBaseConfiguration(final String typeCode, final WidgetInstanceManager wim)
    {
        Base config = null;
        final DefaultConfigContext configContext = new DefaultConfigContext("base");
        configContext.setType(typeCode);
        try
        {
            config = wim.loadConfiguration(configContext, Base.class);
            if(config == null)
            {
                DefaultPreviewListCellRenderer.LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationNotFoundException ccnfe)
        {
            DefaultPreviewListCellRenderer.LOG.debug("Could not find UI configuration for given context (" + configContext + ").",
                            ccnfe);
        }
        catch(final CockpitConfigurationException cce)
        {
            DefaultPreviewListCellRenderer.LOG.error("Could not load cockpit config for the given context '" + configContext + "'.",
                            cce);
        }
        return config;
    }


    public void appendThumbnailAndPopupPreview(final D object, final ImagePreview configuration, final DataType dataType,
                    final P component, final WidgetInstanceManager widgetInstanceManager)
    {
        final ObjectPreview preview = getObjectPreview((C)configuration, object, dataType, widgetInstanceManager);
        if(preview != null && configuration.isDisplayThumbnail() && (!preview.isFallback() || configuration.isDisplayDefaultImage()))
        {
            final Div container = new Div();
            UITools.addSClass(container, SCLASS_IMAGE_CONTAINER);
            final Image thumbnail = prepareThumbnail(preview.getUrl());
            container.appendChild(thumbnail);
            component.appendChild(container);
            fireComponentRendered(thumbnail, component, (C)configuration, object);
            if(configuration.isDisplayPreview() && !preview.isFallback())
            {
                appendPopupPreview(component, (C)configuration, object, preview.getUrl(), thumbnail, getPreviewPopupSclass());
            }
        }
        else if(component instanceof HtmlBasedComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)component, SCLASS_YW_TITLE_ONLY, true);
        }
    }


    protected Image prepareThumbnail(final String thumbnailUrl)
    {
        final Image thumbnail = new Image(thumbnailUrl);
        thumbnail.setClass(getThumbnailSclass());
        return thumbnail;
    }


    protected String getThumbnailSclass()
    {
        return StringUtils.EMPTY;
    }


    protected String getPreviewPopupSclass()
    {
        return StringUtils.EMPTY;
    }


    public Properties getExtensionsToMime()
    {
        return extensionsToMime;
    }


    @Required
    public void setExtensionsToMime(final Properties extensionsToMime)
    {
        this.extensionsToMime = extensionsToMime;
    }


    public ObjectPreviewService getObjectPreviewService()
    {
        return objectPreviewService;
    }


    @Required
    public void setObjectPreviewService(final ObjectPreviewService objectPreviewService)
    {
        this.objectPreviewService = objectPreviewService;
    }
}
