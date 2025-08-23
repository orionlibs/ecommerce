/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.packaging.SimpleHybrisWidgetResourceLoader;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.WidgetWizardCreationDelegate;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultWidgetWizardCreationDelegate implements WidgetWizardCreationDelegate
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetWizardCreationDelegate.class);
    private WidgetService widgetService;
    private WidgetPersistenceService widgetPersistenceService;
    private AuthorityGroupService authorityGroupService;


    @Override
    public Widget createWidget(final String slotId, final Widget parent, final String id, final WidgetDefinition definition)
    {
        final Widget newWidget;
        if(definition.getComposedWidgetRoot() == null)
        {
            String effectiveSlotId = slotId;
            Widget effectiveParent = parent;
            if(parent != null && parent.isPartOfGroup())
            {
                effectiveParent = WidgetTreeUtils.getRootWidget(parent).getGroupContainer();
                effectiveSlotId = WidgetTreeUtils.getSlotIdPathToComposedRoot(parent)
                                + WidgetTreeUtils.COMPOSED_SLOT_SEPARATOR + effectiveSlotId;
            }
            newWidget = getWidgetService().createWidget(effectiveParent, id, effectiveSlotId, definition.getCode());
            applyRoleRestrictions(newWidget);
            getWidgetPersistenceService().storeWidgetTree(effectiveParent == null ? newWidget : effectiveParent);
        }
        else
        {
            final Widget composedRoot = definition.getComposedWidgetRoot();
            newWidget = getWidgetService().createComposedWidget(parent, id, slotId, definition.getCode(), composedRoot);
            getWidgetPersistenceService().storeWidgetTree(parent == null ? newWidget : parent);
        }
        return newWidget;
    }


    @Override
    public EventListener<Event> createAddWidgetWizardEventListener(final String slotID, final Widget parentWidget, final Executable executable)
    {
        return event2 -> {
            final WidgetDefinition definition = (WidgetDefinition)event2.getData();
            final Widget newWidget;
            if(definition.getComposedWidgetRoot() == null)
            {
                newWidget = getWidgetService().createWidget(parentWidget, null, slotID, definition.getCode());
                applyRoleRestrictions(newWidget);
            }
            else
            {
                final Widget composedRoot = definition.getComposedWidgetRoot();
                newWidget = getWidgetService().createComposedWidget(parentWidget, null, slotID, definition.getCode(), composedRoot);
            }
            if(parentWidget == null)
            {
                LOG.warn("Could not add widget to parent, parent was null.");
            }
            else
            {
                getWidgetPersistenceService().storeWidgetTree(parentWidget);
            }
            Executions.getCurrent().setAttribute("lastCreatedWidget", newWidget);
            executable.execute();
            SimpleHybrisWidgetResourceLoader.clearCssCache();
        };
    }


    @Override
    public EventListener<Event> createAddWidgetWizardSelectListener(final Widgetslot widgetslot, final String slotID)
    {
        return event -> {
            final Widget parent = getParentIfAny(widgetslot);
            final String id = (parent == null ? widgetslot.getSlotID() : null);
            final WidgetDefinition definition = (WidgetDefinition)event.getData();
            final Widget newWidget = createWidget(slotID, parent, id, definition);
            Executions.getCurrent().setAttribute("lastCreatedWidget", newWidget);
            if(WidgetTreeUIUtils.getParentWidgetslot(widgetslot) == null)
            {
                widgetslot.updateView();
            }
            else
            {
                WidgetTreeUIUtils.getParentWidgetslot(widgetslot).updateView();
            }
            SimpleHybrisWidgetResourceLoader.clearCssCache();
        };
    }


    /**
     * Applies restrictions for widget based on the {@link AuthorityGroupService#getActiveAuthorityGroupForUser(String)}
     * method.
     *
     * @param widget
     */
    protected void applyRoleRestrictions(final Widget widget)
    {
        final AuthorityGroup activeImpersonationAuthorityGroup = getAuthorityGroupService().getActiveAuthorityGroupForUser(null);
        if(activeImpersonationAuthorityGroup != null)
        {
            widget.setAccessRestrictions(activeImpersonationAuthorityGroup.getAuthorities());
        }
    }


    private Widget getParentIfAny(final Widgetslot widgetslot)
    {
        final Widgetslot parentWidgetSlot = WidgetTreeUIUtils.getParentWidgetslot(widgetslot);
        if(parentWidgetSlot != null && parentWidgetSlot.getWidgetInstance() != null)
        {
            return parentWidgetSlot.getWidgetInstance().getWidget();
        }
        return null;
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


    public WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    public AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    @Required
    public void setAuthorityGroupService(final AuthorityGroupService authorityGroupService)
    {
        this.authorityGroupService = authorityGroupService;
    }
}
