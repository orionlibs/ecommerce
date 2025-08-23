/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.navigation.next;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;

public class NextItemSelectorController extends DefaultWidgetController
{
    public static final String NEXT_ITEM_SELECTOR_CONTEXT = "nextItemSelectorContext";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String NEXT_BUTTON = "nextButton";
    public static final String NEXT_ITEM_SELECTOR_INVOCATION = "nextItemSelectorInvocation";
    public static final String NAVIGATION_NEXT_ITEM_SELECTOR_CONTEXT = "navigationNextItemSelectorContext";
    public static final String BTN_SCLASS = "yw-itemSelectorButton yw-nextItemSelector z-paging-next";
    public static final String NAVIGATION_NEXT = "navigation.next";
    @Wire
    private Button nextButton;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        nextButton = new Button();
        nextButton.setSclass(BTN_SCLASS);
        nextButton.setDisabled(!canSelectNextItem());
        nextButton.setTooltiptext(getLabel(NAVIGATION_NEXT));
        nextButton.addEventListener(Events.ON_CLICK, event -> sendOutput(NEXT_ITEM_SELECTOR_INVOCATION, null));
        comp.appendChild(nextButton);
    }


    @SocketEvent(socketId = NEXT_ITEM_SELECTOR_CONTEXT)
    public void handleSelectorContext(final NavigationItemSelectorContext navigationItemSelectorContext)
    {
        setNavigationItemSelectorContextInModel(navigationItemSelectorContext);
        nextButton.setDisabled(!canSelectNextItem());
    }


    protected boolean canSelectNextItem()
    {
        final NavigationItemSelectorContext context = getNavigationItemSelectorContextFromModel();
        if(context == null)
        {
            return false;
        }
        final boolean lastItemIsNotFocused = context.getFocusedIndex() < context.getPageSize() - 1;
        final boolean thereIsAtLeastOnePage = context.getPageSize() > 0;
        final boolean anyItemIsFocused = context.getFocusedIndex() >= 0;
        return lastItemIsNotFocused && thereIsAtLeastOnePage && anyItemIsFocused;
    }


    private void setNavigationItemSelectorContextInModel(final NavigationItemSelectorContext navigationItemSelectorContext)
    {
        setValue(NAVIGATION_NEXT_ITEM_SELECTOR_CONTEXT, navigationItemSelectorContext);
    }


    protected NavigationItemSelectorContext getNavigationItemSelectorContextFromModel()
    {
        return getValue(NAVIGATION_NEXT_ITEM_SELECTOR_CONTEXT, NavigationItemSelectorContext.class);
    }


    public Button getNextButton()
    {
        return nextButton;
    }
}
