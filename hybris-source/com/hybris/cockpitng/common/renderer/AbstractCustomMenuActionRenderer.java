/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.renderer;

import com.hybris.cockpitng.common.configuration.MenupopupPosition;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menupopup;

/**
 * So called "three dots renderer". It is a renderer that add a button with three dots to DOM. Whenever a user clicks it
 * a popup menu is displayed.
 * <P>
 * Content of popup menu is rendered by all renderers returned from abstract method
 *
 * @param <PARENT>
 *           type of parent component on which thee dots might be rendered
 * @param <CONFIG>
 *           type of configuration object (passed directly to menu renderers)
 * @param <DATA>
 *           type of data rendered (passed directly to menu renderers)
 */
public abstract class AbstractCustomMenuActionRenderer<PARENT extends HtmlBasedComponent, CONFIG, DATA>
                extends AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    private static final String SCLASS_ACTION_CONTAINER = "yw-show-menu-action-container";
    private static final String SCLASS_ACTION_CONTENT = "yw-show-menu-action-content";
    private static final String SCLASS_ACTION = "yw-show-menu-action";
    private static final String SCLASS_ACTION_DISABLED = "yw-show-menu-action-disabled";
    private static final String SCLASS_ACTION_ACTIVE = "yw-show-menu-action-active";
    private String menupopupSclass = "yw-pointer-menupopup yw-pointer-menupopup-top-right";
    private MenupopupPosition menupopupPosition = MenupopupPosition.AFTER_END;
    private List<WidgetComponentRenderer<Menupopup, CONFIG, DATA>> actionRenderers;


    @Override
    public void render(final PARENT parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        UITools.addSClass(parent, SCLASS_ACTION_CONTAINER);
        final Div menuActionContainer = new Div();
        menuActionContainer.setSclass(SCLASS_ACTION_CONTENT);
        final Menupopup menupopup = renderMenuPopup(parent, configuration, data, dataType, widgetInstanceManager);
        final HtmlBasedComponent menuAction = renderMenuAction(parent, configuration, data, dataType, widgetInstanceManager);
        if(CollectionUtils.isNotEmpty(menupopup.getChildren()))
        {
            menuAction.addEventListener(Events.ON_CLICK,
                            createClickListener(menupopup, configuration, data, dataType, widgetInstanceManager));
            menupopup.addEventListener(Events.ON_OPEN,
                            createMenuOpenListener(menuAction, configuration, data, dataType, widgetInstanceManager));
        }
        final boolean disabledAction = CollectionUtils.isEmpty(menupopup.getChildren());
        UITools.modifySClass(menuAction, SCLASS_ACTION_DISABLED, disabledAction);
        if(menuAction instanceof Button)
        {
            ((Button)menuAction).setDisabled(disabledAction);
        }
        parent.appendChild(menuActionContainer);
        menuActionContainer.appendChild(menuAction);
        menuActionContainer.appendChild(menupopup);
        fireComponentRendered(menupopup, parent, configuration, data);
        fireComponentRendered(menuAction, parent, configuration, data);
        fireComponentRendered(parent, configuration, data);
    }


    protected HtmlBasedComponent renderMenuAction(final PARENT parent, final CONFIG configuration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Button menuAction = new Button();
        UITools.addSClass(menuAction, SCLASS_ACTION);
        return menuAction;
    }


    protected Menupopup renderMenuPopup(final PARENT parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Menupopup menupopup = new Menupopup();
        menupopup.setSclass(getMenuPopupSclass());
        UITools.addSClass(menupopup, getMenuPopupSclass());
        final List<WidgetComponentRenderer<Menupopup, CONFIG, DATA>> renderers = getActionRenderers();
        renderers.forEach(renderer -> new ProxyRenderer<>(AbstractCustomMenuActionRenderer.this, parent, configuration, data)
                        .render(renderer, menupopup, configuration, data, dataType, widgetInstanceManager));
        return menupopup;
    }


    public void setActionRenderers(final List<WidgetComponentRenderer<Menupopup, CONFIG, DATA>> actionRenderers)
    {
        this.actionRenderers = actionRenderers;
    }


    protected List<WidgetComponentRenderer<Menupopup, CONFIG, DATA>> getActionRenderers()
    {
        return actionRenderers;
    }


    protected EventListener<OpenEvent> createMenuOpenListener(final HtmlBasedComponent menuAction, final CONFIG configuration,
                    final DATA data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return (final OpenEvent event) -> UITools.modifySClass(menuAction, SCLASS_ACTION_ACTIVE, event.isOpen());
    }


    protected EventListener<Event> createClickListener(final Menupopup parent, final CONFIG configuration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return event -> onActionClick(parent, configuration, data, dataType, widgetInstanceManager);
    }


    protected void onActionClick(final Menupopup parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        parent.open(parent.getParent(), getPopupPosition().getName());
    }


    public void setMenupopupSclass(final String menupopupSclass)
    {
        this.menupopupSclass = menupopupSclass;
    }


    public void setMenupopupPosition(final MenupopupPosition menupopupPosition)
    {
        this.menupopupPosition = menupopupPosition;
    }


    protected MenupopupPosition getPopupPosition()
    {
        return menupopupPosition;
    }


    protected String getMenuPopupSclass()
    {
        return menupopupSclass;
    }
}
