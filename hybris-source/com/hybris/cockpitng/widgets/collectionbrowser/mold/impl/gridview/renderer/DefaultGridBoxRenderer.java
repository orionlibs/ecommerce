/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.gridview.renderer;

import com.hybris.cockpitng.components.grid.GridBox;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.ComponentMarkingUtils;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractImageBoxRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;

public class DefaultGridBoxRenderer extends AbstractImageBoxRenderer<GridView>
{
    public static final String SCLASS_YW_GRID_VIEW_TILE = "yw-grid-view-tile";
    public static final String SCLASS_YW_GRID_VIEW_TILE_SELECTION = "yw-grid-view-tile-selection";
    public static final String SCLASS_YW_GRID_VIEW_TILE_NARROW = "yw-grid-view-tile-narrow";
    public static final String SCLASS_YW_GRID_VIEW_TILE_IMG = "yw-grid-view-tile-thumbnail";
    public static final String SCLASS_IMG_PREVIEW_POPUP = "ye-gridview-preview-popup-image";
    private static final String SCLASS_YE_TEXT_BUTTON = "ye-text-button";
    public static final String PREVIEW_SUFFIX_GRID = "grid";
    public static final String SCLASS_YW_GRID_VIEW_TILE_BOTTOM_PANEL = "bottom-panel";
    public static final String SCLASS_YW_GRID_VIEW_TILE_BOTTOM_PANEL_CONTAINER = "bottom-panel-container";
    public static final String TILE_YTESTID = "gridViewTile";
    public static final String SELECT_BUTTON_YTESTID = "selectButton";
    public static final String MULTI_SELECT_SETTING = "multiSelect";
    private String alternativeFallbackImageURL;
    private String alternativeFallbackImageMime;
    private boolean displayDefaultImage;
    private String defaultImage;
    private ComponentMarkingUtils componentMarkingUtils;


    @Override
    public void render(final HtmlBasedComponent parent, final GridView configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        Validate.notNull("All arguments must be non-null", parent, configuration, element, dataType, widgetInstanceManager);
        parent.setSclass(SCLASS_YW_GRID_VIEW_TILE);
        YTestTools.modifyYTestId(parent, TILE_YTESTID);
        appendSelectionCheckbox(element, parent, configuration);
        appendThumbnailAndPopupPreview(element, configuration, dataType, parent, widgetInstanceManager);
        appendLabel(element, parent, configuration);
        applyAdditionalRenderers(parent, configuration, element, dataType, widgetInstanceManager);
        fireComponentRendered(parent, configuration, element);
    }


    protected void appendSelectionCheckbox(final Object object, final HtmlBasedComponent tile, final GridView configuration)
    {
        final Button checkbox = new Button();
        UITools.addSClass(checkbox, SCLASS_YW_GRID_VIEW_TILE_SELECTION);
        UITools.addSClass(checkbox, SCLASS_YE_TEXT_BUTTON);
        YTestTools.modifyYTestId(checkbox, SELECT_BUTTON_YTESTID);
        getComponentMarkingUtils().markComponent(tile, checkbox, GridBox.MARK_NAME_SELECT, object);
        tile.appendChild(checkbox);
        fireComponentRendered(checkbox, tile, configuration, object);
    }


    @Override
    protected String getThumbnailSclass()
    {
        return SCLASS_YW_GRID_VIEW_TILE_IMG;
    }


    @Override
    protected String getPreviewPopupSclass()
    {
        return SCLASS_IMG_PREVIEW_POPUP;
    }


    public String getAlternativeFallbackImageURL()
    {
        return alternativeFallbackImageURL;
    }


    public void setAlternativeFallbackImageURL(final String alternativeFallbackImageURL)
    {
        this.alternativeFallbackImageURL = alternativeFallbackImageURL;
    }


    public String getAlternativeFallbackImageMime()
    {
        return alternativeFallbackImageMime;
    }


    public void setAlternativeFallbackImageMime(final String alternativeFallbackImageMime)
    {
        this.alternativeFallbackImageMime = alternativeFallbackImageMime;
    }


    @Override
    public String getDefaultImage()
    {
        return defaultImage;
    }


    @Override
    public void setDefaultImage(final String defaultImage)
    {
        this.defaultImage = defaultImage;
    }


    @Override
    public boolean isDisplayDefaultImage()
    {
        return displayDefaultImage;
    }


    @Override
    public void setDisplayDefaultImage(final boolean displayDefaultImage)
    {
        this.displayDefaultImage = displayDefaultImage;
    }


    @Override
    protected List<WidgetComponentRenderer> getAdditionalRenderers(final GridView configuration)
    {
        return configuration.getAdditionalRenderer().stream().map(renderer -> getBean(renderer.getSpringBean()))
                        .collect(Collectors.toList());
    }


    @Override
    protected String getSClassForBoxWithAdditionalRenderers()
    {
        return SCLASS_YW_GRID_VIEW_TILE_NARROW;
    }


    @Override
    protected String getSClassForAdditionalPanel()
    {
        return SCLASS_YW_GRID_VIEW_TILE_BOTTOM_PANEL;
    }


    @Override
    protected String getSClassForAdditionalPanelInnerComponent()
    {
        return SCLASS_YW_GRID_VIEW_TILE_BOTTOM_PANEL_CONTAINER;
    }


    @Override
    protected String getPreviewSuffix()
    {
        return PREVIEW_SUFFIX_GRID;
    }


    protected ComponentMarkingUtils getComponentMarkingUtils()
    {
        return componentMarkingUtils;
    }


    @Required
    public void setComponentMarkingUtils(final ComponentMarkingUtils componentMarkingUtils)
    {
        this.componentMarkingUtils = componentMarkingUtils;
    }
}
