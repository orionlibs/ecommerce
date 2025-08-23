/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.spacemanagement;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.controller.collapsiblecontainer.CollapsibleContainerState;
import org.apache.commons.lang3.BooleanUtils;

public class BackofficeSpaceManagementController extends DefaultWidgetController
{
    private static final String SOCKET_OUT_COLLAPSE_STATE = "collapseState";
    protected static final String SETTING_EXPAND_BOTTOM_SECTION_ON_EMPTY_LIST_SELECTION = "expandBottomSectionOnEmptyListSelection";


    @SocketEvent(socketId = "navigationTreeSelectedObject")
    public void onTreeSelection(final Object object)
    {
        sendOutput(SOCKET_OUT_COLLAPSE_STATE, new CollapsibleContainerState(true, false, true));
    }


    @SocketEvent(socketId = "listSelectedObject")
    public void onListSelection(final Object object)
    {
        if(object != null
                        || BooleanUtils.isTrue(getWidgetSettings().getBoolean(SETTING_EXPAND_BOTTOM_SECTION_ON_EMPTY_LIST_SELECTION)))
        {
            sendOutput(SOCKET_OUT_COLLAPSE_STATE, new CollapsibleContainerState(true, true, false));
        }
    }


    @SocketEvent(socketId = "searchData")
    public void onSearchTriggered(final AdvancedSearchData searchData)
    {
        sendOutput(SOCKET_OUT_COLLAPSE_STATE, new CollapsibleContainerState(null, false, true));
    }
}
