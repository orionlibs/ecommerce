/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.permissions.Permission;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionManagementFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import java.util.Collections;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listcell;

public class PermissionManagementListCellRenderer extends AbstractWidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final String SCLASS_PERMISSION_CONTROL_CELL = "yw-permission-controll-cell";
    private static final String SCLASS_PERMISSION_CONTROL_CELL_DENIED_TRUE = "yw-permission-cell-denied-true";
    private static final String SCLASS_PERMISSION_CONTROL_CELL_DENIED_FALSE = "yw-permission-cell-denied-false";
    private static final String SCLASS_PERMISSION_CONTROL_CELL_INHERIT_DENIED_TRUE = "yw-permission-cell-inherit-denied-true";
    private static final String SCLASS_PERMISSION_CONTROL_CELL_INHERIT_DENIED_FALSE = "yw-permission-cell-inherit-denied-false";
    private PermissionManagementFacade permissionManagementFacade;


    @Override
    public void render(final Listcell listcell, final ListColumn configuration, final Object data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(data instanceof PermissionInfo)
        {
            final PermissionInfo permissionInfo = (PermissionInfo)data;
            final Permission permission = permissionInfo.getPermission(configuration.getQualifier());
            if(permission == null)
            {
                return;
            }
            final Div permissionControlCell = new Div();
            permissionControlCell.setParent(listcell);
            displayPermissionState(permissionControlCell, permission);
            permissionControlCell.addEventListener(Events.ON_CLICK, e -> {
                savePermission(permission);
                final PermissionInfo updatedPermissionInfo = permissionManagementFacade.updatePermissionInfo(permission);
                if(CollectionUtils.isNotEmpty(listcell.getChildren()))
                {
                    listcell.getChildren().clear();
                }
                render(listcell, configuration, updatedPermissionInfo, dataType, widgetInstanceManager);
                Events.sendEvent(
                                new SelectEvent(Events.ON_SELECT, listcell.getListbox(), Collections.singleton(listcell.getParent())));
            });
            fireComponentRendered(permissionControlCell, listcell, configuration, permissionInfo);
        }
    }


    private void savePermission(final Permission permission)
    {
        if(permission.isInherited())
        {
            setPermissionToGranted(permission);
        }
        else if(!permission.isDenied())
        {
            setPermissionToDenied(permission);
        }
        else
        {
            setPermissionToDefault(permission);
        }
    }


    private void setPermissionToDenied(final Permission permission)
    {
        permission.setDenied(true);
        permissionManagementFacade.setPermission(permission);
    }


    private void setPermissionToGranted(final Permission permission)
    {
        permission.setInherited(false);
        permission.setDenied(false);
        permissionManagementFacade.setPermission(permission);
    }


    private void setPermissionToDefault(final Permission permission)
    {
        permissionManagementFacade.deletePermission(permission);
    }


    private static void displayPermissionState(final Div permissionControlCell, final Permission permission)
    {
        UITools.modifySClass(permissionControlCell, SCLASS_PERMISSION_CONTROL_CELL, true);
        if(permission == null)
        {
            return;
        }
        if(permission.isInherited())
        {
            if(permission.isDenied())
            {
                UITools.modifySClass(permissionControlCell, SCLASS_PERMISSION_CONTROL_CELL_INHERIT_DENIED_TRUE, true);
            }
            else
            {
                UITools.modifySClass(permissionControlCell, SCLASS_PERMISSION_CONTROL_CELL_INHERIT_DENIED_FALSE, true);
            }
        }
        else if(!permission.isDenied())
        {
            UITools.modifySClass(permissionControlCell, SCLASS_PERMISSION_CONTROL_CELL_DENIED_FALSE, true);
        }
        else
        {
            UITools.modifySClass(permissionControlCell, SCLASS_PERMISSION_CONTROL_CELL_DENIED_TRUE, true);
        }
    }


    protected PermissionManagementFacade getPermissionManagementFacade()
    {
        return permissionManagementFacade;
    }


    @Required
    public void setPermissionManagementFacade(final PermissionManagementFacade permissionManagementFacade)
    {
        this.permissionManagementFacade = permissionManagementFacade;
    }
}
