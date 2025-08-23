/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import static com.hybris.cockpitng.adminmode.CockpitAdminComposer.DRAGGED_WIDGET;
import static com.hybris.cockpitng.engine.impl.AdminmodeWidgetEngine.ADMIN_FULLSCREEN;
import static com.hybris.cockpitng.engine.impl.AdminmodeWidgetEngine.CP_INVISIBLE_CONTAINER_VISIBLE;
import static com.hybris.cockpitng.engine.impl.AdminmodeWidgetEngine.TOOLBAR_COMPONENT;
import static com.hybris.cockpitng.engine.impl.AdminmodeWidgetEngine.WIDGET_TOKEN;
import static com.hybris.cockpitng.engine.impl.DefaultCockpitWidgetEngine.STOP_PROPAGATION_LISTENER;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.admin.WidgetGroupingService;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.engine.ConnectButtonRenderer;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.engine.TemplateButtonRenderer;
import com.hybris.cockpitng.engine.WidgetToolbarRenderer;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class DefaultWidgetToolbarRenderer implements WidgetToolbarRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(AdminmodeWidgetEngine.class);
    private CockpitAdminService cockpitAdminService;
    private WidgetService widgetService;
    private WidgetPersistenceService widgetPersistenceService;
    private WidgetGroupingService widgetGroupingService;
    private ConnectButtonRenderer connectButtonRenderer;
    private TemplateButtonRenderer templateButtonRenderer;
    private SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry;
    private CockpitComponentDefinitionService cockpitComponentDefinitionService;


    @Override
    public void appendWidgetToolbar(final Component parent, final WidgetDefinition widgetDefinition, final Widgetslot widgetSlot)
    {
        final Widget widget = widgetSlot.getWidgetInstance().getWidget();
        if(!canAppendWidgetToolbar(widget))
        {
            return;
        }
        final Div widgetToolbar = new Div();
        final String color = getCockpitAdminService().getWidgetToolbarColor();
        if(color != null)
        {
            widgetToolbar.setStyle("background-color: " + color);
        }
        widgetToolbar.setSclass("widget_toolbar singleWidget");
        final Div toolbarDiv = new Div();
        if(widgetDefinition == null)
        {
            final Label label = new Label("INVALID WIDGET DEFINITION");
            label.setSclass("widget_label");
            widgetToolbar.appendChild(label);
            LOG.error("Invalid widget definition: {}", widget.getWidgetDefinitionId());
            return;
        }
        else
        {
            prepareWidget(widgetDefinition, widgetSlot, widgetToolbar, toolbarDiv);
        }
        if(!widgetDefinition.isStubWidget())
        {
            renderRemoveBtn(toolbarDiv, widgetSlot, widgetDefinition);
        }
        widgetToolbar.appendChild(toolbarDiv);
        try
        {
            parent.insertBefore(widgetToolbar, parent.getFirstChild());
        }
        catch(final Exception e)
        {
            LOG.error("Could not add toolbar to widget '" + widget + "', reason: ", e);
        }
        widgetToolbar.addEventListener(Events.ON_DOUBLE_CLICK, event -> {
            if(!getCockpitAdminService().isSymbolicAdminMode())
            {
                widget.getWidgetSettings().put(ADMIN_FULLSCREEN, Boolean.TRUE);
                widgetSlot.updateView();
            }
        });
    }


    protected void prepareWidget(final WidgetDefinition widgetDefinition, final Widgetslot widgetSlot, final Div widgetToolbar,
                    final Div toolbarDiv)
    {
        final Widget widget = widgetSlot.getWidgetInstance().getWidget();
        String widgetName = StringUtils.isBlank(widgetDefinition.getName()) //
                        ? widgetDefinition.getCode() //
                        : widgetDefinition.getName();
        if(widgetDefinition.isStubWidget())
        {
            widgetName = widgetName.split("STUB_")[1];
            final AbstractCockpitComponentDefinition orginalDef = getComponentDefinition(widgetName);
            widgetName = orginalDef.getName();
        }
        else
        {
            appendSettingsButton(toolbarDiv, widgetSlot);
            renderComposedGroupButton(toolbarDiv, widgetSlot);
            renderShowInvisibleChildrenButton(toolbarDiv, widgetSlot);
            if(widgetSlot.getParentChildrenContainer() != null)
            {
                getTemplateButtonRenderer().renderTemplateButton(toolbarDiv, widget, widgetDefinition, widgetSlot);
            }
        }
        getConnectButtonRenderer().renderConnectButton(toolbarDiv, widget, widgetSlot);
        final Label label = new Label(widgetName);
        label.setSclass("widget_label");
        widgetToolbar.appendChild(label);
        toolbarDiv.setSclass(TOOLBAR_COMPONENT);
        toolbarDiv.addEventListener(Events.ON_DOUBLE_CLICK, STOP_PROPAGATION_LISTENER);
        label.setDraggable(WIDGET_TOKEN);
        label.setAttribute(DRAGGED_WIDGET, widget);
        widgetToolbar.setDraggable(WIDGET_TOKEN);
        widgetToolbar.setAttribute(DRAGGED_WIDGET, widget);
    }


    protected boolean canAppendWidgetToolbar(final Widget widget)
    {
        return widget != null && getCockpitAdminService().isAdminMode() && !widget.isPartOfGroup();
    }


    protected <T> T getComponentDefinition(final String widgetId)
    {
        return (T)getCockpitComponentDefinitionService().getComponentDefinitionForCode(widgetId);
    }


    @Override
    public void appendSettingsButton(final Component parent, final Widgetslot widgetslot)
    {
        final Widget widget = widgetslot.getWidgetInstance().getWidget();
        final Toolbarbutton settingsBtn = new Toolbarbutton();
        settingsBtn.setParent(parent);
        settingsBtn.setSclass("settingsBtn");
        settingsBtn.setTooltiptext("Widget Settings");
        final EventListener<Event> closeListener = event -> {
            getWidgetPersistenceService().storeWidgetTree(widget);
            if(WidgetTreeUIUtils.getParentWidgetslot(widgetslot) == null)
            {
                widgetslot.updateView();
            }
            else
            {
                WidgetTreeUIUtils.getParentWidgetslot(widgetslot).updateView();
            }
        };
        final Window settingsWizard = getCockpitAdminService().createSettingsWizard(parent, widgetslot, widget, closeListener);
        settingsBtn.addEventListener(Events.ON_CLICK, event -> settingsWizard.doHighlighted());
    }


    @Override
    public void renderComposedGroupButton(final Component parent, final Widgetslot widgetSlot)
    {
        final Widget widget = widgetSlot.getWidgetInstance().getWidget();
        final Toolbarbutton groupBtn = new Toolbarbutton();
        if(widget.getComposedRootInstance() == null && CollectionUtils.isNotEmpty(widget.getChildren()))
        {
            groupBtn.setSclass("groupBtn");
            groupBtn.setTooltiptext("Create widget group");
            groupBtn.addEventListener(Events.ON_CLICK, event -> getWidgetGroupingService().groupWidget(widgetSlot));
            groupBtn.setParent(parent);
        }
        else if(widget.getComposedRootInstance() != null)
        {
            groupBtn.setSclass("groupBtn ungroup");
            groupBtn.setTooltiptext("Ungroup widget");
            groupBtn.addEventListener(Events.ON_CLICK,
                            event -> Messagebox.show("This will decompose this widget group into its single widgets. Do you want to proceed?",
                                            "Confirm", Messagebox.YES + Messagebox.NO, null, messageBoxEvent -> {
                                                if(Messagebox.ON_YES.equals(messageBoxEvent.getName()))
                                                {
                                                    getWidgetGroupingService().ungroupWidget(widgetSlot);
                                                }
                                            }));
            groupBtn.setParent(parent);
        }
    }


    @Override
    public void renderRemoveBtn(final Component parent, final Widgetslot widgetSlot, final WidgetDefinition widgetDefinition)
    {
        final Widget widget = widgetSlot.getWidgetInstance().getWidget();
        final Toolbarbutton clearBtn = new Toolbarbutton();
        clearBtn.setSclass("removeWidgetBtn");
        clearBtn.addEventListener(Events.ON_CLICK, event -> {
            if(confirmNeeded(widget))
            {
                Messagebox.show(Labels.getLabel("engine.remove.widget.question", new Object[]
                                                {widget.getId(), widgetDefinition.getName()}), Labels.getLabel("engine.remove.widget"),
                                new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, messageBoxEvent -> {
                                    if(Messagebox.Button.YES.equals(messageBoxEvent.getButton()))
                                    {
                                        removeWidget(widgetSlot, widget);
                                    }
                                });
            }
            else
            {
                removeWidget(widgetSlot, widget);
            }
        });
        parent.appendChild(clearBtn);
    }


    /**
     * Determines if user is required to confirm before removing a widget. Default implementation returns <code>true</code>
     * if any of the following evaluates <code>true</code>:
     * <ul>
     * <li>widget contains parent widget(s)</li>
     * <li>widget has established connections to other widgets</li>
     * </ul>
     *
     * @param widget
     * @return
     */
    @Override
    public boolean confirmNeeded(final Widget widget)
    {
        return CollectionUtils.isNotEmpty(widget.getChildren()) || CollectionUtils.isNotEmpty(widget.getInConnections())
                        || CollectionUtils.isNotEmpty(widget.getOutConnections());
    }


    /**
     * Removes the widget from the configuration.
     *
     * @param widgetSlot
     * @param widget
     */
    @Override
    public void removeWidget(final Widgetslot widgetSlot, final Widget widget)
    {
        final Widgetslot parentWidgetContainer = WidgetTreeUIUtils.getParentWidgetslot(widgetSlot);
        widgetSlot.setWidgetInstance(null);
        final Widget parentNode = widget.getParent();
        getWidgetPersistenceService().deleteWidgetTree(widget);
        getWidgetService().removeWidget(widget);
        if(parentNode == null)
        {
            getSessionWidgetInstanceRegistry().unregisterWidgetInstance(widget.getSlotId());
        }
        if(parentWidgetContainer == null)
        {
            widgetSlot.updateView();
        }
        else
        {
            parentWidgetContainer.updateView();
        }
    }


    @Override
    public void renderShowInvisibleChildrenButton(final Component parent, final Widgetslot widgetslot)
    {
        final Component invisibleContainer = widgetslot.getInvisibleContainer();
        final Toolbarbutton templateBtn = new Toolbarbutton();
        templateBtn.setParent(parent);
        templateBtn.setSclass("invisibleChildrenBtn");
        templateBtn.setTooltiptext("Toggle invisible widget children");
        templateBtn.addEventListener(Events.ON_CLICK, event -> {
            if(invisibleContainer != null)
            {
                invisibleContainer.setVisible(!invisibleContainer.isVisible());
                widgetslot.getDesktop().setAttribute(CP_INVISIBLE_CONTAINER_VISIBLE + widgetslot.getWidgetInstance().getId(),
                                Boolean.valueOf(invisibleContainer.isVisible()));
                setInvisibleContainerVisible(widgetslot, invisibleContainer.isVisible());
            }
        });
    }


    @Override
    public void setInvisibleContainerVisible(final Widgetslot widgetslot, final boolean value)
    {
        String sclass = widgetslot.getSclass();
        final String invSclass = "invisibleChildrenVisible";
        if(value)
        {
            if(StringUtils.isBlank(sclass))
            {
                sclass = invSclass;
            }
            else if(!sclass.contains(invSclass))
            {
                sclass = sclass.trim() + " " + invSclass;
            }
        }
        else if(sclass != null && sclass.contains(invSclass))
        {
            sclass = sclass.replace(invSclass, StringUtils.EMPTY).trim();
        }
        widgetslot.setSclass(sclass);
    }


    public CockpitAdminService getCockpitAdminService()
    {
        return cockpitAdminService;
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }


    public ConnectButtonRenderer getConnectButtonRenderer()
    {
        return connectButtonRenderer;
    }


    @Required
    public void setConnectButtonRenderer(final ConnectButtonRenderer connectButtonRenderer)
    {
        this.connectButtonRenderer = connectButtonRenderer;
    }


    public WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    public WidgetGroupingService getWidgetGroupingService()
    {
        return widgetGroupingService;
    }


    @Required
    public void setWidgetGroupingService(final WidgetGroupingService widgetGroupingService)
    {
        this.widgetGroupingService = widgetGroupingService;
    }


    public WidgetService getWidgetService()
    {
        return widgetService;
    }


    @Required
    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    public TemplateButtonRenderer getTemplateButtonRenderer()
    {
        return templateButtonRenderer;
    }


    @Required
    public void setTemplateButtonRenderer(final TemplateButtonRenderer templateButtonRenderer)
    {
        this.templateButtonRenderer = templateButtonRenderer;
    }


    public SessionWidgetInstanceRegistry getSessionWidgetInstanceRegistry()
    {
        return sessionWidgetInstanceRegistry;
    }


    @Required
    public void setSessionWidgetInstanceRegistry(final SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry)
    {
        this.sessionWidgetInstanceRegistry = sessionWidgetInstanceRegistry;
    }


    public CockpitComponentDefinitionService getCockpitComponentDefinitionService()
    {
        return cockpitComponentDefinitionService;
    }


    @Required
    public void setCockpitComponentDefinitionService(final CockpitComponentDefinitionService cockpitComponentDefinitionService)
    {
        this.cockpitComponentDefinitionService = cockpitComponentDefinitionService;
    }
}
