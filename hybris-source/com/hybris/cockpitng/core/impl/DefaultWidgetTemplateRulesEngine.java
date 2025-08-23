/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.RuleEngineResult;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetInstanceSettings;
import com.hybris.cockpitng.core.WidgetInstanceSettings.SocketEventRoutingMode;
import com.hybris.cockpitng.core.WidgetTemplateRulesEngine;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.core.ui.impl.DefaultWidgetInstance;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWidgetTemplateRulesEngine implements WidgetTemplateRulesEngine
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetTemplateRulesEngine.class);
    private WidgetInstanceFacade widgetInstanceFacade;


    @Override
    public RuleEngineResult forwardSocketEvent(final Widget templateWidget, final String targetSocketId,
                    final WidgetInstance sourceInstance, final String sourceSocketId)
    {
        WidgetInstance ret = null;
        boolean suppressUpdate = false;
        WidgetInstance parentInstance = null;
        final Widget parentWidget = templateWidget.getParent();
        if(parentWidget != null)
        {
            // TODO what if there is more than one?
            parentInstance = parentWidget.getWidgetInstances().iterator().next();
        }
        final WidgetInstanceSettings instanceSettings = templateWidget.getWidgetInstanceSettings();
        final boolean isCreate = isCreate(instanceSettings, templateWidget, targetSocketId, sourceInstance, sourceSocketId);
        final boolean isClose = isCloseOnIncoming(instanceSettings, templateWidget, targetSocketId, sourceInstance, sourceSocketId);
        final boolean isSelect = isSelect(instanceSettings, templateWidget, targetSocketId, sourceInstance, sourceSocketId);
        if(instanceSettings.isReuseExisting())
        {
            if(CollectionUtils.isEmpty(templateWidget.getWidgetInstances()))
            {
                if(isCreate)
                {
                    ret = createWidgetInstance(templateWidget, parentInstance, sourceInstance);
                }
            }
            else
            {
                // here we have at least one template instance
                if(SocketEventRoutingMode.SELECTED.equals(instanceSettings.getSocketEventRoutingMode()))
                {
                    if(parentInstance != null)
                    {
                        final int parentSelectionIndex = parentInstance.getSelectedChildIndex();
                        final List<WidgetInstance> displayedChildren = widgetInstanceFacade.getWidgetInstances(templateWidget,
                                        parentInstance);
                        WidgetInstance selectedWidgetInstance = null;
                        if(parentSelectionIndex >= 0 && parentSelectionIndex < displayedChildren.size())
                        {
                            selectedWidgetInstance = displayedChildren.get(parentSelectionIndex);
                        }
                        if(selectedWidgetInstance != null)
                        {
                            ret = selectedWidgetInstance;
                            suppressUpdate = true;
                        }
                        else if(isCreate) // TODO
                        // handle
                        // create_if_resolve_failed
                        // in
                        // special
                        // way?
                        {
                            ret = createWidgetInstance(templateWidget, parentInstance, sourceInstance);
                        }
                    }
                }
                else
                {
                    if(SocketEventRoutingMode.FIRST.equals(instanceSettings.getSocketEventRoutingMode())
                                    && CollectionUtils.isNotEmpty(templateWidget.getWidgetInstances()))
                    {
                        ret = templateWidget.getWidgetInstances().get(0);
                        suppressUpdate = true;
                    }
                    else if(SocketEventRoutingMode.LAST.equals(instanceSettings.getSocketEventRoutingMode())
                                    && CollectionUtils.isNotEmpty(templateWidget.getWidgetInstances()))
                    {
                        ret = templateWidget.getWidgetInstances().get(templateWidget.getWidgetInstances().size() - 1);
                        suppressUpdate = true;
                    }
                    else if(SocketEventRoutingMode.LAST_USED.equals(instanceSettings.getSocketEventRoutingMode()))
                    {
                        final WidgetInstance lastFocusedInstance = templateWidget.getLastFocusedInstance();
                        if(lastFocusedInstance != null)
                        {
                            ret = lastFocusedInstance;
                            suppressUpdate = true;
                        }
                        else
                        {
                            final List<WidgetInstance> displayedChildren = widgetInstanceFacade.getWidgetInstances(templateWidget,
                                            parentInstance);
                            if(CollectionUtils.isNotEmpty(displayedChildren))
                            {
                                ret = displayedChildren.iterator().next();
                                suppressUpdate = true;
                            }
                            else if(isCreate) // TODO
                            // handle
                            // create_if_resolve_failed
                            // in
                            // special
                            // way?
                            {
                                ret = createWidgetInstance(templateWidget, parentInstance, sourceInstance);
                            }
                        }
                    }
                }
            }
        }
        else if(isCreate)
        {
            ret = createWidgetInstance(templateWidget, parentInstance, sourceInstance);
        }
        else if(isClose)
        {
            WidgetInstance toRemove = ret;
            if(ret == null)
            {
                for(final WidgetInstance widgetInstance : templateWidget.getWidgetInstances())
                {
                    if(sourceInstance.getParent() != null && sourceInstance.getParent().equals(widgetInstance))
                    {
                        toRemove = widgetInstance;
                        break;
                    }
                }
            }
            if(toRemove != null)
            {
                ret = toRemove;
                parentInstance = ret.getParent();
                widgetInstanceFacade.removeWidgetInstance(toRemove);
            }
            suppressUpdate = false;
        }
        if(ret != null)
        {
            if(isSelect)
            {
                selectInstance(ret);
            }
            final RuleEngineResult ruleEngineResult = new RuleEngineResult(Collections.singletonList(ret),
                            ret.getParent() == null ? parentInstance : ret.getParent(), !suppressUpdate);
            ruleEngineResult.setWidgetAboutToShow(isCreate || isSelect);
            ruleEngineResult.setWidgetAboutToClose(isClose);
            return ruleEngineResult;
        }
        else
        {
            return new RuleEngineResult(Collections.<WidgetInstance>emptyList(), false);
        }
    }


    /**
     * Creates a new {@link WidgetInstance} by using
     * {@link WidgetInstanceFacade#createWidgetInstance(Widget, WidgetInstance)} and sets
     * the creator widget (the one whose event triggered the instantiation) and the template root (self reference).
     */
    protected WidgetInstance createWidgetInstance(final Widget templateWidget, final WidgetInstance parentInstance,
                    final WidgetInstance creator)
    {
        final WidgetInstance instance = widgetInstanceFacade.createWidgetInstance(templateWidget, parentInstance);
        if(instance instanceof DefaultWidgetInstance)
        {
            ((DefaultWidgetInstance)instance).setCreator(creator);
            ((DefaultWidgetInstance)instance).setTemplateRoot(instance);
        }
        return instance;
    }


    /**
     * @param settings
     * @param templateWidget
     * @param targetSocketId
     * @param sourceInstance
     * @param sourceSocketId
     * @return
     */
    protected boolean isCreate(final WidgetInstanceSettings settings, final Widget templateWidget, final String targetSocketId,
                    final WidgetInstance sourceInstance, final String sourceSocketId)
    {
        if(settings.isCreateOnAllIncomingEvents())
        {
            return true;
        }
        else
        {
            return settings.getCreateOnIncomingEvents().contains(targetSocketId);
        }
    }


    /**
     * @param settings
     * @param templateWidget
     * @param targetSocketId
     * @param sourceInstance
     * @param sourceSocketId
     * @return
     */
    protected boolean isCloseOnIncoming(final WidgetInstanceSettings settings, final Widget templateWidget,
                    final String targetSocketId, final WidgetInstance sourceInstance, final String sourceSocketId)
    {
        if(settings.isCloseOnAllIncomingEvents())
        {
            return true;
        }
        else
        {
            return settings.getCloseOnIncomingEvents().contains(targetSocketId);
        }
    }


    /**
     * @param settings
     * @param templateWidget
     * @param targetSocketId
     * @param sourceInstance
     * @param sourceSocketId
     * @return
     */
    protected boolean isSelect(final WidgetInstanceSettings settings, final Widget templateWidget, final String targetSocketId,
                    final WidgetInstance sourceInstance, final String sourceSocketId)
    {
        if(settings.isSelectOnAllIncomingEvents())
        {
            return true;
        }
        else
        {
            return settings.getSelectOnIncomingEvents().contains(targetSocketId);
        }
    }


    public void selectInstance(final WidgetInstance widgetInstance)
    {
        if(widgetInstance == null || widgetInstance.getWidget() == null)
        {
            LOG.error("Could not set selected template instance.");
        }
        else
        {
            widgetInstance.getParent().setSelectedChildIndex(
                            widgetInstanceFacade.getWidgetInstances(widgetInstance.getParent(), widgetInstance.getWidget().getSlotId(), false)
                                            .indexOf(widgetInstance));
            widgetInstance.getWidget().setLastFocusedTemplateInstance(widgetInstance);
        }
    }


    @Override
    public RuleEngineResult handleOutcomingEvent(final WidgetInstance sourceInstance, final String sourceSocketId)
    {
        final WidgetInstanceSettings instanceSettings = sourceInstance.getWidget().getWidgetInstanceSettings();
        final boolean isClose = isCloseOnOutgoing(instanceSettings, sourceInstance, sourceSocketId);
        if(isClose)
        {
            widgetInstanceFacade.removeWidgetInstance(sourceInstance);
            final RuleEngineResult ruleEngineResult = new RuleEngineResult(Collections.singletonList(sourceInstance), true);
            ruleEngineResult.setWidgetAboutToClose(true);
            return ruleEngineResult;
        }
        else
        {
            return new RuleEngineResult(Collections.<WidgetInstance>emptyList(), false);
        }
    }


    /**
     * @param settings
     * @param sourceInstance
     * @param sourceSocketId
     * @return
     */
    protected boolean isCloseOnOutgoing(final WidgetInstanceSettings settings, final WidgetInstance sourceInstance,
                    final String sourceSocketId)
    {
        if(settings.isCloseOnAllOutgoingEvents())
        {
            return true;
        }
        else
        {
            return settings.getCloseOnOutgoingEvents().contains(sourceSocketId);
        }
    }


    @Required
    public void setWidgetInstanceFacade(final WidgetInstanceFacade widgetInstanceFacade)
    {
        this.widgetInstanceFacade = widgetInstanceFacade;
    }
}
