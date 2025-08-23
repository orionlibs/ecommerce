/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.toolbar;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.components.VerticalCockpitActionsRenderer;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;

public class ToolbarWidgetController extends DefaultWidgetController
{
    protected static final String SOCKET_TYPE = "type";
    protected static final String SOCKET_ACTIONS = "toolbarActions";
    protected static final String SOCKET_LOADED_ACTIONS = "loadedActions";
    protected static final String SOCKET_RESET_TYPE = "resetType";
    protected static final String TOOLBAR_SLOT_COMPONENT_ID = "toolbarActions";
    protected static final String SETTING_VIEW_MODE = "viewMode";
    protected static final String VIEW_MODE_VERTICAL = "Vertical";
    protected static final String SCLASS_YW_TOOLBAR_VERTICAL = "yw-toolbar-vertical";
    protected static final String SCLASS_YW_TOOLBAR_HORIZONTAL = "yw-toolbar-horizontal";
    private static final String MODEL_TYPE_CODE = "typeCode";
    private static final String MODEL_ACTIONS = "actionsContextName";
    @Wire
    private Div toolbarContainer;
    @Wire
    private Actions toolbarSlot;
    private String typeCode;
    private String actions;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        if((actions = getModel().getValue(MODEL_ACTIONS, String.class)) == null)
        {
            actions = getWidgetSettings().getString(TOOLBAR_SLOT_COMPONENT_ID);
        }
        typeCode = getModel().getValue(MODEL_TYPE_CODE, String.class);
        reload();
    }


    protected void setViewMode()
    {
        final boolean vertical = StringUtils.equalsIgnoreCase(getWidgetSettings().getString(SETTING_VIEW_MODE), VIEW_MODE_VERTICAL);
        UITools.modifySClass(toolbarContainer, SCLASS_YW_TOOLBAR_VERTICAL, vertical);
        UITools.modifySClass(toolbarContainer, SCLASS_YW_TOOLBAR_HORIZONTAL, !vertical);
        if(vertical)
        {
            toolbarSlot.setRenderer(VerticalCockpitActionsRenderer.class.getName());
        }
    }


    @SocketEvent(socketId = SOCKET_RESET_TYPE)
    public void resetType()
    {
        this.typeCode = null;
        getModel().setValue(MODEL_TYPE_CODE, typeCode);
        reload();
    }


    @SocketEvent(socketId = SOCKET_TYPE)
    public void changeType(final String typeCode)
    {
        this.typeCode = typeCode;
        getModel().setValue(MODEL_TYPE_CODE, typeCode);
        reload();
    }


    @SocketEvent(socketId = SOCKET_ACTIONS)
    public void changeActions(final String actions)
    {
        this.actions = actions;
        getModel().setValue(MODEL_ACTIONS, actions);
        reload();
    }


    protected void reload()
    {
        setViewMode();
        final String config;
        if(StringUtils.isNotBlank(typeCode))
        {
            config = String.format("component=%s,type=%s", actions, typeCode);
        }
        else
        {
            config = String.format("component=%s", actions);
        }
        toolbarSlot.setConfig(config);
        if(StringUtils.isNotBlank(actions))
        {
            UITools.addSClass(toolbarSlot, getActionsSClass());
            UITools.addSClass(toolbarContainer, getToolbarSClass());
        }
        toolbarSlot.reload();
        sendOutput(SOCKET_LOADED_ACTIONS, toolbarSlot.getConfiguration());
    }


    protected String getActionsSClass()
    {
        return sanitizeSClass("yw-" + actions);
    }


    protected String getToolbarSClass()
    {
        if(StringUtils.isNotBlank(typeCode))
        {
            return sanitizeSClass("yw-" + actions + "-" + typeCode);
        }
        else
        {
            return getActionsSClass();
        }
    }


    protected String sanitizeSClass(final String sclass)
    {
        return sclass.toLowerCase(Locale.ENGLISH).replace(" ", "-").replace(".", "");
    }
}
