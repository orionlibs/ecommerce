/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.masterdetail;

import com.hybris.backoffice.masterdetail.SettingButton;
import com.hybris.backoffice.masterdetail.SettingItem;
import com.hybris.cockpitng.util.YTestTools;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

public class NavigationItem
{
    private static final String SCLASS_ICON = "cng-action-icon cng-font-icon";
    private static final String SCLASS_SIDEBAR_ITEM = "yw-sidebar-item";
    private static final String SCLASS_SIDEBAR_ITEM_TITLE = "yw-sidebar-item-title";
    private static final String SCLASS_SIDEBAR_ITEM_SELECTED = "yw-sidebar-item-selected";
    private static final String SCLASS_SIDEBAR_ITEM_SUBTITLE = "yw-sidebar-item-subtitle";
    private static final String FONT_ICON_PREFIX = "font-icon--";
    private SettingItem settingItem;
    private Label subTitleLabel;
    private Hlayout itemContainer;


    protected NavigationItem(final Component parent, final SettingItem settingItem)
    {
        this.settingItem = settingItem;
        render(parent);
    }


    protected String getId()
    {
        return settingItem.getId();
    }


    private void render(final Component parent)
    {
        itemContainer = new Hlayout();
        itemContainer.setSclass(SCLASS_SIDEBAR_ITEM);
        YTestTools.modifyYTestId(itemContainer, settingItem.getId());
        parent.appendChild(itemContainer);
        renderIcon(itemContainer);
        final var titleContainer = new Vlayout();
        titleContainer.setParent(itemContainer);
        createLabel(titleContainer, settingItem.getTitle(), SCLASS_SIDEBAR_ITEM_TITLE);
        subTitleLabel = createLabel(titleContainer, settingItem.getSubtitle(), SCLASS_SIDEBAR_ITEM_SUBTITLE);
    }


    private void renderIcon(final Component parent)
    {
        final String icon = settingItem.getIcon();
        if(StringUtils.isNotBlank(icon))
        {
            final var button = new Button();
            button.setSclass(SCLASS_ICON);
            button.addSclass(FONT_ICON_PREFIX + icon);
            button.setParent(parent);
        }
    }


    private Label createLabel(final Component parent, final String value, final String sclass)
    {
        final var label = new Label(value);
        label.setSclass(sclass);
        label.setParent(parent);
        return label;
    }


    protected void setSelectedStyle()
    {
        itemContainer.addSclass(SCLASS_SIDEBAR_ITEM_SELECTED);
    }


    protected void removeSelectedStyle()
    {
        itemContainer.removeSclass(SCLASS_SIDEBAR_ITEM_SELECTED);
    }


    protected void addEventListener(final String eventType, final EventListener<Event> eventListener)
    {
        itemContainer.addEventListener(eventType, eventListener);
    }


    protected boolean isActive()
    {
        return settingItem.isActive();
    }


    protected void updateItem(final SettingItem data)
    {
        updateSubtitle(data.getSubtitle());
    }


    private void updateSubtitle(final String subTitle)
    {
        subTitleLabel.setValue(subTitle);
    }


    protected List<SettingButton> getSettingButtons()
    {
        return settingItem.getButtons();
    }
}
