/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Initializable;
import com.hybris.cockpitng.core.RuleEngineResult;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetTemplateRulesEngine;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Idspace;

public class Widgetslot extends Idspace implements WidgetContainer, Cleanable, Initializable
{
    public static final String ATTRIBUTE_WIDGET_MODEL = "widgetModel";
    public static final String ATTRIBUTE_WIDGET_CONTROLLER = "widgetController";
    private static final long serialVersionUID = 8395176229354678010L;
    private static final Logger LOG = LoggerFactory.getLogger(Widgetslot.class);
    private static final String ZCLASS = "widget_cnt";
    private String slotID;
    private WidgetInstance currentWidgetInstance;
    private transient WidgetService widgetService;
    private transient WidgetInstanceFacade widgetInstanceFacade;
    private transient CockpitComponentDefinitionService widgetDefinitionService;
    private Widgetchildren parentChildrenContainer;
    private Div invisibleContainer;
    private transient CockpitWidgetEngine cockpitWidgetEngine;
    private transient WidgetUtils widgetUtils;
    private transient WidgetTemplateRulesEngine rulesEngine;


    public Widgetslot()
    {
        super();
        Widgetslot.this.setZclass(ZCLASS);
    }


    @Override
    public void afterCompose()
    {
        updateView();
        this.addEventListener("onSocketOutput", this::onSocketOutput);
    }


    @Override
    public void updateView()
    {
        this.getChildren().clear();
        getCockpitWidgetEngine().createWidgetView(this);
    }


    public void updateChildren(final String childSlotId)
    {
        final Component slotOrChildren = getSlotOrChildren(childSlotId);
        if(slotOrChildren instanceof Widgetchildren)
        {
            ((Widgetchildren)slotOrChildren).updateChildren();
        }
        else if(slotOrChildren instanceof Widgetslot)
        {
            ((Widgetslot)slotOrChildren).updateView();
        }
        else
        {
            LOG.warn("unable to update children of '{}' and slot '{}' - no Widgetchildren or Widgetslot found for that slot",
                            getWidgetInstance(), childSlotId);
        }
    }


    private Component getSlotOrChildren(final String childSlotId)
    {
        final List<Component> children = getChildren();
        for(final Component comp : children)
        {
            final Component result = getSlotOrChildren(childSlotId, comp);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }


    private static Component getSlotOrChildren(final String childSlotId, final Component component)
    {
        if(component instanceof Widgetslot)
        {
            if(StringUtils.equals(childSlotId, ((Widgetslot)component).getSlotID()))
            {
                return component;
            }
        }
        else if(component instanceof Widgetchildren)
        {
            if(StringUtils.equals(childSlotId, ((Widgetchildren)component).getSlotID()))
            {
                return component;
            }
        }
        else
        {
            final List<Component> children = component.getChildren();
            for(final Component comp : children)
            {
                final Object result = getSlotOrChildren(childSlotId, comp);
                if(result != null)
                {
                    return comp;
                }
            }
        }
        return null;
    }


    /**
     * @param slotID
     *           the slotID to set
     */
    public void setSlotID(final String slotID)
    {
        this.slotID = slotID;
    }


    public Map<String, Object> getSettings()
    {
        return getWidgetInstance() == null ? Collections.<String, Object>emptyMap()
                        : getWidgetInstance().getWidget().getWidgetSettings();
    }


    /**
     * @param parentChildrenContainer
     *           the parentChildrenContainer to set
     */
    public void setParentChildrenContainer(final Widgetchildren parentChildrenContainer)
    {
        this.parentChildrenContainer = parentChildrenContainer;
    }


    /**
     * @return the parentChildrenContainer
     */
    public Widgetchildren getParentChildrenContainer()
    {
        return parentChildrenContainer;
    }


    public WidgetModel getViewModel()
    {
        return (WidgetModel)getAttribute(ATTRIBUTE_WIDGET_MODEL);
    }


    public <C extends WidgetController> C getViewController()
    {
        return (C)getAttribute(ATTRIBUTE_WIDGET_CONTROLLER);
    }


    public String getSlotID()
    {
        return slotID;
    }


    public WidgetInstance getWidgetInstance()
    {
        return this.currentWidgetInstance;
    }


    public WidgetInstance getParentWidgetInstance()
    {
        final Widgetslot parentWidgetContainer = WidgetTreeUIUtils.getParentWidgetslot(this);
        if(parentWidgetContainer != null)
        {
            return parentWidgetContainer.getWidgetInstance();
        }
        return null;
    }


    public void setWidgetInstance(final WidgetInstance currentWidgetInstance)
    {
        this.currentWidgetInstance = currentWidgetInstance;
        if(currentWidgetInstance != null)
        {
            final WidgetDefinition widgetDefinition = getWidgetDefinition(currentWidgetInstance.getWidget());
            if(widgetDefinition == null)
            {
                LOG.error("Could not find widget definition for code '" + currentWidgetInstance.getWidget().getWidgetDefinitionId()
                                + "'");
            }
            else if(currentWidgetInstance.getWidget().getComposedRootInstance() == null
                            && widgetDefinition.getComposedWidgetRoot() != null)
            {
                // TODO possibly move to the engine
                getWidgetService().loadComposedChildren(currentWidgetInstance.getWidget(), widgetDefinition.getComposedWidgetRoot());
            }
        }
    }


    private void onSocketOutput(final Event event)
    {
        if(getWidgetInstance() == null)
        {
            return;
        }
        WidgetInstance templateInstance = getWidgetInstance();
        if(!getWidgetInstance().getWidget().isTemplate())
        {
            templateInstance = getWidgetInstance().getParent();
        }
        if(templateInstance == null)
        {
            return;
        }
        final WidgetInstance parent = templateInstance.getParent();
        final RuleEngineResult result = getRulesEngine().handleOutcomingEvent(templateInstance, (String)event.getData());
        if(!result.getInstances().isEmpty() && parent != null)
        {
            final Widgetslot parentContainer = getWidgetUtils().getRegisteredWidgetslot(parent);
            if(result.isUpdateNeeded())
            {
                final WidgetInstance first = result.getInstances().iterator().next();
                final Widget widget = first.getWidget();
                final String slotId = widget.getSlotId();
                parentContainer.updateChildren(slotId);
                if(result.isWidgetAboutToClose())
                {
                    getTemplateNotificationStack().onTemplateClosed(templateInstance);
                }
            }
        }
    }


    @Override
    public void cleanup()
    {
        if(getViewController() instanceof Cleanable)
        {
            ((Cleanable)getViewController()).cleanup();
        }
    }


    @Override
    public void initialize()
    {
        if(getViewController() instanceof Initializable)
        {
            ((Initializable)getViewController()).initialize();
        }
    }


    public WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        return (WidgetDefinition)getWidgetDefinitionService().getComponentDefinitionForCode(widget.getWidgetDefinitionId());
    }


    public void setInvisibleContainer(final Div invisibleContainer)
    {
        this.invisibleContainer = invisibleContainer;
    }


    public Div getInvisibleContainer()
    {
        return invisibleContainer;
    }


    protected WidgetService getWidgetService()
    {
        if(widgetService == null)
        {
            widgetService = SpringUtil.getApplicationContext().getBean("widgetService", WidgetService.class);
        }
        return widgetService;
    }


    protected WidgetInstanceFacade getWidgetInstanceFacade()
    {
        if(widgetInstanceFacade == null)
        {
            widgetInstanceFacade = SpringUtil.getApplicationContext().getBean("widgetInstanceFacade", WidgetInstanceFacade.class);
        }
        return widgetInstanceFacade;
    }


    protected CockpitComponentDefinitionService getWidgetDefinitionService()
    {
        if(widgetDefinitionService == null)
        {
            widgetDefinitionService = SpringUtil.getApplicationContext().getBean("widgetDefinitionService",
                            CockpitComponentDefinitionService.class);
        }
        return widgetDefinitionService;
    }


    public CockpitWidgetEngine getCockpitWidgetEngine()
    {
        if(cockpitWidgetEngine == null)
        {
            cockpitWidgetEngine = SpringUtil.getApplicationContext().getBean("cockpitWidgetEngine", CockpitWidgetEngine.class);
        }
        return cockpitWidgetEngine;
    }


    public WidgetUtils getWidgetUtils()
    {
        if(widgetUtils == null)
        {
            widgetUtils = SpringUtil.getApplicationContext().getBean("widgetUtils", WidgetUtils.class);
        }
        return widgetUtils;
    }


    public WidgetTemplateRulesEngine getRulesEngine()
    {
        if(rulesEngine == null)
        {
            rulesEngine = SpringUtil.getApplicationContext().getBean("widgetTemplateRulesEngine", WidgetTemplateRulesEngine.class);
        }
        return rulesEngine;
    }


    public NotificationStack getTemplateNotificationStack()
    {
        return SpringUtil.getApplicationContext().getBean("notificationStack", NotificationStack.class);
    }
}
