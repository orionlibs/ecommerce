/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.systembar;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.core.logo.LogoService;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.modules.HotDeploymentService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class SystemBarController extends DefaultWidgetController
{
    /**
     * @deprecated since 2205. Not used anymore.
     */
    @Deprecated(since = "2205", forRemoval = true)
    public static final String LOGOUT_BTN = "logoutBtn";
    private static final String REDEPLOY = "Redeploy";
    private static final String PROPERTY_HOT_DEPLOYMENT_ENABLED = "cockpitng.hotDeployment.enabled";
    private static final String CSS_CLASS_YW_SYSTEM_ITEMS_CONTAINER_BTN = "yw-systemItemsContainer-btn";
    private static final String LABEL_ORCHESTRATOR = "general.orchestrator";
    private static final String BEAN_HOT_DEPLOYMENT_SERVICE = "hotDeploymentService";
    private static final String BEAN_COCKPIT_ADMIN_SERVICE = "cockpitAdminService";
    protected static final String SOCKET_INPUT_LOGO_CHANGED = "logoChanged";
    private Div logoContainer;
    private Div systemItemsContainer;
    private transient CockpitSessionService cockpitSessionService;
    private transient CockpitProperties cockpitProperties;
    @WireVariable
    private transient LogoService logoService;
    @Wire
    private Actions devToolbarActionSlot;
    @Wire
    private Image logoImage;


    @Override
    public void initialize(final Component comp)
    {
        if(cockpitProperties.getBoolean(PROPERTY_HOT_DEPLOYMENT_ENABLED))
        {
            createButton(REDEPLOY, event -> {
                final HotDeploymentService service = BackofficeSpringUtil.getBean(BEAN_HOT_DEPLOYMENT_SERVICE);
                service.hotDeploy();
            }, true);
        }
        final CockpitAdminService cockpitAdminService = BackofficeSpringUtil.getBean(BEAN_COCKPIT_ADMIN_SERVICE);
        if(cockpitAdminService != null && cockpitAdminService.isAdminModePermitted())
        {
            createButton(getLabel(LABEL_ORCHESTRATOR), event -> cockpitAdminService.toggleAdminMode(comp), false);
        }
        refreshActionSlot();
        reloadLogo();
    }


    protected void refreshActionSlot()
    {
        if(devToolbarActionSlot != null)
        {
            final String componentContext = getWidgetSettings().getString("devToolbarActionsComponentId");
            devToolbarActionSlot.setConfig("component=" + componentContext);
            devToolbarActionSlot.reload();
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_LOGO_CHANGED)
    public void reloadLogo()
    {
        final String url = getLogoService().getShellBarLogoUrl();
        logoImage.setSrc(StringUtils.isEmpty(url) ? String.format("%s%s", getWidgetRoot(), "/images/logo-sap.png") : url);
    }


    private void createButton(final String label, final EventListener<Event> onClick, final boolean visible)
    {
        final Button button = new Button();
        button.setLabel(label);
        button.addEventListener(Events.ON_CLICK, onClick);
        button.setParent(systemItemsContainer);
        button.setClass(CSS_CLASS_YW_SYSTEM_ITEMS_CONTAINER_BTN);
        button.setVisible(visible);
    }


    /**
     * @deprecated since 2205. Not used anymore.
     */
    @Deprecated(since = "2205", forRemoval = true)
    @ViewEvent(componentID = LOGOUT_BTN, eventName = Events.ON_CLICK)
    public void logout()
    {
        cockpitSessionService.logout();
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    public Div getLogoContainer()
    {
        return logoContainer;
    }


    public Div getSystemItemsContainer()
    {
        return systemItemsContainer;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public Actions getDevToolbarActionSlot()
    {
        return devToolbarActionSlot;
    }


    public LogoService getLogoService()
    {
        return logoService;
    }


    public void setLogoService(final LogoService logoService)
    {
        this.logoService = logoService;
    }


    public Image getLogoImage()
    {
        return logoImage;
    }
}
