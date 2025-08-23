/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.navigation.prev;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;

public class PreviousItemSelectorController extends DefaultWidgetController
{
    public static final String PREVIOUS_ITEM_SELECTOR_CONTEXT = "previousItemSelectorContext";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String PREVIOUS_BUTTON = "previousButton";
    public static final String PREVIOUS_ITEM_SELECTOR_INVOCATION = "previousItemSelectorInvocation";
    public static final String NAVIGATION_PREV_ITEM_SELECTOR_CONTEXT = "navigationPrevItemSelectorContext";
    public static final String BTN_SCLASS = "yw-itemSelectorButton yw-previousItemSelector z-paging-previous";
    public static final String NAVIGATION_PREVIOUS = "navigation.previous";
    private Button previousButton;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        previousButton = new Button();
        previousButton.setSclass(BTN_SCLASS);
        previousButton.setDisabled(!canSelectPreviousItem());
        previousButton.setTooltiptext(getLabel(NAVIGATION_PREVIOUS));
        previousButton.addEventListener(Events.ON_CLICK, event -> sendOutput(PREVIOUS_ITEM_SELECTOR_INVOCATION, null));
        comp.appendChild(previousButton);
    }


    @SocketEvent(socketId = PREVIOUS_ITEM_SELECTOR_CONTEXT)
    public void handleSelectorContext(final NavigationItemSelectorContext navigationItemSelectorContext)
    {
        setNavigationItemSelectorContextInModel(navigationItemSelectorContext);
        previousButton.setDisabled(!canSelectPreviousItem());
    }


    protected boolean canSelectPreviousItem()
    {
        final NavigationItemSelectorContext context = getNavigationItemSelectorContextFromModel();
        if(context == null)
        {
            return false;
        }
        final int anythingExceptForFirstItemIsFocused = context.getFocusedIndex();
        return anythingExceptForFirstItemIsFocused > 0;
    }


    private void setNavigationItemSelectorContextInModel(final NavigationItemSelectorContext navigationItemSelectorContext)
    {
        getModel().setValue(NAVIGATION_PREV_ITEM_SELECTOR_CONTEXT, navigationItemSelectorContext);
    }


    protected NavigationItemSelectorContext getNavigationItemSelectorContextFromModel()
    {
        return getModel().getValue(NAVIGATION_PREV_ITEM_SELECTOR_CONTEXT, NavigationItemSelectorContext.class);
    }


    public Button getPreviousButton()
    {
        return previousButton;
    }
}
