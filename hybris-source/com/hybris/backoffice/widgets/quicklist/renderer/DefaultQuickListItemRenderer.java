/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicklist.renderer;

import com.hybris.cockpitng.config.quicklist.jaxb.QuickList;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.renderer.AbstractImageComponentRenderer;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

public class DefaultQuickListItemRenderer extends AbstractImageComponentRenderer<HtmlBasedComponent, QuickList, Object>
                implements QuickListItemRenderer
{
    public static final String SCLASS_YW_QUICK_LIST_TILE_SUBTITLE_CONTAINER = "yw-quicklist-tile-subtitle-container";
    public static final String SCLASS_YW_QUICK_LIST_TILE_SUBTITLE = "yw-quicklist-tile-subtitle";
    public static final String SCLASS_YW_QUICK_LIST_TILE_TITLE = "yw-quicklist-tile-title";
    public static final String SCLASS_YW_QUICK_LIST_TILE_TITLE_ONLY = "yw-quicklist-tile-title-only";
    public static final String SCLASS_YW_QUICK_LIST_TILE_IMG = "yw-quicklist-tile-thumbnail";
    public static final String SCLASS_IMG_PREVIEW_POPUP = "ye-quicklist-preview-popup-image";
    public static final String SCLASS_REMOVE_BUTTON = "ye-delete-btn";
    public static final String PREVIEW_SUFFIX_QUICK_LIST = "quicklist";
    public static final String ATTRIBUTE_KEY_CLICKABLE = "clickable";
    public static final String ATTRIBUTE_CLICKABLE_TITLE = "title";
    public static final String ATTRIBUTE_CLICKABLE_REMOVE_BUTTON = "removeButton";
    private LabelService labelService;
    private WidgetRenderingUtils widgetRenderingUtils;
    private boolean allowRemove;


    @Override
    public void render(final HtmlBasedComponent parent, final QuickList configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        Validate.notNull("All arguments must be non-null", parent, configuration, element, dataType, widgetInstanceManager);
        appendLabel(element, parent, configuration);
        appendThumbnailAndPopupPreview(element, configuration, dataType, parent, widgetInstanceManager);
        appendDescriptionLabels(element, parent, configuration, dataType);
        if(isAllowRemove())
        {
            appendRemoveButton(element, parent, configuration);
        }
        fireComponentRendered(parent, configuration, element);
    }


    protected void appendRemoveButton(final Object object, final HtmlBasedComponent parent, final QuickList configuration)
    {
        final Button removeButton = new Button();
        removeButton.setSclass(SCLASS_REMOVE_BUTTON);
        removeButton.setAttribute(ATTRIBUTE_KEY_CLICKABLE, ATTRIBUTE_CLICKABLE_REMOVE_BUTTON);
        parent.appendChild(removeButton);
        fireComponentRendered(removeButton, parent, configuration, object);
    }


    protected void appendDescriptionLabels(final Object element, final HtmlBasedComponent parent, final QuickList configuration,
                    final DataType dataType)
    {
        final Div subtitleContainer = new Div();
        subtitleContainer.setSclass(SCLASS_YW_QUICK_LIST_TILE_SUBTITLE_CONTAINER);
        parent.appendChild(subtitleContainer);
        configuration.getAttribute().stream()
                        .map(expr -> getWidgetRenderingUtils().getAttributeLabel(element, dataType, expr.getQualifier()).getLabel())
                        .forEach(value -> {
                            final Label label = new Label();
                            final String titleAttrValue = StringUtils.defaultString(value);
                            label.setValue(titleAttrValue);
                            label.setTooltiptext(titleAttrValue);
                            label.setSclass(SCLASS_YW_QUICK_LIST_TILE_SUBTITLE);
                            subtitleContainer.appendChild(label);
                        });
    }


    protected void appendLabel(final Object object, final HtmlBasedComponent tile, final QuickList configuration)
    {
        final Label label = new Label();
        final String title = getLabelService().getObjectLabel(object);
        label.setValue(title);
        label.setTooltiptext(title);
        label.setSclass(SCLASS_YW_QUICK_LIST_TILE_TITLE);
        label.setAttribute(ATTRIBUTE_KEY_CLICKABLE, ATTRIBUTE_CLICKABLE_TITLE);
        tile.appendChild(label);
        fireComponentRendered(label, tile, configuration, object);
    }


    protected void appendThumbnailAndPopupPreview(final Object object, final QuickList configuration, final DataType dataType,
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
        else
        {
            UITools.modifySClass(component, SCLASS_YW_QUICK_LIST_TILE_TITLE_ONLY, true);
        }
    }


    @Override
    protected ObjectPreview getObjectPreview(final QuickList configuration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getObjectPreview(configuration, object, dataType, widgetInstanceManager,
                        Collections.singletonMap(ObjectPreviewService.PREFERRED_PREVIEW_SUFFIX, PREVIEW_SUFFIX_QUICK_LIST));
    }


    @Override
    protected Image prepareThumbnail(final String thumbnailUrl)
    {
        final Image thumbnail = new Image(thumbnailUrl);
        thumbnail.setClass(SCLASS_YW_QUICK_LIST_TILE_IMG);
        return thumbnail;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Required
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }


    @Override
    public boolean isOpenItemHyperlink(final Component component)
    {
        return ATTRIBUTE_CLICKABLE_TITLE.equalsIgnoreCase((String)component.getAttribute(ATTRIBUTE_KEY_CLICKABLE));
    }


    @Override
    public boolean isRemoveItemButton(final Component component)
    {
        return ATTRIBUTE_CLICKABLE_REMOVE_BUTTON.equalsIgnoreCase((String)component.getAttribute(ATTRIBUTE_KEY_CLICKABLE));
    }


    public boolean isAllowRemove()
    {
        return allowRemove;
    }


    public void setAllowRemove(final boolean allowRemove)
    {
        this.allowRemove = allowRemove;
    }
}
