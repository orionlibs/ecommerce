/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

public class Widgetchildren extends Div implements WidgetContainer
{
    public static final String ON_CHILD_WIDGET_SELECTED = "onChildWidgetSelected";
    public static final String PORTAL = "portal";
    public static final String TAB = "tab";
    public static final String LIST = "list";
    public static final String H_SPLIT = "h_split";
    public static final String V_SPLIT = "v_split";
    public static final String INVISIBLE = "invisible";
    public static final String SYMBOLIC = "symbolic";
    private static final long serialVersionUID = -8875086420819855930L;
    private static final String ZCLASS = "widget_cnt widget_children";
    private String type;
    private String slotID;
    private transient CockpitWidgetEngine cockpitWidgetEngine;
    private transient WidgetInstanceFacade widgetInstanceFacade;


    public Widgetchildren()
    {
        super();
        Widgetchildren.this.setZclass(ZCLASS);
    }


    @Override
    public void afterCompose()
    {
        updateChildren();
    }


    @Override
    public void updateView()
    {
        this.getChildren().clear();
        updateChildren();
    }


    public void updateChildren()
    {
        getCockpitWidgetEngine().createWidgetView(this);
    }


    /**
     * @return the parentWidget
     */
    public WidgetInstance getParentWidgetInstance()
    {
        final Widgetslot parentWidgetContainer = getParentWidgetContainer();
        if(parentWidgetContainer != null)
        {
            return parentWidgetContainer.getWidgetInstance();
        }
        return null;
    }


    public Widgetslot getParentWidgetContainer()
    {
        Component parent = getParent();
        while(parent != null)
        {
            if(parent instanceof Widgetslot)
            {
                return ((Widgetslot)parent);
            }
            parent = parent.getParent();
        }
        return null;
    }


    /**
     * @param type
     *           the type to set
     */
    public void setType(final String type)
    {
        this.type = type;
    }


    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }


    /**
     * @param slotID
     *           the slotID to set
     */
    public void setSlotID(final String slotID)
    {
        this.slotID = slotID;
    }


    /**
     * @return the slotID
     */
    public String getSlotID()
    {
        return slotID;
    }


    /**
     * @return the cockpitWidgetEngine
     */
    public CockpitWidgetEngine getCockpitWidgetEngine()
    {
        if(cockpitWidgetEngine == null)
        {
            cockpitWidgetEngine = (CockpitWidgetEngine)SpringUtil.getBean("cockpitWidgetEngine");
        }
        return cockpitWidgetEngine;
    }


    protected WidgetInstanceFacade getWidgetInstanceFacade()
    {
        if(widgetInstanceFacade == null)
        {
            widgetInstanceFacade = SpringUtil.getApplicationContext().getBean("widgetInstanceFacade", WidgetInstanceFacade.class);
        }
        return widgetInstanceFacade;
    }


    public void selectChildWidget(final int index)
    {
        Events.postEvent(ON_CHILD_WIDGET_SELECTED, this, Integer.valueOf(index));
    }
}
