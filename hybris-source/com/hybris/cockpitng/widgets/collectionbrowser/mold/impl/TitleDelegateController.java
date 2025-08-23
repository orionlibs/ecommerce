/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserDelegateController;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;

public class TitleDelegateController implements CollectionBrowserDelegateController
{
    public static final String SETTING_LIST_TITLE = "listTitle";
    public static final String SETTING_LIST_SUBTITLE = "listSubtitle";
    public static final String SETTING_DISPLAY_NUMBEROFITEMS_IN_SUBTITLE = "displayNumberOfItemsInSubTitle";
    public static final String LABEL_PAGESIZE_NUMBEROFITEMS = "pagesize.numberofitems";
    public static final String TITLE_VISIBILITY_SUPPRESSOR_WIDGET_ID = "com.hybris.cockpitng.collapsiblecontainer";
    private CollectionBrowserController controller;


    public TitleDelegateController()
    {
    }


    /**
     * @deprecated since 6.7 - not used anymore, please use @link{{@link #TitleDelegateController()}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public TitleDelegateController(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }


    public void resetTitle()
    {
        if(controller.getListTitle() != null)
        {
            prepareSubTitle();
            controller.getNumberOfItemsLabel().setVisible(false);
            controller.getListSubtitle().setValue(StringUtils.EMPTY);
            suppressTitleVisibilityForCollapsibleContainer();
        }
        controller.getItemCount().setDisabled(true);
    }


    public void updateTitle(final String typeCode, final int totalCount)
    {
        prepareSubTitle();
        updateSubTitle(typeCode, totalCount);
        suppressTitleVisibilityForCollapsibleContainer();
    }


    private void prepareSubTitle()
    {
        final TypedSettingsMap settings = controller.getWidgetSettings();
        final String title = controller.getLabel(settings.getString(SETTING_LIST_TITLE));
        controller.getListTitle().setValue(StringUtils.defaultIfBlank(title, StringUtils.EMPTY));
        final String defaultSubTitle = settings.getString(SETTING_LIST_SUBTITLE);
        final String subTitle = controller.getLabel(defaultSubTitle);
        controller.getListSubtitle().setValue(StringUtils.defaultIfBlank(subTitle, defaultSubTitle));
    }


    private void updateSubTitle(final String typeCode, final int totalCount)
    {
        if(controller.getWidgetSettings().getBoolean(SETTING_DISPLAY_NUMBEROFITEMS_IN_SUBTITLE))
        {
            displayNumberOfItemsInSubtitle(typeCode, totalCount);
        }
        else
        {
            displayNumberOfItemsInLabel(totalCount);
        }
    }


    private void displayNumberOfItemsInLabel(final int totalCount)
    {
        final Object[] args = {String.valueOf(totalCount)};
        final String label = controller.getLabel(LABEL_PAGESIZE_NUMBEROFITEMS, args);
        controller.getNumberOfItemsLabel().setValue(label);
        controller.getNumberOfItemsLabel().setVisible(true);
        controller.getListSubtitle().setValue(StringUtils.EMPTY);
    }


    private void displayNumberOfItemsInSubtitle(final String typeCode, final int totalCount)
    {
        final StringBuilder subTitleBuilder = new StringBuilder();
        subTitleBuilder.append(totalCount >= 0 ? totalCount : 0);
        if(StringUtils.isNotBlank(typeCode))
        {
            String objectLabel = controller.getLabelService().getObjectLabel(typeCode);
            if(StringUtils.isBlank(objectLabel))
            {
                objectLabel = Labels.getLabel(String.format("%s%s", typeCode, "-name"));
            }
            subTitleBuilder.append(" (").append(objectLabel).append(')');
        }
        controller.getListSubtitle().setValue(subTitleBuilder.toString());
        controller.getNumberOfItemsLabel().setVisible(false);
    }


    private void suppressTitleVisibilityForCollapsibleContainer()
    {
        if(isWidgetIsNotNull())
        {
            final WidgetInstance widgetInstance = controller.getWidgetslot().getParentWidgetInstance();
            final String widgetDefinitionId = widgetInstance.getWidget().getWidgetDefinitionId();
            if(TITLE_VISIBILITY_SUPPRESSOR_WIDGET_ID.equals(widgetDefinitionId))
            {
                hideTitle();
            }
        }
    }


    private boolean isWidgetIsNotNull()
    {
        if(controller.getWidgetslot() != null)
        {
            final boolean instanceIsNotNull = controller.getWidgetslot().getParentWidgetInstance() != null;
            if(instanceIsNotNull)
            {
                return controller.getWidgetslot().getParentWidgetInstance().getWidget() != null;
            }
        }
        return false;
    }


    private void hideTitle()
    {
        final Label listTitle = controller.getListTitle();
        final Label listSubtitle = controller.getListSubtitle();
        listTitle.setVisible(false);
        listSubtitle.setVisible(false);
        controller.setWidgetTitle(listTitle.getValue() + " " + listSubtitle.getValue());
    }


    protected CollectionBrowserController getController()
    {
        return controller;
    }


    @Override
    public void setController(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }
}
