/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer;

import com.hybris.cockpitng.config.summaryview.jaxb.SummaryView;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.renderer.AbstractImageComponentRenderer;
import java.util.Collections;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Image;

public class DefaultSummaryViewThumbnailRenderer extends AbstractImageComponentRenderer<HtmlBasedComponent, SummaryView, Object>
{
    public static final String SCLASS_YW_SUMMARY_VIEW_IMG = "yw-summaryview-img-thumbnail";
    public static final String SCLASS_IMG_PREVIEW_POPUP = "yw-summaryview-preview-popup-image";
    public static final String PREVIEW_SUFFIX_SUMMARY_VIEW = "grid"; // we want to have a big thumbnail here like in the grid
    private String alternativeFallbackImageURL;
    private String alternativeFallbackImageMime;


    @Override
    public void render(final HtmlBasedComponent parent, final SummaryView configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        Validate.notNull("All arguments must be non-null", parent, configuration, element, dataType, widgetInstanceManager);
        appendThumbnailAndPopupPreview(element, configuration, dataType, parent, widgetInstanceManager);
    }


    public void appendThumbnailAndPopupPreview(final Object object, final SummaryView configuration, final DataType dataType,
                    final HtmlBasedComponent component, final WidgetInstanceManager widgetInstanceManager)
    {
        final ObjectPreview preview = getObjectPreview(configuration, object, dataType, widgetInstanceManager);
        if(preview != null && (!preview.isFallback() || configuration.isDisplayDefaultImage()))
        {
            final Image thumbnail = prepareThumbnail(preview.getUrl());
            component.appendChild(thumbnail);
            fireComponentRendered(thumbnail, component, configuration, object);
            if(configuration.isDisplayPreview() && !preview.isFallback())
            {
                appendPopupPreview(component, configuration, object, preview.getUrl(), thumbnail, SCLASS_IMG_PREVIEW_POPUP);
            }
        }
    }


    @Override
    protected ObjectPreview getObjectPreview(final SummaryView configuration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getObjectPreview(configuration, object, dataType, widgetInstanceManager,
                        Collections.singletonMap(ObjectPreviewService.PREFERRED_PREVIEW_SUFFIX, PREVIEW_SUFFIX_SUMMARY_VIEW));
    }


    @Override
    protected Image prepareThumbnail(final String thumbnailUrl)
    {
        final Image thumbnail = new Image(thumbnailUrl);
        thumbnail.setClass(SCLASS_YW_SUMMARY_VIEW_IMG);
        return thumbnail;
    }


    public void setAlternativeFallbackImageURL(final String alternativeFallbackImageURL)
    {
        this.alternativeFallbackImageURL = alternativeFallbackImageURL;
    }


    public String getAlternativeFallbackImageURL()
    {
        return alternativeFallbackImageURL;
    }


    public void setAlternativeFallbackImageMime(final String alternativeFallbackImageMime)
    {
        this.alternativeFallbackImageMime = alternativeFallbackImageMime;
    }


    public String getAlternativeFallbackImageMime()
    {
        return alternativeFallbackImageMime;
    }
}
