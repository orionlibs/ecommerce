/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.navigation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigationItemSelectorContext
{
    private static final int NO_ITEM_FOCUSED_INDEX = -1;
    private final int selectedIndex;
    private final int focusedIndex;
    private final int pageSize;


    @JsonCreator
    public NavigationItemSelectorContext(@JsonProperty("pageSize") final int pageSize,
                    @JsonProperty("selectedIndex") final int selectedIndex)
    {
        this(pageSize, selectedIndex, NO_ITEM_FOCUSED_INDEX);
    }


    @JsonCreator
    public NavigationItemSelectorContext(@JsonProperty("pageSize") final int pageSize,
                    @JsonProperty("selectedIndex") final int selectedIndex, @JsonProperty("focusedIndex") final int focusedIndex)
    {
        this.pageSize = pageSize;
        this.selectedIndex = selectedIndex;
        this.focusedIndex = focusedIndex;
    }


    public int getSelectedIndex()
    {
        return selectedIndex;
    }


    public int getFocusedIndex()
    {
        return focusedIndex;
    }


    public int getPageSize()
    {
        return pageSize;
    }
}
