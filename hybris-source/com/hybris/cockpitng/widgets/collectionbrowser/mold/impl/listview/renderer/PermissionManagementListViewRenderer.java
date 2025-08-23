/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import org.zkoss.zul.Listitem;

public class PermissionManagementListViewRenderer extends DefaultListViewRenderer
{
    private static final String SCLASS_PERMISSION_NOT_PERSISTED = "yw-permission-notpersisted";


    @Override
    public void render(final Listitem row, final ListView listConfig, final Object entry, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        super.render(row, listConfig, entry, dataType, widgetInstanceManager);
        if(!((PermissionInfo)entry).isPersisted())
        {
            UITools.modifySClass(row, SCLASS_PERMISSION_NOT_PERSISTED, true);
        }
    }
}
