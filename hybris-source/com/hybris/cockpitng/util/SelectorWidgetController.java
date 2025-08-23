/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;
import org.springframework.context.ApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 *
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SelectorWidgetController<T extends Component> extends SelectorComposer<T> implements WidgetInstanceManagerAware,
                WidgetController
{
    private static final long serialVersionUID = 332097631872976562L;
    private transient WidgetInstanceManager widgetInstanceManager;
    @WireVariable
    private transient WidgetUtils widgetUtils;


    @Override
    public void doAfterCompose(final T comp) throws Exception
    {
        super.doAfterCompose(comp);
        WidgetControllers.setupWidgetEventListeners(comp, this, widgetInstanceManager, widgetUtils);
    }


    @Override
    public ComponentInfo doBeforeCompose(final Page _page, final Component parent, final ComponentInfo compInfo)
    {
        final Object attribute = parent.getAttribute("moduleAppCtx");
        if(attribute instanceof ApplicationContext)
        {
            WidgetControllers.wireModuleApplicationContext((ApplicationContext)attribute, this, _page);
        }
        return super.doBeforeCompose(_page, parent, compInfo);
    }


    @Override
    public void doBeforeComposeChildren(final T comp) throws Exception
    {
        super.doBeforeComposeChildren(comp);
        WidgetControllers.initSettings(comp, widgetInstanceManager);
    }


    @Override
    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public void sendOutput(final String socketId, final Object data)
    {
        widgetInstanceManager.sendOutput(socketId, data);
    }


    @Override
    public TypedSettingsMap getWidgetSettings()
    {
        return widgetInstanceManager.getWidgetSettings();
    }


    @Override
    public WidgetModel getModel()
    {
        return widgetInstanceManager.getModel();
    }


    /**
     * Returns root path prefix for the given widget. All resource paths within the widget must be preceded by this.
     */
    public String getWidgetRoot()
    {
        final Widgetslot slot = getWidgetslot();
        if(slot != null)
        {
            final WidgetDefinition definition = slot.getWidgetDefinition(slot.getWidgetInstance().getWidget());
            if(definition != null)
            {
                return definition.getLocationPath();
            }
        }
        return "";
    }


    public Widgetslot getWidgetslot()
    {
        return widgetInstanceManager.getWidgetslot();
    }


    /**
     * @return the widgetUtils
     */
    public WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }
}
